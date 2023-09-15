package com.akka.test.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


public class JsonConfigurationTools {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> T readFile(String configurationFile, Class<T> clazz) throws IOException {
        try {
            return mapper.readValue(getFileAsStream(configurationFile), clazz);
        } catch (IOException e) {
            throw e;
        }

    }

    private static InputStream getFileAsStream(String configurationFile) throws IOException {
        return new FileInputStream(new File(configurationFile));
    }


    public static <T> T readValue(String json, Class<T> clazz) throws IOException {
        try {
            return mapper.readValue(json, clazz);
        } catch (IOException e) {
            throw e;
        }
    }


    public static JsonNode fromBytes(byte[] bytes) throws IOException {
        try {
            return mapper.readTree(bytes);
        } catch (IOException e) {
            throw e;
        }
    }

    public static <T> T fromBytes(byte[] bytes, Class<T> clazz) throws IOException {
        try {
            return bytes != null ? mapper.readValue(bytes, clazz) : null;
        } catch (IOException e) {
            throw e;
        }
    }


    public static JsonNode toJsonNode(String value) throws IOException {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            return mapper.readTree(value);
        } catch (IOException e) {
            throw e;
        }
    }

    public static ObjectNode newNode() {
        return mapper.createObjectNode();
    }

    public static ArrayNode newArrayNode() {
        return mapper.createArrayNode();
    }

    public static String toString(Object detail) {

        try {
            return mapper.writeValueAsString(detail);
        } catch (JsonProcessingException e) {
            return "";
        }
    }

    public static boolean isJSON(String str) {

        boolean result = false;

        if (StringUtils.hasLength(str)) {

            str = str.trim();

            if (str.startsWith("{") && str.endsWith("}")) {

                result = true;

            } else if (str.startsWith("[") && str.endsWith("]")) {

                result = true;

            }

        }

        return result;

    }

}
