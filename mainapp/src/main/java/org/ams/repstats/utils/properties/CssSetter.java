package org.ams.repstats.utils.properties;

import com.sun.javafx.css.StyleManager;
import javafx.application.Application;

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
    public static String pinkStyleSheet = "pink.css";

    /**
     * Обновляем стиль
     */
    public static void refreshStyleSheet() {
        Application.setUserAgentStylesheet(null);
        StyleManager.getInstance().addUserAgentStylesheet("styles/" + CssSetter.currentStyleSheet);
    }

    public static void setDefaultStyleSheet() {
        currentStyleSheet = defaultStyleSheet;
        refreshStyleSheet();
    }

    public static void setBlackStyleSheet() {
        currentStyleSheet = blackStyleSheet;
        refreshStyleSheet();

    }

    public static void setBlueStyleSheet() {
        currentStyleSheet = blueStyleSheet;
        refreshStyleSheet();
    }

    public static void setGreenStyleSheet() {
        currentStyleSheet = greenStyleSheet;
        refreshStyleSheet();
    }

    public static void setPinkStyleSheet() {
        currentStyleSheet = pinkStyleSheet;
        refreshStyleSheet();
    }

    /**
     * @return текущий стиль
     */
    public static String getCurrentStyleSheet() {
        return currentStyleSheet;
    }

    /**
     * Установка стиля
     *
     * @param currentStyleSheet стиль
     */
    public static void setCurrentStyleSheet(String currentStyleSheet) {
        CssSetter.currentStyleSheet = currentStyleSheet;
        refreshStyleSheet();
    }
}
