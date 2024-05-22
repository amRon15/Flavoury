package com.example.flavoury.ui.login;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Login extends AsyncTask<String, Void, String> {

    private LoginCallback callback;

    public Login(LoginCallback callback) {
        this.callback = callback;
    }

    @Override
    protected String doInBackground(String... params) {
        String username = params[0];
        String password = params[1];

        try {
            URL url = new URL("http://localhost/logsign/login.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            StringBuilder postData = new StringBuilder();
            postData.append(URLEncoder.encode("Username", "UTF-8"));
            postData.append("=");
            postData.append(URLEncoder.encode(username, "UTF-8"));
            postData.append("&");
            postData.append(URLEncoder.encode("Password", "UTF-8"));
            postData.append("=");
            postData.append(URLEncoder.encode(password, "UTF-8"));

            OutputStream outputStream = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            writer.write(postData.toString());
            writer.flush();
            writer.close();
            outputStream.close();

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String line;
                StringBuilder response = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                return response.toString();
            } else {
                return "Error: " + responseCode;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        callback.onLoginResult(result);
    }

    public interface LoginCallback {
        void onLoginResult(String result);
    }
}