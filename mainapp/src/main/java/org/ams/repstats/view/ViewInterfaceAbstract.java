package org.ams.repstats.view;

import org.ams.repstats.userinterface.UInterface;

/**
 * Created with IntelliJ IDEA
 * User: Maxim Amosov <faystmax@gmail.com>
 * Date: 07.01.2017
 * Time: 12:56
 */
public abstract class ViewInterfaceAbstract implements ViewInterface {

    private boolean isStart;
    private UInterface uInterface;

    public ViewInterfaceAbstract() {

    }

    public ViewInterfaceAbstract(boolean isStart, UInterface uInterface) {
        this.isStart = isStart;
        this.uInterface = uInterface;
    }

    public ViewInterfaceAbstract(UInterface uInterface) {
        isStart = false;
        this.uInterface = uInterface;
    }

    public void setUInterface(UInterface uInterface) {
        this.uInterface = uInterface;

    }

    public boolean isStart() {
        return isStart;
    }

    public UInterface getuInterface() {
        return uInterface;
    }

    public void setStart(boolean start) {
        isStart = start;
    }
}
