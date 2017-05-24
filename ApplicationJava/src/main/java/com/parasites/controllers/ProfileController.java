package com.parasites.controllers;

import com.parasites.models.FirstnameAPICall;
import com.parasites.models.LastnameAPICall;
import com.parasites.models.PasswordAPICall;
import com.parasites.network.OnlineServerManager;

/**
 * Created by spyro on 22/05/2017.
 */
public class ProfileController {

    public String getMessage() {
        return message;
    }

    private String message;
    private String token;

    public ProfileController(String token,
                             String password,
                             String password_confirming,
                             String mail,
                             String mail_confirming,
                             String firstname,
                             String lastname,
                             String phone){
        this.token = token;
        message = "";
        message += changePassword(password, password_confirming);
        message += changeMail(mail, mail_confirming);
        message += changeFirstname(firstname);
        message += changeLastname(lastname);
        message += changePhone(phone);
    }

    public String changePassword(String password, String password_confirming){
        if(password.equals("")) return "";
        if(!password.equals(password_confirming))
            return "Le mot de passe et sa confirmation ne correspondent pas.\n";
        PasswordAPICall call = new PasswordAPICall(password, token);
        if(call.getCodeResponse() != 200){
            return call.getMessage() + "\n";
        }
        OnlineServerManager.getInstance().getCurrentUser().setPassword(password);
        return "Le mot de passe a bien été modifié.\n";
    }

    public String changeMail(String mail, String mail_confirming){
        if(mail.equals("")) return "";
        if(!mail.equals(mail_confirming))
            return "Le mail et sa confirmation ne correspondent pas.\n";
        return "";
    }

    public String changeFirstname(String firstname){
        if(firstname.equals("")) return "";
        FirstnameAPICall call = new FirstnameAPICall(firstname, token);
        if(call.getCodeResponse() != 200){
            return call.getMessage() + "\n";
        }
        OnlineServerManager.getInstance().getCurrentUser().setFirstname(firstname);
        return "Le prénom a bien été modifié.\n";
    }

    public String changeLastname(String lastname){
        if(lastname.equals("")) return "";
        LastnameAPICall call = new LastnameAPICall(lastname, token);
        if(call.getCodeResponse() != 200){
            return call.getMessage() + "\n";
        }
        OnlineServerManager.getInstance().getCurrentUser().setLastname(lastname);
        return "Le nom de famille a bien été modifié.\n";
    }

    public String changePhone(String phone){
        if(phone.equals("")) return "";
        return "";
    }
}
