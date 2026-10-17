package com.auron.http;

import com.auron.desk.JsonText;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public final class HttpKit {
    private HttpKit() {
    }

    public static void json(HttpExchange exchange, int status, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(status, bytes.length);
        exchange.getResponseBody().write(bytes);
        exchange.close();
    }

    public static void error(HttpExchange exchange, int status, String message) throws IOException {
        json(exchange, status, "{\"error\":" + JsonText.quote(message) + "}");
    }

    public static String readBody(HttpExchange exchange) throws IOException {
        try (InputStream input = exchange.getRequestBody()) {
            return new String(input.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    public static boolean method(HttpExchange exchange, String expected) throws IOException {
        if (exchange.getRequestMethod().equals(expected)) {
            return true;
        }
        error(exchange, 405, "method not allowed");
        return false;
    }

    public static long longField(String json, String key) {
        String needle = "\"" + key + "\"";
        int keyAt = json.indexOf(needle);
        if (keyAt < 0) {
            throw new IllegalArgumentException("missing " + key);
        }
        int colon = json.indexOf(':', keyAt + needle.length());
        if (colon < 0) {
            throw new IllegalArgumentException("missing " + key);
        }
        int start = colon + 1;
        while (start < json.length() && Character.isWhitespace(json.charAt(start))) {
            start++;
        }
        int end = start;
        if (end < json.length() && json.charAt(end) == '-') {
            end++;
        }
        while (end < json.length() && Character.isDigit(json.charAt(end))) {
            end++;
        }
        if (end == start || (end == start + 1 && json.charAt(start) == '-')) {
            throw new IllegalArgumentException("invalid " + key);
        }
        return Long.parseLong(json.substring(start, end));
    }
}
