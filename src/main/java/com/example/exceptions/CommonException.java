package com.example.exceptions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
public class CommonException extends RuntimeException {

    private final String errorCode;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final List<String> messageParams = new ArrayList<>();

    public CommonException(String errorCode, String message, Object... messageParams) {
        super(message);
        this.errorCode = errorCode;
        if (null != messageParams) {
            for (Object messageParam : messageParams) {
                this.messageParams.add((String) messageParam);
            }
        }
    }

    @Override
    public String toString() {
        ObjectNode rootNode = generateObjectNode();
        return rootNode.toString();
    }

    private ObjectNode generateObjectNode() {
        JsonNode messageParamsArray = fromCollection(getMessageParams());

        ObjectNode detailsNode = objectMapper.createObjectNode();
        detailsNode.put("errorCode", getErrorCode());
        detailsNode.put("message", getMessage());
        detailsNode.set("messageParams", messageParamsArray);

        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.set(this.getClass().getSimpleName(), detailsNode);
        return rootNode;
    }

    private <T> JsonNode fromCollection(Collection<T> collection) {
        ArrayNode arrayNode = objectMapper.createArrayNode();
        for (T param : collection) {
            arrayNode.addPOJO(param);
        }
        return arrayNode;
    }
}
