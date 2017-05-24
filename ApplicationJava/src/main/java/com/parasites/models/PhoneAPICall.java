package com.parasites.models;

/**
 * Created by spyro on 24/05/2017.
 */
public class PhoneAPICall extends RestApiCall{
    public PhoneAPICall(String phone, String token){
        super("http://localhost:3000/users/update/phone/"
                + phone + "?token=" + token, "", "PUT");
    }
}