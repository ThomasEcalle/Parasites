package com.parasites.controllers;

/**
 * Created by spyro on 22/05/2017.
 */
public class ProfileController {

    private String password;
    private String password_confirmation;
    private String mail;
    private String mail_confirmation;
    private String firstname;
    private String lastname;
    private String phone;

    public ProfileController(String password,
                             String password_confirmation,
                             String mail,
                             String mail_confirmation,
                             String firstname,
                             String lastname,
                             String phone){
        this.password = password;
        this.password_confirmation = password_confirmation;
        this.mail = mail;
        this.mail_confirmation = mail_confirmation;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
    }

    public String displayAPICalls(){
        String message = "";
        if(password != "") {
            message += changePassword();
        }
        if(mail != "") {
            message += ("\n" + changeMail());
        }
        if(firstname != "") {
            message += ("\n" + changeFirstname());
        }
        if(lastname != "") {
            message += ("\n" + changeLastname());
        }
        if(phone != "") {
            message += ("\n" + changePhone());
        }
        return message;
    }

    public String changePassword(){
        return "ok";
    }

    public String changeMail(){
        return "ok";
    }

    public String changeFirstname(){
        return "ok";
    }

    public String changeLastname(){
        return "ok";
    }

    public String changePhone(){
        return "ok";
    }
}
