package com.auron.risk;

public record BorrowRequest(
        long collateralValue,
        long existingDebt,
        long additionalBorrow,
        int maxLtvBps) {
}
