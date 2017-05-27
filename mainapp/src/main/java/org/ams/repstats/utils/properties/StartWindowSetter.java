package org.ams.repstats.utils.properties;

import java.util.Objects;

/**
 * Created with IntelliJ IDEA
 * User: Maxim Amosov <faystmax@gmail.com>
 * Date: 06.05.2017
 * Time: 16:13
 */
public class StartWindowSetter {
    // Текущий стиль
    public static EStartWindow currentStartWindow = EStartWindow.REP;

    public static EStartWindow getCurrentStartWindow() {
        return currentStartWindow;
    }

    public static String getCurrentStartWindowAsString() {
        return getEStartWindowString(currentStartWindow);
    }

    public static void setCurrentStartWindow(EStartWindow currentStartWindow) {
        StartWindowSetter.currentStartWindow = currentStartWindow;
    }

    public static void setCurrentStartWindow(String currentStartWindow) {
        if (Objects.equals(currentStartWindow, getEStartWindowString(EStartWindow.REP))) {
            StartWindowSetter.currentStartWindow = EStartWindow.REP;
        } else if (Objects.equals(currentStartWindow, getEStartWindowString(EStartWindow.DEVELOPER))) {
            StartWindowSetter.currentStartWindow = EStartWindow.DEVELOPER;
        } else if (Objects.equals(currentStartWindow, getEStartWindowString(EStartWindow.PROJECT))) {
            StartWindowSetter.currentStartWindow = EStartWindow.PROJECT;
        } else if (Objects.equals(currentStartWindow, getEStartWindowString(EStartWindow.TEAM))) {
            StartWindowSetter.currentStartWindow = EStartWindow.TEAM;
        }
    }

    public static String getEStartWindowString(EStartWindow startWindow) {
        if (startWindow == EStartWindow.REP) {
            return "repository";
        } else if (startWindow == EStartWindow.DEVELOPER) {
            return "developer";
        } else if (startWindow == EStartWindow.PROJECT) {
            return "project";
        } else if (startWindow == EStartWindow.TEAM) {
            return "team";
        }
        return null;
    }

    public static void setRepStartWindow() {
        currentStartWindow = EStartWindow.REP;
    }

    public static void setProjectStartWindow() {
        currentStartWindow = EStartWindow.PROJECT;
    }

    public static void setTeamtStartWindow() {
        currentStartWindow = EStartWindow.TEAM;
    }

    public static void setDeveloperStartWindow() {
        currentStartWindow = EStartWindow.DEVELOPER;
    }


}
