package com.edf.datalake.service;

import com.edf.datalake.model.ApiKey;
import com.edf.datalake.model.KafkaTopic;
import com.edf.datalake.model.dto.MessagesDTO;
import com.edf.datalake.service.dao.ApiKeyRepository;
import com.edf.datalake.service.kafka.ConsumerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AccessPointService {

    @Autowired
    private ApiKeyRepository apiKeyRepository;

    @Autowired
    private ConsumerService consumer;

    private Logger logger = LoggerFactory.getLogger(AccessPointService.class);

    @Async
    public MessagesDTO getCurrentMessages(String apiKey, String topic) {
        return consumer.getMessages(apiKey, topic);
    }

    public Boolean checkPrerequisites(String topic, String apiKey) {
        KafkaTopic temporaryTopic = new KafkaTopic(topic);
        ApiKey entity = apiKeyRepository.findOne(apiKey);


        if(entity == null || entity.getTopics() == null || entity.getTopics().isEmpty())
            return Boolean.FALSE;

        return (entity.getTopics().contains(temporaryTopic));
    }

}
