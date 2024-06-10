package com.test.global.support;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class APIHelper {
    private static WebUI webUI = null;

    public APIHelper(WebUI webUI) {
        APIHelper.webUI = webUI;
    }

    private static String readJsonFromFile(String fileName) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

        File folder = new File("request");
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles == null)
            throw new NullPointerException("Unable to file directory: " + "request");

        boolean isFileFound = false;
        for (File listOfFile : listOfFiles) {
            if (listOfFile.getName().equalsIgnoreCase(fileName)) {
                isFileFound = true;
                System.out.println("Reading json file: " + listOfFile.getAbsolutePath());
                try (BufferedReader bufferedReader = new BufferedReader(new FileReader(listOfFile.getAbsolutePath()))) {
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                }
            }
        }

        if (!isFileFound) {
            throw new FileNotFoundException("Error in reading file: " + fileName);
        }

        return stringBuilder.toString();
    }

    public static void sendGetRespondData(String endPoint) {
        try {
            URL url = new URL("http://localhost:9997" + endPoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Get response code
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);
            webUI.putStateVariable("responseCode", String.valueOf(responseCode));

            // Read response body
            BufferedReader reader;
            if (responseCode == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }

            StringBuilder responseBody = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                responseBody.append(line);
            }
            reader.close();

            System.out.println("Response Body: " + responseBody);
            String getResult = String.valueOf(responseBody);
            getResult = getResult.replaceAll("\\{", "<");
            getResult = getResult.replaceAll("\\}", ">");
            getResult = getResult.replaceAll(",", "<COMMA>");
            getResult = getResult.replaceAll("\"", "<DOUBLE_QUOTES>");
            webUI.putStateVariable("responseBody", getResult);
            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final ThreadLocal<DateFormat> dateFormatThreadLocal = ThreadLocal.withInitial(() -> {
        DateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd'T'00:00:00");
        dtFormat.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));
        return dtFormat;
    });

    private static String replaceDatePlaceholder(String requestBody, String placeholder, int daysToAdd) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Singapore"));
        calendar.add(Calendar.DATE, daysToAdd);
        return requestBody.replace(placeholder, dateFormatThreadLocal.get().format(calendar.getTime()));
    }

    public static void sendPostRespondData(String endPoint, String fileName) {
        try {
            URL url = new URL("http://localhost:9997" + endPoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            String requestBody = readJsonFromFile(fileName);

            if (requestBody.contains("<today_date>")) {
                requestBody = replaceDatePlaceholder(requestBody, "<today_date>", 0);
            }

            if (requestBody.contains("<today+1_date>")) {
                requestBody = replaceDatePlaceholder(requestBody, "<today+1_date>", 1);
            }

            if (requestBody.contains("<today+30_date>")) {
                requestBody = replaceDatePlaceholder(requestBody, "<today+30_date>", 30);
            }

            System.out.println("Request Body: " + requestBody);

            synchronized (APIHelper.class) {
                // Write JSON data to the connection's output stream
                try (OutputStream outputStream = connection.getOutputStream()) {
                    byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
                    outputStream.write(input, 0, input.length);
                }

                // Get response code
                int responseCode = connection.getResponseCode();
                System.out.println("Response Code: " + responseCode);
                webUI.putStateVariable("responseCode", String.valueOf(responseCode));

                // Read response body
                BufferedReader reader;
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                } else {
                    reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                }

                StringBuilder responseBody = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    responseBody.append(line);
                }
                reader.close();

                System.out.println("Response Body: " + responseBody);
                String getResult = String.valueOf(responseBody);
                getResult = getResult.replaceAll("\\{", "<");
                getResult = getResult.replaceAll("\\}", ">");
                getResult = getResult.replaceAll(",", "<COMMA>");
                getResult = getResult.replaceAll("\"", "<DOUBLE_QUOTES>");
                webUI.putStateVariable("responseBody", getResult);
            }

            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

