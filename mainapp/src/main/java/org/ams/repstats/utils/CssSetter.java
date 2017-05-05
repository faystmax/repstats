package org.ams.repstats.utils;

import javafx.scene.Scene;

/**
 * Created with IntelliJ IDEA
 * User: Maxim Amosov <faystmax@gmail.com>
 * Date: 06.05.2017
 * Time: 0:34
 */
public class CssSetter {

    // Текущий стиль
    public static String currentStyleSheet = "default.css";

    // Все стили
    public static String defaultStyleSheet = "default.css";
    public static String blackStyleSheet = "black.css";
    public static String blueStyleSheet = "blue.css";
    public static String greenStyleSheet = "green.css";


    public static void addStyleSheet(Scene scene) {
        scene.getStylesheets().add("styles/" + CssSetter.currentStyleSheet);
    }

    public static void setDefaultStyleSheet() {
        currentStyleSheet = defaultStyleSheet;
    }

    public static void setBlackStyleSheet() {
        currentStyleSheet = blackStyleSheet;
    }

    public static void setBlueStyleSheet() {
        currentStyleSheet = blueStyleSheet;
    }

    public static void setGreenStyleSheet() {
        currentStyleSheet = greenStyleSheet;
    }

}
