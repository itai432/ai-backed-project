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

    public String generateSQLQuery(String userInput) throws IOException {
        String prompt = "Translate the following request into a PostgreSQL query: \"" + userInput + "\". " +
                "Ensure the query is valid and does not contain any explanation. Use the tables: app_users, app_books, and app_users_books. " +
                "The app_users table has columns: user_id, username, password, email, full_name, date_of_birth, purchases. " +
                "The app_books table has columns: book_id, title, author, published_year, genre, purchases. " +
                "The app_users_books table has columns: id, user_id, book_id, purchase_date.";

        return sendToChatGPT(prompt);
    }

    public String sendToChatGPT(String prompt) throws IOException {
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
        message.put("content", prompt);
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
        JSONArray choicesArray = responseObject.getJSONArray("choices");
        if (choicesArray.length() > 0) {
            JSONObject firstChoice = choicesArray.getJSONObject(0);
            if (firstChoice.has("message") && firstChoice.getJSONObject("message").has("content")) {
                return firstChoice.getJSONObject("message").getString("content").trim();
            }
        }
        return "No content found in the response.";
    }
}
