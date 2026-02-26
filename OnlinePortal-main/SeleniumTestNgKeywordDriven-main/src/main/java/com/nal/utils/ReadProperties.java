package com.nal.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ReadProperties {

    public static String readPropertyValue(String propertyName) {
        Properties properties = new Properties();
        String propertyValue = "";

        try (FileInputStream input = new FileInputStream("src/test/resources/application.properties")) {
            properties.load(input);
// Reading properties
            propertyValue = properties.getProperty(propertyName);
            System.out.println(propertyName +": " + propertyValue);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return propertyValue;
    }
}