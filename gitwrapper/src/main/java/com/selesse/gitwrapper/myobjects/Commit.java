package com.selesse.gitwrapper.myobjects;

import com.google.common.base.Ascii;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * \brief Коммит.
 * \author faystmax
 * \version 0.5
 * \date 18 февраля 2017 года
 * <p>
 * Представляет собой сущность коммита репозитория analyzer.
 * Хранит в себе всю основную информацию о коммите, вкючая список изменённых файлов.
 */
public class Commit {

    private String sha;                     ///< sha коммита
    private String commitMessage;           ///< Сообщение коммита.Загаловок+сообщение.
    private ZonedDateTime commitDateTime;   ///< Время коммита
    private Author author;                  ///< Ссылка на автора коммита
    /**
     * < Ссылка на автора пославшего коммит.
     * По сути это автор откуда был послан коммит. Например если зайти на github.com и
     * сделать изминение в репозитории через web интерфейс, то в committer запишется "GitHub <noreply@github.com>".
     * Но поскольку чаще всего коммиты делаются с устройств самих авторов, эти 2 значения ("author" и "committer")
     * почти всегда совадают.
     */
    private Author committer;
    private List<GitFile> filesChanged;     ///< Список изминённых файлов
    private int linesAdded;                 ///< Строк добавленных в коммите
    private int linesRemoved;               ///< Строк удалённых в коммите

    /**
     * Инициализирует коммит.
     *
     * @param sha            sha коммита
     * @param commitMessage  Сообщение коммита.Загаловок+сообщение.
     * @param commitDateTime Время коммита
     * @param author         Ссылка на автора коммита
     * @param committer      Ссылка на автора пославшего коммит
     * @param filesChanged   Список изминённых файлов
     */
    public Commit(String sha,
                  String commitMessage,
                  ZonedDateTime commitDateTime,
                  Author author,
                  Author committer,
                  List<GitFile> filesChanged) {
        this.sha = sha;
        this.commitMessage = commitMessage;
        this.commitDateTime = commitDateTime;
        this.author = author;
        this.committer = committer;
        this.filesChanged = filesChanged;
    }

    /**
     * Возвращает sha коммита.
     *
     * @return sha.
     */
    public String getSHA() {
        return sha;
    }

    /**
     * Возвращает сообщение коммита.
     *
     * @return Заколовок+сообщение.
     */
    public String getCommitMessage() {
        return commitMessage;
    }

    /**
     * Возвращет время коммита.
     *
     * @return время коммита.
     */
    public ZonedDateTime getCommitDateTime() {
        return commitDateTime;
    }

    /**
     * Возвращет автра репозитория.
     *
     * @return автора репозитория.
     */
    public Author getAuthor() {
        return author;
    }

    /**
     * Возвращает автора откуда был сделан коммит
     *
     * @return автора откуда был сделан коммит
     */
    public Author getCommitter() {
        return committer;
    }

    /**
     * Возвращает список изменённых файлов.
     *
     * @return список изменённых файлов
     */
    public List<GitFile> getFilesChanged() {
        return filesChanged;
    }

    /**
     * Возвращает строковое представление коммита.
     * Возращает: Первые 7 символов sha : " 50 символов сообщения коммита" by имя автора.
     *
     * @return - строковое представление коммита
     */
    @Override
    public String toString() {
        return String.format("%s: \"%s\" by %s",
                getSHA().substring(0, 7), Ascii.truncate(getCommitMessage(), 50, "..."), author.getName());
    }

    /**
     * Устанавливает кол-во добавленных строк
     *
     * @param linesAdded кол-во добавленных строк
     */
    public void setLinesAdded(int linesAdded) {
        this.linesAdded = linesAdded;
    }

    /**
     * Устанавливает кол-во удалённых строк
     *
     * @param linesRemoved кол-во удалённых строк
     */
    public void setLinesRemoved(int linesRemoved) {
        this.linesRemoved = linesRemoved;
    }

    /**
     * Возвращает кол-во добавленных строк
     *
     * @return кол-во добавленных строк
     */
    public int getLinesAdded() {
        return linesAdded;
    }

    /**
     * Возвращает кол-во удалённых строк
     *
     * @return кол-во удалённых строк
     */
    public int getLinesRemoved() {
        return linesRemoved;
    }
}
