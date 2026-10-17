package com.auron.desk;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DeskBookTest {
    @Test
    void findsEthMarket() {
        Market market = DeskBook.findMarket("eth-usdc").orElseThrow();

        assertEquals("ETH", market.collateralSymbol());
        assertEquals(7_500, market.maxLtvBps());
    }

    @Test
    void listsTheSampleBook() {
        assertEquals(3, DeskBook.markets().size());
        assertEquals(3, DeskBook.positions().size());
        assertTrue(DeskBook.findPosition("missing").isEmpty());
        assertEquals("bob", DeskBook.findPosition("pos-1002").orElseThrow().owner());
    }
}
