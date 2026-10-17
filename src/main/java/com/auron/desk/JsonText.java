package com.auron.desk;

public final class JsonText {
    private JsonText() {
    }

    public static String quote(String value) {
        return "\"" + value.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
    }
}
