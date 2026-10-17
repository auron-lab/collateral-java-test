package com.auron.risk;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BorrowingLimitServiceTest {
    @Test
    void allowsBorrowBelowLimit() {
        BorrowResult result = BorrowingLimitService.evaluateBorrow(1_000, 500, 200, 7_500);

        assertTrue(result.allowed());
        assertEquals(750, result.maxDebt());
        assertEquals(700, result.totalDebt());
    }

    @Test
    void rejectsBorrowAboveLimit() {
        BorrowResult result = BorrowingLimitService.evaluateBorrow(1_000, 500, 300, 7_500);

        assertFalse(result.allowed());
        assertEquals(800, result.totalDebt());
    }

    @Test
    void allowsBorrowExactlyAtLimit() {
        BorrowResult result = BorrowingLimitService.evaluateBorrow(1_000, 500, 250, 7_500);

        assertTrue(result.allowed());
        assertEquals(750, result.totalDebt());
    }

    @Test
    void rejectsLtvAboveTenThousandBasisPoints() {
        assertThrows(IllegalArgumentException.class,
                () -> BorrowingLimitService.evaluateBorrow(1_000, 0, 100, 10_001));
    }
}
