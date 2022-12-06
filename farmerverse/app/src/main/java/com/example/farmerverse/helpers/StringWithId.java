package com.example.farmerverse.helpers;
public class StringWithId {

/**
 * This class is to help with spinners
 * It enables us to have an id attached to each spinner item in order to query the database
 * with the given id
 * toString is called by the spinner automatically as what it returns is what the user sees
 * as the options in the spinner
 */
public class StringWithId {
    private String string;
    private int id;

    public StringWithId(String string, int id) {
        this.string = string;
        this.id = id;
    }

    @Override
    public String toString() {
        return string;
    }

    public int getId() {
        return id;
    }
}
