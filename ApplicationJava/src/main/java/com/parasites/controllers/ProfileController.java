package com.parasites.controllers;

import com.parasites.models.*;
import com.parasites.network.OnlineServerManager;

/**
 * Created by spyro on 22/05/2017.
 */
public class ProfileController {

    private String token;
    private Object[] password_message;
    private Object[] mail_message;
    private Object[] firstname_message;
    private Object[] lastname_message;
    private Object[] phone_message;

    public Object[] getPassword_message() {
        return password_message;
    }

    public Object[] getMail_message() {
        return mail_message;
    }

    public Object[] getFirstname_message() {
        return firstname_message;
    }

    public Object[] getLastname_message() {
        return lastname_message;
    }

    public Object[] getPhone_message() {
        return phone_message;
    }

    public ProfileController(String token,
                             String password,
                             String password_confirming,
                             String mail,
                             String mail_confirming,
                             String firstname,
                             String lastname,
                             String phone){
        this.token = token;
        password_message = changePassword(password, password_confirming);
        mail_message = changeMail(mail, mail_confirming);
        firstname_message = changeFirstname(firstname);
        lastname_message = changeLastname(lastname);
        phone_message = changePhone(phone);
    }

    public Object[] changePassword(String password, String password_confirming){
        if(password.equals("")) return null;
        if(!password.equals(password_confirming))
            return new Object[] {"Le mot de passe et sa confirmation ne correspondent pas.", 0 };
        PasswordAPICall call = new PasswordAPICall(password, token);
        if(call.getCodeResponse() != 200){
            return new Object[] { call.getMessage(), 0 };
        }
        OnlineServerManager.getInstance().getCurrentUser().setPassword(password);
        return new Object[] { "Le mot de passe a bien été modifié.", 1 };
    }

    public Object[] changeMail(String mail, String mail_confirming){
        if(mail.equals("")) return null;
        if(!mail.equals(mail_confirming))
            return new Object[] { "Le mail et sa confirmation ne correspondent pas.", 0 };
        MailAPICall call = new MailAPICall(mail, token);
        if(call.getCodeResponse() != 200){
            return new Object[] { call.getMessage(), 0 };
        }
        OnlineServerManager.getInstance().getCurrentUser().setEmail(mail);
        return new Object[] { "Le mail a bien été changé.", 1 };
    }

    public Object[] changeFirstname(String firstname){
        if(firstname.equals("")) return null;
        FirstnameAPICall call = new FirstnameAPICall(firstname, token);
        if(call.getCodeResponse() != 200){
            return new Object[] { call.getMessage(), 0 };
        }
        OnlineServerManager.getInstance().getCurrentUser().setFirstname(firstname);
        return new Object[] { "Le prénom a bien été modifié.", 1 };
    }

    public Object[] changeLastname(String lastname){
        if(lastname.equals("")) return null;
        LastnameAPICall call = new LastnameAPICall(lastname, token);
        if(call.getCodeResponse() != 200){
            return new Object[] { call.getMessage(), 0 };
        }
        OnlineServerManager.getInstance().getCurrentUser().setLastname(lastname);
        return new Object[] { "Le nom de famille a bien été modifié.", 1 };
    }

    public Object[] changePhone(String phone){
        if(phone.equals("")) return null;
        PhoneAPICall call = new PhoneAPICall(phone, token);
        if(call.getCodeResponse() != 200){
            return new Object[] { call.getMessage(), 0 };
        }
        OnlineServerManager.getInstance().getCurrentUser().setPhone_number(phone);
        return new Object[] { "Le numéro de téléphone a bien été modifié.", 1 };
    }
}
