package com.auron.http;

import com.auron.risk.BorrowRequest;
import com.auron.risk.BorrowResult;
import com.auron.risk.BorrowingLimitService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public final class BorrowHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!HttpKit.method(exchange, "POST")) {
            return;
        }
        if (!"/v1/borrow-check".equals(exchange.getRequestURI().getPath())) {
            HttpKit.error(exchange, 404, "not found");
            return;
        }
        try {
            String body = HttpKit.readBody(exchange);
            BorrowRequest request = new BorrowRequest(
                    HttpKit.longField(body, "collateral_value"),
                    HttpKit.longField(body, "existing_debt"),
                    HttpKit.longField(body, "additional_borrow"),
                    (int) HttpKit.longField(body, "max_ltv_bps"));
            BorrowResult result = BorrowingLimitService.evaluateBorrow(
                    request.collateralValue(),
                    request.existingDebt(),
                    request.additionalBorrow(),
                    request.maxLtvBps());
            HttpKit.json(exchange, 200, result.toJson());
        } catch (IllegalArgumentException exception) {
            HttpKit.error(exchange, 400, exception.getMessage());
        } catch (RuntimeException exception) {
            HttpKit.error(exchange, 500, exception.getMessage());
        }
    }
}
