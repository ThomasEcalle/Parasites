package com.parasites.models;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by spyro on 10/05/2017.
 */
public abstract class RestApiCall {

    private URL url;
    private HttpURLConnection conn;
    private OutputStream os;
    private String input;
    private String ouput;
    protected JSONObject json;
    private int codeResponse;

    public RestApiCall(String APIurl, String input, String method) {
        try {
            this.input = input;
            url = new URL(APIurl);
            initConnection(method);
            initOutput();
            json = getJson(new BufferedReader(new InputStreamReader(getInputStream())));
        } catch (IOException e){
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
    }

    private void initConnection(String method) throws IOException{
        conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod(method);
        conn.setRequestProperty("Content-Type", "application/json");
    }

    private void initOutput() throws IOException{
        os = conn.getOutputStream();
        os.write(input.getBytes());
        os.flush();
        codeResponse = conn.getResponseCode();
    }

    private InputStream getInputStream(){
        InputStream inputStream;
        try
        {
            inputStream = conn.getInputStream();
        }
        catch(IOException exception)
        {
            inputStream = conn.getErrorStream();
        }
        return inputStream;
    }

    private JSONObject getJson(BufferedReader br) throws IOException, JSONException{
        String line;
        String response = "";
        while ((line = br.readLine()) != null) {
            response += line;
        }
        return new JSONObject(response);
    }

    public String getMessage(){
        try {
            return json.get("message").toString();
        } catch (JSONException e) {
            try {
                return ((JSONArray) json.get("errors")).getString(0).split(": ")[1];
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        return null;
    }

    public int getCodeResponse(){
        return codeResponse;
    }
}