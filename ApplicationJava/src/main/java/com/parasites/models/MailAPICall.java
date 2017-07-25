package com.parasites.models;

/**
 * Created by spyro on 24/05/2017.
 */
public class MailAPICall extends RestApiCall{
    public MailAPICall(String mail, String token){
        super("/users/update/email/"
                + mail + "?token=" + token, "", "PUT");
    }
}