package com.parasites.models;

/**
 * Created by spyro on 24/05/2017.
 */
public class PhoneAPICall extends RestApiCall{
    public PhoneAPICall(String phone, String token){
        super("/users/update/phone/"
                + phone + "?token=" + token, "", "PUT");
    }
}