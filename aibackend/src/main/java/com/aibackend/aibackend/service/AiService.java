package com.aibackend.aibackend.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service
public class AiService {

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String SCHEMA_INFO_KEY = "schema_structure";

    @Value("${openai.api.key}")
    private String openaiApiKey;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public String generateSQLQuery(String userInput) throws IOException {
        // קריאה ל-Redis לקבלת מבנה הנתונים
        List<Map<String, String>> schemaInfo = (List<Map<String, String>>) redisTemplate.opsForValue().get(SCHEMA_INFO_KEY);

        if (schemaInfo == null) {
            throw new IllegalStateException("Schema information not found in Redis.");
        }

        // בניית הפנייה ל-ChatGPT
        String prompt = buildPrompt(schemaInfo, userInput);
        return sendToChatGPT(prompt);
    }

    private String buildPrompt(List<Map<String, String>> schemaInfo, String userInput) {
        // יצירת מחרוזת JSON של מבנה הנתונים
        JSONArray schemaArray = new JSONArray();
        for (Map<String, String> column : schemaInfo) {
            JSONObject columnObj = new JSONObject(column);
            schemaArray.put(columnObj);
        }

        // בניית ההנחיה ל-ChatGPT
        return "I have a question to ask you. Based on the following database schema, generate a PostgreSQL SQL query that answers the question. "
                + "The schema information is as follows: " + schemaArray.toString() + " "
                + "The question is: \"" + userInput + "\" "
                + "Return only the SQL query.";
    }

    private String sendToChatGPT(String prompt) throws IOException {
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

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonBody.toString().getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
            String responseLine;
            while ((responseLine = in.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }
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

    public String executeSQLQuery(String sqlQuery) throws IOException, SQLException {
        // קבלת פרטי החיבור מ-Redis
        Map<String, String> dbParams = (Map<String, String>) redisTemplate.opsForValue().get("user_params");
        if (dbParams == null) {
            throw new IllegalStateException("Database connection parameters not found in Redis.");
        }

        String dbUrl = dbParams.get("url");
        String dbUsername = dbParams.get("username");
        String dbPassword = dbParams.get("password");

        try (Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            // עיבוד התוצאה
            StringBuilder result = new StringBuilder();
            int columnCount = resultSet.getMetaData().getColumnCount();
            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    result.append(resultSet.getString(i)).append("\t");
                }
                result.append("\n");
            }
            return result.toString();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("SQL error: " + e.getMessage());
        }
    }


    public void saveCallHistory(String question, String response) {
        String sql = "INSERT INTO call_history (question, response) VALUES (?, ?)";
        jdbcTemplate.update(sql, question, response);
}
}