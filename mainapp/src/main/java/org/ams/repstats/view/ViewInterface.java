package org.ams.repstats.view;

import org.ams.repstats.userinterface.UInterface;

/**
 * Created with IntelliJ IDEA
 * User: Maxim Amosov <faystmax@gmail.com>
 * Date: 07.01.2017
 * Time: 12:55
 */
public interface ViewInterface {

    void chooseProjectAction();

    void showAllFiles();

    void showAvtors();

    void showMainInf();

    void start();

    void closeRepository();

    void setUInterface(UInterface uInterface);
}
