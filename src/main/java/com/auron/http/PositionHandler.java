package com.auron.http;

import com.auron.desk.DeskBook;
import com.auron.desk.Market;
import com.auron.desk.Position;
import com.auron.risk.BorrowResult;
import com.auron.risk.BorrowingLimitService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.Optional;

public final class PositionHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        if (path.endsWith("/borrow-check")) {
            borrow(exchange, path);
            return;
        }
        if (!HttpKit.method(exchange, "GET")) {
            return;
        }
        if ("/v1/positions".equals(path)) {
            HttpKit.json(exchange, 200, DeskBook.positionsJson());
            return;
        }
        String prefix = "/v1/positions/";
        if (path.startsWith(prefix) && path.indexOf('/', prefix.length()) < 0) {
            Optional<Position> position = DeskBook.findPosition(path.substring(prefix.length()));
            if (position.isEmpty()) {
                HttpKit.error(exchange, 404, "unknown position");
                return;
            }
            HttpKit.json(exchange, 200, position.get().toJson());
            return;
        }
        HttpKit.error(exchange, 404, "not found");
    }

    private static void borrow(HttpExchange exchange, String path) throws IOException {
        if (!HttpKit.method(exchange, "POST")) {
            return;
        }
        String prefix = "/v1/positions/";
        String suffix = "/borrow-check";
        if (!path.startsWith(prefix) || !path.endsWith(suffix)) {
            HttpKit.error(exchange, 404, "not found");
            return;
        }
        String id = path.substring(prefix.length(), path.length() - suffix.length());
        Optional<Position> position = DeskBook.findPosition(id);
        if (position.isEmpty()) {
            HttpKit.error(exchange, 404, "unknown position");
            return;
        }
        Optional<Market> market = DeskBook.findMarket(position.get().marketId());
        if (market.isEmpty()) {
            HttpKit.error(exchange, 404, "unknown market");
            return;
        }
        try {
            long additional = HttpKit.longField(HttpKit.readBody(exchange), "additional_borrow");
            BorrowResult result = BorrowingLimitService.evaluateBorrow(
                    position.get().collateralValue(),
                    position.get().debt(),
                    additional,
                    market.get().maxLtvBps());
            HttpKit.json(exchange, 200, result.toJson());
        } catch (IllegalArgumentException exception) {
            HttpKit.error(exchange, 400, exception.getMessage());
        } catch (RuntimeException exception) {
            HttpKit.error(exchange, 500, exception.getMessage());
        }
    }
}
