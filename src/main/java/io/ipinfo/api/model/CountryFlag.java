package io.ipinfo.api.model;

public class CountryFlag {
    private final String emoji;
    private final String unicode;

    public CountryFlag(
            String emoji,
            String unicode
    ) {
        this.emoji = emoji;
        this.unicode = unicode;
    }

    public String getEmoji() {
        return emoji;
    }

    public String getUnicode() {
        return unicode;
    }

    @Override
    public String toString() {
        return "CountryFlag{" +
                "emoji='" + emoji + '\'' +
                ",unicode='" + unicode + '\'' +
                '}';
    }
}
