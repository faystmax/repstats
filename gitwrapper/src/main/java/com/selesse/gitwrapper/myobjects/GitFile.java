package com.selesse.gitwrapper.myobjects;

import com.google.common.base.Splitter;
import org.eclipse.jgit.diff.RawText;
import org.eclipse.jgit.lib.FileMode;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * \brief Git Файл.
 * \author faystmax
 * \version 0.5
 * \date 18 февраля 2017 года
 * <p>
 * Хранит необходимую информацию о файле репозитория.
 */
public class GitFile {

    private String path;                ///< Полный путь до файла
    private FileMode fileMode;          ///< Тип файла
    private boolean isBinary;           ///< Если бинариник - истина, иначе - ложь
    private int numberOfLines;          ///< Кол-во строк кода
    private String changeTypeName;      ///< Тип изминения

    /**
     * Инициализирует git файл.
     *
     * @param path                  Полный путь до файла
     * @param fileMode              Тип файла
     * @param bytes                 Содержание файла
     * @param changeTypeName        Тип изминения
     */
    public GitFile(String path, FileMode fileMode, byte[] bytes, String changeTypeName) {
        this.path = path;
        this.fileMode = fileMode;
        this.isBinary = RawText.isBinary(bytes);
        this.changeTypeName = changeTypeName;

        this.numberOfLines = getContents(bytes).size();
    }

    /**
     * Получаем количество строк в файле.
     * По сути просто вызывает getContents() и берёт его размер.
     * В итоге получаем количество строк
     *
     * @return число строк
     */
    public int getNumberOfLines() {
        return numberOfLines;
    }

    /**
     * Возвращает массив строк файла.
     * Если уже был построен ранее - возвращаем его.Иначе формируем его
     * на основе содержимого файла(byteContents), разбивая его на строки.
     *
     * @return список строк файла
     */
    private List<String> getContents(byte[] byteContents) {
        List<String> contents = null;
        try {
            // Пропуск длинных файлов (скорее всего dll либо внешний jar)
            if (byteContents.length / 1048576 > 50) {
                contents = new ArrayList<>();
            }
            String fileContents = new String(byteContents, "UTF-8");
            contents = Splitter.onPattern("\r?\n").splitToList(fileContents);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("Error reading file: " + path);
        }
        return contents;
    }

    /**
     * Является ли файл бинарником
     *
     * @return истинна - бинарник, иначе - нет.
     */
    public boolean isBinary() {
        return isBinary;
    }

    /**
     * Возвращает тип файла.
     *
     * @return тип файла (из внешнего пакета)
     */
    public FileMode getFileMode() {
        return fileMode;
    }

    /**
     * Возвращает полный путь до файла.
     *
     * @return путь до файла.
     */
    public String getPath() {
        return path;
    }

    /**
     * Возвращает тип изменения
     *
     * @return тип изминения
     */
    public String getChangeTypeName() {
        return changeTypeName;
    }
}
