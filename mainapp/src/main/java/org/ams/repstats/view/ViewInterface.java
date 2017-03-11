package org.ams.repstats.view;

import org.ams.repstats.userinterface.UInterface;

/**
 * Created with IntelliJ IDEA
 * User: Maxim Amosov <faystmax@gmail.com>
 * Date: 07.01.2017
 * Time: 12:55
 */
public interface ViewInterface {

    void ChooseProjectAction();

    void ShowAllFiles();

    void ShowAvtors();

    void showMainInf();

    void start();

    void setUInterface(UInterface uInterface);
}
