package ru.rgr.types;

public class IntegerType {
    private int value;

    public IntegerType(int value) {
        this.value = value;
    }

    public int getIntValue() {
        return this.value;
    }

    public void setIntValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
