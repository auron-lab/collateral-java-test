package com.auron.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public final class HealthHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!HttpKit.method(exchange, "GET")) {
            return;
        }
        if (!"/health".equals(exchange.getRequestURI().getPath())) {
            HttpKit.error(exchange, 404, "not found");
            return;
        }
        HttpKit.json(exchange, 200, "{\"status\":\"ok\"}");
    }
}
