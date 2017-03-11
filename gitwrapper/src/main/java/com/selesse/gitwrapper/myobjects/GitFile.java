package com.selesse.gitwrapper.myobjects;

import com.google.common.base.Splitter;
import org.eclipse.jgit.diff.RawText;
import org.eclipse.jgit.lib.FileMode;

import java.io.UnsupportedEncodingException;
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
    private byte[] byteContents;        ///< Содержание файла
    private List<String> contents;      ///< Список строк фала

    /**
     * Инициализирует git файл.
     *
     * @param path     Полный путь до файла
     * @param fileMode Тип файла
     * @param bytes    Содержание файла
     */
    public GitFile(String path, FileMode fileMode, byte[] bytes) {
        this.path = path;
        this.fileMode = fileMode;
        this.isBinary = RawText.isBinary(bytes);
        this.byteContents = bytes;
    }

    /**
     * Получаем количество строк в файле.
     * По сути просто вызывает getContents() и берёт его размер.
     * В итоге получаем количество строк
     *
     * @return число строк
     * @see #getContents()
     */
    public int getNumberOfLines() {
        return getContents().size();
    }

    /**
     * Возвращает массив строк файла.
     * Если уже был построен ранее - возвращаем его.Иначе формируем его
     * на основе содержимого файла(byteContents), разбивая его на строки.
     *
     * @return список строк файла
     */
    public List<String> getContents() {
        if (contents == null) {
            try {
                // UTF-8 is the only encoding, ever, right?
                String fileContents = new String(byteContents, "UTF-8");
                contents = Splitter.onPattern("\r?\n").splitToList(fileContents);
            } catch (UnsupportedEncodingException e) {
                throw new IllegalArgumentException("Error reading file: " + path);
            }
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

}
