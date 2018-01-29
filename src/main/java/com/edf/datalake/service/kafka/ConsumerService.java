package com.edf.datalake.service.kafka;

import com.edf.datalake.model.ApiKey;
import com.edf.datalake.model.KafkaTopic;
import com.edf.datalake.model.dto.MessagesDTO;
import com.edf.datalake.model.dto.Status;
import com.edf.datalake.service.dao.ApiKeyRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class ConsumerService {

    @Autowired
    private Environment env;

    @Autowired
    private ApiKeyRepository repository;

    private ConcurrentHashMap<String, ConcurrentHashMap<String, ConsumerExecutor>> consumers;
    private Logger logger = LoggerFactory.getLogger(ConsumerService.class);
    private String POLL_TME = "poll.time";
    private JSONParser jsonParser;


    @PostConstruct
    public void initConsumers() {

        logger.info("Begin initialization process !");

        final String SECURITY_LOGIN       = "java.security.auth.login.config";
        final String SECURITY_KRB5        = "java.security.krb5.conf";
        final String BOOTSTRAP_SERVERS    = "bootstrap.servers";
        final String ZOOKEEPER            = "zookeeper";
        final String GROUP_ID             = "group.id";
        final String SECURITY_PROTOCOL    = "security.protocol";
        final String TRUSTSTORE_LOCATION  = "ssl.truststore.location";
        final String TRUSTSTORE_PASSWORD  = "ssl.truststore.password";
        final String KEY_DESERIALIZER     = "key.deserializer";
        final String VALUE_DESERIALIZER   = "value.deserializer";
        final String AUTO_COMMIT          = "enable.auto.commit";
        final String AUTO_COMMIT_INTERVAL = "auto.commit.interval.ms";
        final String SESSION_TIMEOUT      = "session.timeout.ms";
        final String MAX_POLL_RECORDS     = "max.poll.records";
        final String POOL_CONSUMER_SIZE   = "pool.consumer.size";

        consumers = new ConcurrentHashMap<>();
        jsonParser = new JSONParser();

        Properties config = new Properties();

        System.setProperty(SECURITY_LOGIN, env.getProperty(SECURITY_LOGIN));
        System.setProperty(SECURITY_KRB5, env.getProperty(SECURITY_KRB5));

        config.put(BOOTSTRAP_SERVERS, env.getProperty(BOOTSTRAP_SERVERS));
        config.put(ZOOKEEPER, env.getProperty(ZOOKEEPER));
        config.put(SECURITY_PROTOCOL, env.getProperty(SECURITY_PROTOCOL));
        config.put(TRUSTSTORE_LOCATION, env.getProperty(TRUSTSTORE_LOCATION));
        config.put(TRUSTSTORE_PASSWORD, env.getProperty(TRUSTSTORE_PASSWORD));
        config.put(AUTO_COMMIT, env.getProperty(AUTO_COMMIT));
        config.put(AUTO_COMMIT_INTERVAL, env.getProperty(AUTO_COMMIT_INTERVAL));
        config.put(MAX_POLL_RECORDS, env.getProperty(MAX_POLL_RECORDS));
        config.put(SESSION_TIMEOUT, env.getProperty(SESSION_TIMEOUT));
        config.put(KEY_DESERIALIZER, env.getProperty(KEY_DESERIALIZER));
        config.put(VALUE_DESERIALIZER, env.getProperty(VALUE_DESERIALIZER));


        for(ApiKey apiKey : repository.findAll()) {
            consumers.put( apiKey.getId(), new ConcurrentHashMap<>() );

            for (KafkaTopic topic : apiKey.getTopics()) {
                config.put(GROUP_ID, topic.getId() + apiKey.getId());
                consumers.get(apiKey.getId()).put(topic.getId(), new ConsumerExecutor());

                for(int i=0; i<Integer.valueOf(env.getProperty(POOL_CONSUMER_SIZE)); i++) {
                    KafkaConsumer consumer = new KafkaConsumer<String, String>(config);
                    consumer.subscribe(Arrays.asList( topic.getId() ));

                    consumers.get(apiKey.getId()).get(topic.getId()).addConsumer(consumer);
                }
            }
        }

        logger.info("Initialization successfully completed !");
        logger.info("Configuration Tree is as follow :");

        for(ConcurrentHashMap.Entry<String, ConcurrentHashMap<String, ConsumerExecutor>> entryOne : consumers.entrySet()) {
            logger.info("API KEY : " + entryOne.getKey());

            for(Map.Entry<String, ConsumerExecutor> entryTwo : entryOne.getValue().entrySet()) {
                logger.info("\t\t TOPIC : " + entryTwo.getKey());
            }
        }

    }

    public MessagesDTO getMessages(String apiKey, String topic) {
        ConsumerRecords<String, String> records = consumers.get(apiKey).get(topic).
                consume(Long.valueOf(env.getProperty(POLL_TME)));

        List<JSONObject> events = new ArrayList<>();
        MessagesDTO result;

        try {

            for (ConsumerRecord<String, String> record : records) {
                JSONObject event = (JSONObject) jsonParser.parse(record.value());
                events.add(event);
            }

            result = new MessagesDTO(Status.GRANTED, events);

        } catch (WakeupException e) {
            result = new MessagesDTO(Status.BUSY_ERROR, null);
        } catch (ParseException e) {
            result = new MessagesDTO(Status.PARSE_ERROR, events);
        }



        return result;
    }

    public void shutdown() {
        logger.info("Closing Kafka Consumers");

        for(Map.Entry<String, ConcurrentHashMap<String, ConsumerExecutor>> entry1 : consumers.entrySet()) {
            for( Map.Entry<String, ConsumerExecutor> entry2 : entry1.getValue().entrySet() ) {
                for(KafkaConsumer consumer : entry2.getValue().getConsumers()) {
                    try {
                        consumer.close();
                    } catch (IllegalStateException e) {
                        logger.error("Already closed !");
                    }

                }
            }
        }

        logger.info("All Kafka consumers have been shutdown successfully !");
    }
}



