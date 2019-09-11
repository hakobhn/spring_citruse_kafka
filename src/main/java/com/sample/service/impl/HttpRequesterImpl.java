package com.sample.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.service.HttpRequester;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

@Service("httpRequester")
public class HttpRequesterImpl implements HttpRequester {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequesterImpl.class);

    private ObjectMapper jsonObjectMapper = new ObjectMapper();;

    @Override
    public void sendRequest(String urlStr, Object data) {

        try {

            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(10000);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json, */*");
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");

            byte[] outputBytes = data.toString().getBytes();
            OutputStream os = conn.getOutputStream();
            os.write(outputBytes);
            os.close();

            if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }
            conn.disconnect();

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            String inputLine;
            StringBuffer html = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                html.append(inputLine);
            }

            in.close();
            conn.disconnect();

            System.out.println("URL Content... \n" + html.toString());
            System.out.println("Done");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
