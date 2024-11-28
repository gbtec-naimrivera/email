package com.example.email.entity;

import lombok.Getter;

/**
 * <p>Enum representing the possible states of an email.</p>
 * <p>This enum defines the various states that an email can have, such as sent, draft, deleted, and spam.</p>
 */
@Getter
public enum EmailStateEnum {

    /**
     * <p>Indicates that the email has been sent.</p>
     */
    SENT(1),

    /**
     * <p>Indicates that the email is a draft and has not been sent.</p>
     */
    DRAFT(2),

    /**
     * <p>Indicates that the email has been deleted.</p>
     */
    DELETED(3),

    /**
     * <p>Indicates that the email has been marked as spam.</p>
     */
    SPAM(4);

    /**
     * <p>The state code associated with the email state.</p>
     */
    private final int stateCode;

    /**
     * <p>Constructor to initialize the state with a specific code.</p>
     *
     * @param stateCode The code representing the email state.
     */
    EmailStateEnum(int stateCode) {
        this.stateCode = stateCode;
    }

    /**
     * <p>Converts a state code to its corresponding {@link EmailStateEnum} value.</p>
     *
     * @param stateCode The state code to convert.
     * @return The corresponding {@link EmailStateEnum} value.
     * @throws IllegalArgumentException If the state code is unknown.
     */
    public static EmailStateEnum fromStateCode(int stateCode) {
        for (EmailStateEnum state : values()) {
            if (state.getStateCode() == stateCode) {
                return state;
            }
        }
        throw new IllegalArgumentException("Unknown state code: " + stateCode);
    }
}
