package org.ams.repstats.utils;

import java.io.*;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA
 * User: Maxim Amosov <faystmax@gmail.com>
 * Date: 06.05.2017
 * Time: 14:25
 */
public class MyProperties {

    public static Properties prop = new Properties();

    private static String filePropertyName = "config.properties";

    private static OutputStream output = null;
    private static InputStream input = null;

    /**
     * Загружаем свойства из конфига
     */
    public static void loadProperties() {
        try {
            input = new FileInputStream("config.properties");

            if (input == null) {
                System.out.println("Unable to find property file: " + filePropertyName);
                return;
            }

            // load a properties file
            prop.load(input);

            // load css property
            CssSetter.setCurrentStyleSheet(prop.getProperty("style"));

            // load startwindow property
            StartWindowSetter.setCurrentStartWindow(prop.getProperty("startWindow"));

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Пишем свойства в конфиг
     */
    public static void writeProperties() {
        try {

            output = new FileOutputStream(filePropertyName);

            // set database properties value
            prop.setProperty("database", "jdbc:mysql://localhost:3306/gitstats?characterEncoding=UTF-8");
            prop.setProperty("dbuser", "root");
            prop.setProperty("dbpassword", "");

            // set user setting  properties value
            prop.setProperty("style", CssSetter.getCurrentStyleSheet());
            prop.setProperty("startWindow", StartWindowSetter.getCurrentStartWindowAsString());

            // save properties to project root folder
            prop.store(output, null);

        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public static String givePropValue(String key) {
        return prop.getProperty(key);
    }

}
