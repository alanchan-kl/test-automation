package com.test.global.support;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class Accounts {
    private Map<String, Integer> userIndexMap = new HashMap<String, Integer>();
    private Map<String, ArrayList<String>> userMap;

    private static Map<String, Accounts> urlAccsMapping = new HashMap<String, Accounts>();

    static {
        Map<String, String> urlFileMapping = new HashMap<String, String>();
        urlFileMapping.put("FACEBOOK", "TestAccounts/facebook_users.txt");

        ClassLoader classLoader = Accounts.class.getClassLoader();

        for (Map.Entry<String, String> entry : urlFileMapping.entrySet()) {
            Map<String, ArrayList<String>> userMap = new HashMap<String, ArrayList<String>>();
            File file = new File(classLoader.getResource(entry.getValue()).getFile());

            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line = "";
                while ((line = br.readLine()) != null) {
                    String[] account = line.split(",");
                    String env = account[0];
                    String username = account[1];
                    ArrayList<String> users = userMap.get(env);
                    if (users == null) {
                        users = new ArrayList<String>();
                        userMap.put(env, users);
                    }
                    users.add(username);
                }

                urlAccsMapping.put(entry.getKey(), new Accounts(userMap));
            } catch (Exception e) {
                System.out.println("Error loading accounts.");
                e.printStackTrace();
                urlAccsMapping = null;
            }
        }
    }

    public static Accounts getInstance(String env) throws Exception {
        if (urlAccsMapping == null) {
            throw new Exception("Accounts failed to initialise");
        }

        return urlAccsMapping.get(getType(env));
    }

    public Accounts(Map<String, ArrayList<String>> userMap) {
        this.userMap = userMap;
    }

    public synchronized String getNextUsername(String usertype) {
        Integer index = userIndexMap.get(usertype);

        if (index == null) {
            index = 0;
            userIndexMap.put(usertype, index);
        }

        String password = null;

        try {
            password = userMap.get(usertype).get(index);
        } catch (Exception IndexOutOfBoundsException) {
            System.out.println("Not enough test account.");
        }

        index++;
        userIndexMap.put(usertype, index);
        return password;
    }

    public static String getType(String env) throws Exception {
        String type;
        switch (env.trim()) {
            case "Facebook":
                type = "FACEBOOK";
                break;
            default:
                type = "EMPTY";
                break;
        }
        return type;
    }
}
