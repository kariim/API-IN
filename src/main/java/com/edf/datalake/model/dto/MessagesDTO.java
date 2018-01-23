package com.edf.datalake.model.dto;

import org.json.simple.JSONObject;

import java.util.List;

public class MessagesDTO {

    public Status status;
    public List<JSONObject> messages;

    public MessagesDTO(Status status, List<JSONObject> messages) {
        this.status = status;
        this.messages = messages;
    }
}
