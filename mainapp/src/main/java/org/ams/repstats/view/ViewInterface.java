package org.ams.repstats.view;

import org.ams.repstats.uinterface.UInterface;

/**
 * Created with IntelliJ IDEA
 * User: Maxim Amosov <faystmax@gmail.com>
 * Date: 07.01.2017
 * Time: 12:55
 */
public interface ViewInterface {

    /**
     * Выбор папки с проектом
     */
    void chooseProjectAction();

    /**
     * Отображение всех файлов
     */
    void showAllFiles();

    /**
     * Отображение всех авторов
     */
    void showAvtors();

    /**
     * Отображение основной информации
     */
    void showMainInf();

    /**
     * Начало анализа
     */
    void start();

    /**
     * Закрытие репозитория
     */
    void closeRepository();

    /**
     * Установка фасада
     *
     * @param uInterface - фасад git/svn ...
     */
    void setUInterface(UInterface uInterface);
}
