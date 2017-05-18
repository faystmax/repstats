package org.ams.repstats.userinterface;

import com.selesse.gitwrapper.myobjects.Author;
import com.selesse.gitwrapper.myobjects.Branch;
import com.selesse.gitwrapper.myobjects.Commit;
import org.ams.repstats.graph.DiffChart;

import javax.swing.table.TableModel;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

/**
 * Created with IntelliJ IDEA
 * User: Maxim Amosov <faystmax@gmail.com>
 * Date: 27.12.2016
 * Time: 19:59
 */
public interface UInterface {

    boolean сhooseProjectDirectory(String Path);

    boolean startProjectAnalyze();

    boolean startProjectAnalyze(LocalDate start, LocalDate end);

    void closeRepository();

    DiffChart getChart();

    String getRepName();

    String getBranchName();

    int getNumberOfCommits();

    TableModel getAuthors();


    TableModel getAllFiles();

    Author getAuthorByName(String name);

    Author getAuthorByEmail(String email);

    Collection<Commit> getLastCommits(Author author);

    String getRemoteName();

    long getTotalNumberOfLines();


    ArrayList<Branch> getListCurBranches();

    ArrayList<Branch> getListMergedBranches();

    HashMap<Author, ArrayList<Integer>> getCommitsByMonths(ArrayList<Author> allAvtors);

    HashMap<Author, ArrayList<Integer>> getCommitsByDaysInCurMonth(ArrayList<Author> allAvtors);

    HashMap<Author, ArrayList<Integer>> getCommitsByWeek(ArrayList<Author> allAvtors);

    Set<Author> getAllAuthors();

    HashMap<Author, HashMap<LocalDate, Integer>> getCommitsByCustomDate(ArrayList<Author> allAvtors);

    String getUrl();

    HashMap<Author, ArrayList<Integer>> getCommitsByTime(ArrayList<Author> allAvtors);

    int getBugFixesCount(Author selectedAuthor);

    int getBugFixesCount();

    long getTotalLinesAddedWithDate();

    long getTotalLinesRemovedWithDate();

    long getTotalLinesAddedAll();

    long getTotalLinesRemovedAll();
}
