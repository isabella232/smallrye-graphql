package io.smallrye.graphql.client;

import java.util.Map;

import javax.json.JsonObject;

public interface Request {
    String getDocument();

    Map<String, Object> getVariables();

    void setVariables(Map<String, Object> variables);

    Object getVariable(String key);

    Request setVariable(String key, Object value);

    Request resetVariables();

    String toJson();

    JsonObject toJsonObject();

}
