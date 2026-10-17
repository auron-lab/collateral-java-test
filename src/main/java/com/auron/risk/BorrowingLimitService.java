package com.auron.risk;

public final class BorrowingLimitService {
    private static final int BASIS_POINTS = 10_000;

    private BorrowingLimitService() {
    }

    /**
     * Checks whether a proposed borrow remains within the collateral limit.
     * Money values use the same unit and are assumed to be non-negative.
     *
     * @throws IllegalArgumentException if maxLtvBps is greater than 10,000
     */
    public static BorrowResult evaluateBorrow(
            long collateralValue,
            long existingDebt,
            long additionalBorrow,
            int maxLtvBps) {
        // TODO: reject invalid LTV, calculate max debt and total debt, and return the result.
        throw new UnsupportedOperationException("Implement evaluateBorrow");
    }
}
