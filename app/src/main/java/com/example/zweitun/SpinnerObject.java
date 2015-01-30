package com.example.zweitun;


public class SpinnerObject {
    private int id;
    private String value;

    public SpinnerObject(int id, String value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
