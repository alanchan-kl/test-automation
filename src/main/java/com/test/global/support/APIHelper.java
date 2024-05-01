package com.test.global.support;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
            if (!listOfFile.isDirectory()) {
                if (listOfFile.isFile()) {
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
            }
        }

        if (!isFileFound) {
            throw new FileNotFoundException("Error in reading file: "+ fileName);
        }

        return stringBuilder.toString();
    }

    public static void sendRespondData(String endPoint, String fileName) {
        try {
            URL url = new URL("http://localhost:9997" + endPoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            String requestBody = readJsonFromFile(fileName);
            DateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd'T'00:00:00");
            TimeZone tz = TimeZone.getTimeZone("Asia/Singapore");
            dtFormat.setTimeZone(tz);

            if (requestBody.contains("<today_date>")) {
                Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("Asia/Singapore"));
                requestBody = requestBody.replace("<today_date>", dtFormat.format(calendar.getTime()));
            }

            if (requestBody.contains("<today+1_date>")) {
                Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("Asia/Singapore"));
                calendar.add(Calendar.DATE, 1);
                requestBody = requestBody.replace("<today+1_date>", dtFormat.format(calendar.getTime()));
            }

            if (requestBody.contains("<today+30_date>")) {
                Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("Asia/Singapore"));
                calendar.add(Calendar.DATE, 30);
                requestBody = requestBody.replace("<today+30_date>", dtFormat.format(calendar.getTime()));
            }

            System.out.println("Request Body: " + requestBody);

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
            String getResult =  String.valueOf(responseBody);
            getResult = getResult.replaceAll("\\{","<");
            getResult = getResult.replaceAll("\\}",">");
            getResult = getResult.replaceAll(",","<COMMA>");
            getResult = getResult.replaceAll("\"","<DOUBLE_QUOTES>");
            webUI.putStateVariable("responseBody", getResult);
            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

