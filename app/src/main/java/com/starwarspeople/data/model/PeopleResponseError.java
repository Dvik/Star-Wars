package com.starwarspeople.data.model;

/**
 * Created by Divya on 1/22/2017.
 */

public class PeopleResponseError {

    private int status;
    private String message;

    public PeopleResponseError() {
    }

    public int status() {
        return status;
    }

    public String message() {
        return message;
    }


}
