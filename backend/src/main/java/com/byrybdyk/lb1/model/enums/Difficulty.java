package com.byrybdyk.lb1.model.enums;

public enum Difficulty {
    VERY_EASY(1),
    HARD(2),
    INSANE(3);

    private final int value;

    Difficulty(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Difficulty fromValue(int value) {
        for (Difficulty difficulty : Difficulty.values()) {
            if (difficulty.getValue() == value) {
                return difficulty;
            }
        }
        throw new IllegalArgumentException("Invalid difficulty value: " + value);
    }
}
