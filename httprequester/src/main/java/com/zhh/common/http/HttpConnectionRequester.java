package com.zhh.common.http;

import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.zhh.common.http.HttpRequester.Request.Method;
import com.zhh.common.util.IoUtils;

class HttpConnectionRequester extends HttpRequester {

    @Override
    public RequestResult execute(Request request) {

        URL url = null;
        BufferedReader reader = null;
        BufferedWriter writer = null;
        RequestResult result = new RequestResult();

        try {
            // create the HttpURLConnection
            url = new URL(request.getRequestUrl());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // add headers
            Map<String, String> headers = request.getHeaders();
            addHeaders(connection, headers);

            // just want to do an HTTP GET here
            Method method = request.getMethod();
            connection.setRequestMethod(method.name());
            connection.setDoInput(true);

            if (method == Method.POST || method == Method.PUT) {
                // uncomment this if you want to write output to this url
                connection.setDoOutput(true);
                String bodyString = getBody(request);
                if (bodyString != null && !bodyString.isEmpty()) {
                    OutputStream os = connection.getOutputStream();
                    writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(bodyString);
                    writer.flush();
                }
            }

            // give it 15 seconds to respond
            connection.setReadTimeout(15*1000);
            connection.setConnectTimeout(15*1000);
            connection.connect();

            result.code = connection.getResponseCode();
            result.message = connection.getResponseMessage();

            // read the output from the server
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null) {
              stringBuilder.append(line + "\n");
            }

            String originalData = stringBuilder.toString();
            DataConverter dataConverter = request.getDataConverter();
            if (dataConverter != null) {
                dataConverter.convertData(originalData, result);
            } else {
                result.data = originalData;
            }

        } catch (Exception e) {
            result.exception = e;
        } finally {
            IoUtils.close(reader);
            IoUtils.close(writer);
        }

        return result;
    }

    private static String getBody(Request request) throws UnsupportedEncodingException {

        Map<String, String> forms = request.getForms();
        if (!forms.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            Set<Entry<String, String>> entries = forms.entrySet();

            for (Entry<String, String> entry : entries) {
                String key = entry.getKey();
                String value = entry.getValue();
                builder.append(URLEncoder.encode(key, "UTF-8"))
                       .append("=")
                       .append(URLEncoder.encode(value, "UTF-8"))
                       .append("&");
            }
            String result = builder.toString();
            return result.substring(0, result.length() - 1);
        }

        String jsonBody = request.getRequestBody();
        if (!TextUtils.isEmpty(jsonBody)) {
            return jsonBody;
        }

        return null;
    }

    private static void addHeaders(HttpURLConnection connection, Map<String, String> headers) {

        if (!headers.isEmpty() && connection != null) {
            Set<Entry<String, String>> entries = headers.entrySet();
            for (Entry<String, String> entry : entries) {
                String key = entry.getKey();
                String value = entry.getValue();
                connection.addRequestProperty(key, value);
            }
        }
    }
}
