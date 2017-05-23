package com.parasites.models;

import javafx.scene.control.PasswordField;

/**
 * Created by spyro on 23/05/2017.
 */
public class PasswordAPICall extends RestApiCall {

    public PasswordAPICall(String password, String token){
        super("http://localhost:3000/users/update/password/"
                + password + "?token=" + token, "", "PUT");

    }
}