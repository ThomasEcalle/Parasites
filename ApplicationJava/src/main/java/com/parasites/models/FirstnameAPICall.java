package com.parasites.models;

/**
 * Created by spyro on 24/05/2017.
 */
public class FirstnameAPICall extends RestApiCall{
    public FirstnameAPICall(String firstname, String token){
        super("/users/update/firstname/"
                + firstname + "?token=" + token, "", "PUT");
    }
}