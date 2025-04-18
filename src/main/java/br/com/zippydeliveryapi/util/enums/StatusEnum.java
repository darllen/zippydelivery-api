package br.com.zippydeliveryapi.util.enums;

public enum StatusEnum {
    ACTIVE(1, "Active"),
    INACTIVE(0, "Inactive"),
    PENDING(2, "Pending"),
    AWAITING_APPROVAL(3, "Awaiting Approval"),
    REFUNDED(4, "Refunded"),
    CANCELED(5, "Canceled"),
    PROCESSING(6, "Processing"),
    CONFIRMED(7, "Confirmed");

    private final int code;
    private final String description;

    StatusEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static StatusEnum fromCode(int code) {
        for (StatusEnum status : StatusEnum.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }

    public static StatusEnum fromDescription(String description) {
        for (StatusEnum status : StatusEnum.values()) {
            if (status.getDescription().equalsIgnoreCase(description)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid description: " + description);
    }
}