package com.edf.datalake.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "authorized_api_keys")
@EntityListeners(ApiKey.class)
public class ApiKey {

    @Id
    @Column(name = "client_key")
    private String id;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "authorized_api_keys_topics",
            joinColumns = @JoinColumn(name = "client_key", referencedColumnName = "client_key"),
            inverseJoinColumns = @JoinColumn(name = "url_suffix", referencedColumnName = "url_suffix"))
    private List<KafkaTopic> topics;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<KafkaTopic> getTopics() {
        return topics;
    }

    public void setTopics(List<KafkaTopic> topics) {
        this.topics = topics;
    }
}
