package com.auron.desk;

public record Position(String id, String marketId, String owner, long collateralValue, long debt) {
    public String toJson() {
        return "{\"id\":" + JsonText.quote(id)
                + ",\"market_id\":" + JsonText.quote(marketId)
                + ",\"owner\":" + JsonText.quote(owner)
                + ",\"collateral_value\":" + collateralValue
                + ",\"debt\":" + debt
                + "}";
    }
}
