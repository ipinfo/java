package io.ipinfo.api.model;

public class CountryCurrency {
    private final String code;
    private final String symbol;

    public CountryCurrency(
            String code,
            String symbol
    ) {
        this.code = code;
        this.symbol = symbol;
    }

    public String getCode() {
        return code;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return "CountryCurrency{" +
                "code='" + code + '\'' +
                ",symbol='" + symbol + '\'' +
                '}';
    }
}
