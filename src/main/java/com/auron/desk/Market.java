package com.auron.desk;

public record Market(String id, String collateralSymbol, String debtSymbol, int maxLtvBps) {
    public String toJson() {
        return "{\"id\":" + JsonText.quote(id)
                + ",\"collateral_symbol\":" + JsonText.quote(collateralSymbol)
                + ",\"debt_symbol\":" + JsonText.quote(debtSymbol)
                + ",\"max_ltv_bps\":" + maxLtvBps
                + "}";
    }
}
