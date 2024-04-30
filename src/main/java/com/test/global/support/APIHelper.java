package com.test.global.support;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class APIHelper {
    private static WebUI webUI = null;

    public APIHelper(WebUI webUI) {
        this.webUI = webUI;
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
            throw new FileNotFoundException(String.format("Error in reading file: ") + fileName);
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

            // Write JSON data to the connection's output stream
            try (OutputStream outputStream = connection.getOutputStream()) {
                byte[] input = requestBody.getBytes("utf-8");
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
            webUI.putStateVariable("responseBody", String.valueOf(responseBody));
            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

