package com.parasites.controllers;

/**
 * Created by spyro on 22/05/2017.
 */
public class ProfileController {

    private String message;

    public ProfileController(String password,
                             String password_confirmation,
                             String mail,
                             String mail_confirmation,
                             String firstname,
                             String lastname,
                             String phone){
        message += changePassword(password, password_confirmation);
        message += changeMail(mail, mail_confirmation);
        message += changeFirstname(firstname);
        message += changeLastname(lastname);
        message += changePhone(phone);
    }

    public String changePassword(String password, String password_confirmation){
        if(password == "") return "";
        return "";
    }

    public String changeMail(String mail, String mail_confirmation){
        if(mail == "") return "";
        return "";
    }

    public String changeFirstname(String firstname){
        if(firstname == "") return "";
        return "";
    }

    public String changeLastname(String lastname){
        if(lastname == "") return "";
        return "";
    }

    public String changePhone(String phone){
        if(phone == "") return "";
        return "";
    }
}
