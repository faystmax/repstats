package org.ams.repstats.view;

import org.ams.repstats.uinterface.UInterface;

/**
 * Created with IntelliJ IDEA
 * User: Maxim Amosov <faystmax@gmail.com>
 * Date: 07.01.2017
 * Time: 12:56
 */
public abstract class ViewInterfaceAbstract implements ViewInterface {

    private boolean isStart;        ///< флаг начала анализа
    private UInterface uInterface;  ///< фасад

    /**
     * Констроуктор по умолчанию
     */
    public ViewInterfaceAbstract() {
    }


    /**
     * Конструктор с заданным фасадом
     *
     * @param uInterface - фасад
     */
    public ViewInterfaceAbstract(UInterface uInterface) {
        isStart = false;
        this.uInterface = uInterface;
    }

    /**
     * Установка фасада
     *
     * @param uInterface - фасад git/svn ...
     */
    public void setUInterface(UInterface uInterface) {
        this.uInterface = uInterface;

    }

    /**
     * Закрытие репозитопия
     */
    @Override
    public void closeRepository() {
        this.getuInterface().closeRepository();
    }

    /**
     * Возвращает флаг начала анализа
     *
     * @return флаг начала анализа
     */
    public boolean isStart() {
        return isStart;
    }

    /**
     * Возвращает фасад
     *
     * @return фасад
     */
    public UInterface getuInterface() {
        return uInterface;
    }

    /**
     * Установка флага начала анализа
     *
     * @param start флаг начала анализа
     */
    public void setStart(boolean start) {
        isStart = start;
    }

    /**
     * Заглушка
     * Переопределите этот метод в наследнике для выбора репозитория
     */
    @Override
    public void chooseProjectAction() {
    }

    /**
     * Заглушка
     * Переопределите этот метод в наследнике для показа авторов репозитория
     */
    @Override
    public void showAvtors() {
    }

    /**
     * Заглушка
     * Переопределите этот метод в наследнике для показа всех авторов репозитория
     */
    @Override
    public void showAllFiles() {
    }

}
