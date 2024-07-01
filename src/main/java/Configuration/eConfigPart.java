package Configuration;

public enum eConfigPart {
    gold("gold"),
    hint("hint"),
    ant("ant");

    private String key;

    eConfigPart(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
