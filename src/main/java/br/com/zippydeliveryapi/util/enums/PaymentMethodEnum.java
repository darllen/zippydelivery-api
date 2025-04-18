package br.com.zippydeliveryapi.util.enums;

public enum PaymentMethodEnum {
    CASH(1, "Cash"),
    CREDIT_CARD(2, "Credit Card"),
    DEBIT_CARD(3, "Debit Card"),
    PIX(4, "PIX"),
    MEAL_VOUCHER(5, "Meal Voucher"),
    OTHER(6, "Other");

    private final int code;
    private final String description;

    PaymentMethodEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static PaymentMethodEnum fromCode(int code) {
        for (PaymentMethodEnum method : PaymentMethodEnum.values()) {
            if (method.getCode() == code) {
                return method;
            }
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }

    public static PaymentMethodEnum fromDescription(String description) {
        for (PaymentMethodEnum method : PaymentMethodEnum.values()) {
            if (method.getDescription().equalsIgnoreCase(description)) {
                return method;
            }
        }
        throw new IllegalArgumentException("Invalid description: " + description);
    }
}