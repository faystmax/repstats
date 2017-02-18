package com.selesse.gitwrapper.myobjects;

import org.eclipse.jgit.lib.FileMode;

/**
 * \brief Класс для конверта типа файла в строку.
 * \author faystmax
 * \version 0.5
 * \date 18 февраля 2017 года
 */
public class FileModes {

    /**
     * Возвращает строковое  представления типа файла.
     *
     * @param fileMode из внешнего пакета
     * @return тип файла - string
     */
    public static String asString(FileMode fileMode) {
        if (fileMode.equals(FileMode.EXECUTABLE_FILE)) {
            return "executable file";
        } else if (fileMode.equals(FileMode.REGULAR_FILE)) {
            return "normal file";
        } else if (fileMode.equals(FileMode.TREE)) {
            return "directory";
        } else if (fileMode.equals(FileMode.SYMLINK)) {
            return "symlink";
        } else if (fileMode.equals(FileMode.GITLINK)) {
            return "analyzer link";
        } else {
            return fileMode.toString();
        }
    }
}
