package com.parasites.models;

import com.google.gson.Gson;
import com.parasites.network.bo.User;
import org.json.JSONException;

/**
 * Created by spyro on 10/05/2017.
 */
public class ConnectionAPICall extends RestApiCall
{

    public ConnectionAPICall(String pseudo, String password)
    {
        super("http://localhost:3000/users/connect",
                "{\"pseudo\":\"" +
                        pseudo +
                        "\",\"password\":\"" +
                        password + "\"}", "POST");
    }

    public String getToken()
    {
        try
        {
            return json.get("token").toString();

        } catch (JSONException e)
        {
            return null;
        }
    }

    public User getUser()
    {
        final Gson gson = new Gson();
        try
        {
            return gson.fromJson(json.getJSONObject("user").toString(), User.class);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
