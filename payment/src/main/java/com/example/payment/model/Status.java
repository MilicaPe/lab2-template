package com.example.payment.model;

public enum Status {
    PAID (0),
    CANCELED(1);

    private final int value;

    Status(int value) {
        this.value = value;
    }

    public static Status valueOf(int value) {
        switch (value) {
            case 1:
                return Status.CANCELED;
            default:
                return Status.PAID;
        }
    }
    public int getValue() {
        return value;
    }

}
