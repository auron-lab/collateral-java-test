package com.auron.http;

import com.auron.desk.DeskBook;
import com.auron.desk.Market;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.Optional;

public final class MarketHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!HttpKit.method(exchange, "GET")) {
            return;
        }
        String path = exchange.getRequestURI().getPath();
        if ("/v1/markets".equals(path)) {
            HttpKit.json(exchange, 200, DeskBook.marketsJson());
            return;
        }
        String prefix = "/v1/markets/";
        if (path.startsWith(prefix) && path.indexOf('/', prefix.length()) < 0) {
            Optional<Market> market = DeskBook.findMarket(path.substring(prefix.length()));
            if (market.isEmpty()) {
                HttpKit.error(exchange, 404, "unknown market");
                return;
            }
            HttpKit.json(exchange, 200, market.get().toJson());
            return;
        }
        HttpKit.error(exchange, 404, "not found");
    }
}
