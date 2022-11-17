package io.ipinfo.api.model;

public class Continent {
    private final String code;
    private final String name;

    public Continent(
            String code,
            String name
    ) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Continent{" +
                "code='" + code + '\'' +
                ",name='" + name + '\'' +
                '}';
    }
}
