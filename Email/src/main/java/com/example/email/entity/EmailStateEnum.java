package com.example.email.entity;

public enum EmailStateEnum {
    SEND(1),
    DRAFT(2),
    DELETED(3),
    SPAM(4);

    private final int stateCode;

    EmailStateEnum(int stateCode) {
        this.stateCode = stateCode;
    }

    public int getStateCode() {
        return stateCode;
    }

    public static EmailStateEnum fromStateCode(int stateCode) {
        for (EmailStateEnum state : values()) {
            if (state.getStateCode() == stateCode) {
                return state;
            }
        }
        throw new IllegalArgumentException("Unknown state code: " + stateCode);
    }
}
