package com.parasites.controllers;

import com.parasites.models.*;
import com.parasites.network.OnlineServerManager;
import com.parasites.utils.InfoMessage;
import javafx.scene.paint.Color;

import javax.sound.midi.MidiDevice;

/**
 * Created by spyro on 22/05/2017.
 */
public class ProfileController {

    private String token;
    private InfoMessage password_message;
    private InfoMessage mail_message;
    private InfoMessage firstname_message;
    private InfoMessage lastname_message;
    private InfoMessage phone_message;

    public InfoMessage getPassword_message() {
        return password_message;
    }

    public InfoMessage getMail_message() {
        return mail_message;
    }

    public InfoMessage getFirstname_message() {
        return firstname_message;
    }

    public InfoMessage getLastname_message() {
        return lastname_message;
    }

    public InfoMessage getPhone_message() {
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

    public InfoMessage changePassword(String password, String password_confirming){
        if(password.equals("")) return null;
        if(!password.equals(password_confirming))
            return new InfoMessage("Le mot de passe et sa confirmation ne correspondent pas.", Color.RED);
        PasswordAPICall call = new PasswordAPICall(password, token);
        if(call.getCodeResponse() != 200){
            return new InfoMessage(call.getMessage(), Color.RED);
        }
        OnlineServerManager.getInstance().getCurrentUser().setPassword(password);
        return new InfoMessage("Le mot de passe a bien été modifié.", Color.GREEN);
    }

    public InfoMessage changeMail(String mail, String mail_confirming){
        if(mail.equals("")) return null;
        if(!mail.equals(mail_confirming))
            return new InfoMessage("Le mail et sa confirmation ne correspondent pas.", Color.RED);
        MailAPICall call = new MailAPICall(mail, token);
        if(call.getCodeResponse() != 200){
            return new InfoMessage(call.getMessage(), Color.RED);
        }
        OnlineServerManager.getInstance().getCurrentUser().setEmail(mail);
        return new InfoMessage("Le mail a bien été changé.", Color.GREEN);
    }

    public InfoMessage changeFirstname(String firstname){
        if(firstname.equals("")) return null;
        FirstnameAPICall call = new FirstnameAPICall(firstname, token);
        if(call.getCodeResponse() != 200){
            return new InfoMessage(call.getMessage(), Color.RED);
        }
        OnlineServerManager.getInstance().getCurrentUser().setFirstname(firstname);
        return new InfoMessage("Le prénom a bien été modifié.", Color.GREEN);
    }

    public InfoMessage changeLastname(String lastname){
        if(lastname.equals("")) return null;
        LastnameAPICall call = new LastnameAPICall(lastname, token);
        if(call.getCodeResponse() != 200){
            return new InfoMessage(call.getMessage(), Color.RED);
        }
        OnlineServerManager.getInstance().getCurrentUser().setLastname(lastname);
        return new InfoMessage("Le nom de famille a bien été modifié.", Color.GREEN);
    }

    public InfoMessage changePhone(String phone){
        if(phone.equals("")) return null;
        PhoneAPICall call = new PhoneAPICall(phone, token);
        if(call.getCodeResponse() != 200){
            return new InfoMessage(call.getMessage(), Color.RED);
        }
        OnlineServerManager.getInstance().getCurrentUser().setPhone_number(phone);
        return new InfoMessage("Le numéro de téléphone a bien été modifié.", Color.GREEN);
    }
}
