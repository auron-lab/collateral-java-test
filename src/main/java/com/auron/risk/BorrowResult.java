package com.auron.risk;

public record BorrowResult(boolean allowed, long maxDebt, long totalDebt) {
    public String toJson() {
        return "{\"allowed\":" + allowed
                + ",\"max_debt\":" + maxDebt
                + ",\"total_debt\":" + totalDebt
                + "}";
    }
}
