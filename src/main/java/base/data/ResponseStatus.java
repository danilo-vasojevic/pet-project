package base.data;

public enum ResponseStatus {
    PASSED("PASSED"),
    FAILED("FAILED"),
    ERROR("ERROR"),
    UNKNOWN("UNKNOWN");

    private String status;

    ResponseStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {return status;}

    public static ResponseStatus tryGetValue(String name) {
        try {
            return valueOf(name.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }
}
