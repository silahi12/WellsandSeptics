package com.nal.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class WriteProperties {


    public static void writeProperty(String propertyName, String propertyValue) {
        Properties properties = new Properties();

// Setting key-value pair
        properties.setProperty(propertyName, propertyValue);

        try (FileOutputStream output = new FileOutputStream("src/test/resources/application.properties")) {
            properties.store(output, "Configuration Settings");
            System.out.println("Properties file saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}