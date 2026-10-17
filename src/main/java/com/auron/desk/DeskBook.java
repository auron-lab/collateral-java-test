package com.auron.desk;

import java.util.List;
import java.util.Optional;

public final class DeskBook {
    private static final List<Market> MARKETS = List.of(
            new Market("eth-usdc", "ETH", "USDC", 7_500),
            new Market("wbtc-usdc", "WBTC", "USDC", 7_000),
            new Market("steth-usdc", "stETH", "USDC", 6_500));

    private static final List<Position> POSITIONS = List.of(
            new Position("pos-1001", "eth-usdc", "alice", 10_000, 5_200),
            new Position("pos-1002", "wbtc-usdc", "bob", 1_000, 500),
            new Position("pos-1003", "steth-usdc", "carol", 2_500, 0));

    private DeskBook() {
    }

    public static List<Market> markets() {
        return MARKETS;
    }

    public static Optional<Market> findMarket(String id) {
        return MARKETS.stream().filter(market -> market.id().equals(id)).findFirst();
    }

    public static List<Position> positions() {
        return POSITIONS;
    }

    public static Optional<Position> findPosition(String id) {
        return POSITIONS.stream().filter(position -> position.id().equals(id)).findFirst();
    }

    public static String marketsJson() {
        return jsonArray(MARKETS.stream().map(Market::toJson).toList());
    }

    public static String positionsJson() {
        return jsonArray(POSITIONS.stream().map(Position::toJson).toList());
    }

    private static String jsonArray(List<String> objects) {
        return "[" + String.join(",", objects) + "]";
    }
}
