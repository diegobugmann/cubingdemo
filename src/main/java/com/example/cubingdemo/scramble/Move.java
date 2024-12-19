package com.example.cubingdemo.scramble;

public enum Move {

    // Define enum constants with their corresponding display values
    U("U"),
    U_PRIME("U'"),
    U2("U2"),
    D("D"),
    D_PRIME("D'"),
    D2("D2"),
    R("R"),
    R_PRIME("R'"),
    R2("R2"),
    L("L"),
    L_PRIME("L'"),
    L2("L2"),
    F("F"),
    F_PRIME("F'"),
    F2("F2"),
    B("B"),
    B_PRIME("B'"),
    B2("B2");

    private final String displayValue;

    Move(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }

}