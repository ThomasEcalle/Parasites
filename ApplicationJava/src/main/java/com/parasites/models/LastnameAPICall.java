package com.parasites.models;

/**
 * Created by spyro on 24/05/2017.
 */
public class LastnameAPICall extends RestApiCall {
    public LastnameAPICall(String lastname, String token){
        super("/users/update/lastname/"
                + lastname + "?token=" + token, "", "PUT");
    }
}