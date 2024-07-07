package com.aibackend.aibackend.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class AiService {

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    @Value("${openai.api.key}")
    private String openaiApiKey;

    public String getAPIResponse(String apiUrl) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        conn.disconnect();
        return content.toString();
    }

    public String sendToChatGPT(String jsonResponse, String userInput) throws IOException {
        URL url = new URL(API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + openaiApiKey);
        conn.setDoOutput(true);

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("model", "gpt-3.5-turbo");
        JSONArray messages = new JSONArray();
        JSONObject message = new JSONObject();
        message.put("role", "user");
        message.put("content", "קיבלתי את הנתונים הבאים מה-API: " + jsonResponse + "\n" + userInput);
        messages.put(message);
        jsonBody.put("messages", messages);

        OutputStream os = conn.getOutputStream();
        byte[] input = jsonBody.toString().getBytes("utf-8");
        os.write(input, 0, input.length);

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
        StringBuilder response = new StringBuilder();
        String responseLine;
        while ((responseLine = in.readLine()) != null) {
            response.append(responseLine.trim());
        }
        in.close();
        conn.disconnect();

        JSONObject responseObject = new JSONObject(response.toString());
        return responseObject.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
    }
}
