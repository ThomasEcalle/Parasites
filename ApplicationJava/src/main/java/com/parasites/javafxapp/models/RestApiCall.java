package com.parasites.javafxapp.models;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.json.JSONException;
import org.json.JSONObject;
import sun.misc.IOUtils;

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

    public RestApiCall(String APIurl, String input) {
        try {
            this.input = input;
            url = new URL(APIurl);
            initConnection();
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

    private void initConnection() throws IOException{
        conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
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

    public String gotMessage(){
        try {
            return json.get("message").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getCodeResponse(){
        return codeResponse;
    }
}