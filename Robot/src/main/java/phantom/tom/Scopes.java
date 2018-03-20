package phantom.tom;

public enum Scopes {
    AUTONOMOUS("AUTONOMOUS"),
    MANUAL("MANUAL");

    public String name;
    private Scopes(String name) {
        this.name = name;
    }
}
