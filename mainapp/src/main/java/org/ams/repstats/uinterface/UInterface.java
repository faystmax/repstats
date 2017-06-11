package org.ams.repstats.uinterface;

import com.selesse.gitwrapper.myobjects.Author;
import com.selesse.gitwrapper.myobjects.Branch;
import com.selesse.gitwrapper.myobjects.Commit;

import javax.swing.table.TableModel;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

/**
 * Интерфейс Фасада
 * Created with IntelliJ IDEA
 * User: Maxim Amosov <faystmax@gmail.com>
 * Date: 27.12.2016
 * Time: 19:59
 */
public interface UInterface {
    /**
     * Проверка на наличие репозитория
     *
     * @param Path
     * @return
     */
    boolean сhooseProjectDirectory(String Path);

    /**
     * Начинаем анализ репозитория
     *
     * @return
     */
    boolean startProjectAnalyze();

    /**
     * Начинаем анализ репозитория с промежутком времени
     *
     * @param start
     * @param end
     * @return
     */
    boolean startProjectAnalyze(LocalDate start, LocalDate end);

    /**
     * Закрываем репозиторий
     */
    void closeRepository();

    /**
     * @return имя репозитория
     */
    String getRepName();

    /**
     * @return имя ветки
     */
    String getBranchName();

    /**
     * @return кол-во коммитов
     */
    int getNumberOfCommits();

    /**
     * @return таблицу с авторами
     */
    TableModel getAuthors();

    /**
     * @return таблицу с файлами репозитория
     */
    TableModel getAllFiles();

    /**
     * Получаем автора по имени
     *
     * @param name
     * @return
     */
    Author getAuthorByName(String name);

    /**
     * Получаем автора по email
     *
     * @param email
     * @return
     */
    Author getAuthorByEmail(String email);

    /**
     * Полуаем последний коммит автора
     *
     * @param author
     * @return
     */
    Collection<Commit> getLastCommits(Author author);

    /**
     * @return общее кол-во строк
     */
    long getTotalNumberOfLines();

    /**
     * @return список текущей ветки
     */
    ArrayList<Branch> getListCurBranches();

    /**
     * @return список слитых веток
     */
    ArrayList<Branch> getListMergedBranches();

    /**
     * Получаем коммиты по месяцам
     *
     * @param allAvtors
     * @return
     */
    HashMap<Author, ArrayList<Integer>> getCommitsByMonths(ArrayList<Author> allAvtors);

    /**
     * Получаем коммиты по дням в месяце
     *
     * @param allAvtors
     * @return
     */
    HashMap<Author, ArrayList<Integer>> getCommitsByDaysInCurMonth(ArrayList<Author> allAvtors);

    /**
     * Получаем коммиты за неделю
     *
     * @param allAvtors
     * @return
     */
    HashMap<Author, ArrayList<Integer>> getCommitsByWeek(ArrayList<Author> allAvtors);

    /**
     * @return всех аавторов
     */
    Set<Author> getAllAuthors();

    /**
     * Все коммита с датами
     *
     * @param allAvtors
     * @return
     */
    HashMap<Author, HashMap<LocalDate, Integer>> getCommitsByCustomDate(ArrayList<Author> allAvtors);

    /**
     * @return url
     */
    String getUrl();

    /**
     * Возвращает коммиты по времени
     *
     * @param allAvtors
     * @return
     */
    HashMap<Author, ArrayList<Integer>> getCommitsByTime(ArrayList<Author> allAvtors);

    /**
     * @param selectedAuthor
     * @return кол-во исправленных багов автором
     */
    int getBugFixesCount(Author selectedAuthor);

    /**
     * @return кол-во исправленных багов
     */
    int getBugFixesCount();

    /**
     * @return кол-во добавленных строк за промежуток времени
     */
    long getTotalLinesAddedWithDate();

    /**
     * @return кол-во удалённых строкза промежуток времени
     */
    long getTotalLinesRemovedWithDate();

    /**
     * @return всего кол-во добавленных строк
     */
    long getTotalLinesAddedAll();

    /**
     * @return всего удалённых добавленных строк
     */
    long getTotalLinesRemovedAll();
}
