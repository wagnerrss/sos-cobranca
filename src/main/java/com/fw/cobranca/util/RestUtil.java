package com.fw.cobranca.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class RestUtil implements Util {
    public Map postPush(String documento, String titulo, String mensagem) {
        try {
            String url = "https://fcm.googleapis.com/fcm/send";

            String msgJson = "{" +
                    "    \"to\": \"/topics/" + documento + "\", " +
                    "        \"notification\": { " +
                    "    \"title\": \"" + titulo + "\", " +
                    "            \"body\": \"" + mensagem + "\" " +
                    "}}";

            HttpPost requestPost = new HttpPost(url);
            requestPost.addHeader("Content-Type", "application/json");
            requestPost.addHeader("Authorization", "Key=AAAAPbSbcHg:APA91bGYi_y8LYBeIofjJGvTn-sJfMWH7Juu2TlBiYblKfMYVg92v-BBwO7dIGuOnABttuOxxQrwo8Zf76RhPfKo1vzX9-jQB-6QIhHP4NF-b03hX0kM2FhMLqXML82pyB-fnSfk03WJ");

            StringEntity sEntity = new StringEntity(msgJson, "UTF-8");
            requestPost.setEntity(sEntity);
            CloseableHttpResponse response;

            CloseableHttpClient httpClient = HttpClients.createDefault();
            response = httpClient.execute(requestPost);

            Map status = new HashMap();
            status.put("status", response.getStatusLine().getStatusCode());
            return status;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new LinkedHashMap();
    }

    public Map postItau(Map value, boolean isToken, String params, String body, String token) {

        Map<String, Object> result = new HashMap<>();
        HttpURLConnection connection = null;

        try {
            System.out.println("INICIO POST ITAU");
            URL url = new URL(isToken ? "https://sts.itau.com.br/api/oauth/token" : "https://secure.api.itau/pix_recebimentos/v2/" + params);

            connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("Content-Type", isToken ? "application/x-www-form-urlencoded" : "application/json");

            if (!isToken) {
                connection.addRequestProperty("Authorization", "Bearer " + token);
            }

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            // Add certificate
            File p12 = new File("src/main/resources/certnovo.pfx");
            String p12password = toStr(value.get("cert_pass"));

            InputStream keyInput = new FileInputStream(p12);

            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(keyInput, p12password.toCharArray());
            keyInput.close();

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            keyManagerFactory.init(keyStore, p12password.toCharArray());

            SSLContext context = SSLContext.getInstance("TLS");
            context.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());

            SSLSocketFactory socketFactory = context.getSocketFactory();
            if (connection instanceof HttpsURLConnection)
                ((HttpsURLConnection) connection).setSSLSocketFactory(socketFactory);

            try (OutputStream outputStream = connection.getOutputStream()) {
                byte[] input = body.getBytes("utf-8");
                outputStream.write(input, 0, input.length);
            }

            BufferedReader bufferedReader =
                    new BufferedReader(new InputStreamReader(connection.getInputStream()));

            StringBuilder response = new StringBuilder();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }

            bufferedReader.close();

            result = new ObjectMapper().readValue(response.toString(), HashMap.class);

            System.out.println(response.toString());
            System.out.println("FIM POST ITAU");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null)
                connection.disconnect();
        }

        return result;
    }

    public Map getItau(Map value, String params,String token) {

        Map<String, Object> result = new HashMap<>();
        HttpURLConnection connection = null;

        try {
            System.out.println("INICIO GET ITAU");
            URL url = new URL("https://secure.api.itau/pix_recebimentos/v2/" + params);

            connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("Content-Type", "application/json");
            connection.addRequestProperty("Authorization", "Bearer " + token);

            connection.setRequestMethod("GET");
            connection.setDoOutput(true);

            File p12 = new File("src/main/resources/certnovo.pfx");
            String p12password = toStr(value.get("cert_pass"));

            InputStream keyInput = new FileInputStream(p12);

            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(keyInput, p12password.toCharArray());
            keyInput.close();

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            keyManagerFactory.init(keyStore, p12password.toCharArray());

            SSLContext context = SSLContext.getInstance("TLS");
            context.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());

            SSLSocketFactory socketFactory = context.getSocketFactory();
            if (connection instanceof HttpsURLConnection)
                ((HttpsURLConnection) connection).setSSLSocketFactory(socketFactory);

            BufferedReader bufferedReader =
                    new BufferedReader(new InputStreamReader(connection.getInputStream()));

            StringBuilder response = new StringBuilder();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }

            bufferedReader.close();

            result = new ObjectMapper().readValue(response.toString(), HashMap.class);

            System.out.println(response.toString());
            System.out.println("FIM GET ITAU");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (connection != null)
                connection.disconnect();
        }

        return result;
    }
}
