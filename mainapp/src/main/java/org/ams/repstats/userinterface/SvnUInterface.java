package org.ams.repstats.userinterface;

import com.oneandone.sales.svnstats.FileStats;
import com.oneandone.sales.svnstats.connectors.Repository;
import com.oneandone.sales.svnstats.connectors.svnkit.SvnRepository;
import com.oneandone.sales.svnstats.model.Revision;
import com.selesse.gitwrapper.myobjects.Author;
import com.selesse.gitwrapper.myobjects.Branch;
import com.selesse.gitwrapper.myobjects.Commit;
import org.ams.repstats.graph.DiffChart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.table.TableModel;
import java.time.LocalDate;
import java.util.*;

/**
 * Created with IntelliJ IDEA
 * User: Maxim Amosov <faystmax@gmail.com>
 * Date: 27.12.2016
 * Time: 19:57
 */
public class SvnUInterface implements UInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(SvnUInterface.class);

    private String svnPath = null;
    private Repository repository;
    private List<Revision> revisions;
    private FileStats fileStats;

    @Override
    public boolean сhooseProjectDirectory(String svnPath) {
        try {
            this.svnPath = svnPath;
            Repository repository = createRepository(this.svnPath);

        } catch (Exception ex) {
            LOGGER.error("Error: {} is an invalid svn root", this.svnPath);
            return false;
        }
        return true;
    }

    private Repository createRepository(String svnPath) {
        return new SvnRepository(svnPath);
    }

    @Override
    public boolean startProjectAnalyze() {
        try {
            this.revisions = repository.fetchRevisions(0, -1); //default
            this.fileStats = new FileStats(revisions);

        } catch (Exception e) {
            LOGGER.error("Error while starting svn project analyze");
            return false;
        }
        return true;
    }

    @Override
    public boolean startProjectAnalyze(LocalDate start, LocalDate end) {
        return false;
    }

    @Override
    public void closeRepository() {
        // TODO
    }

    @Override
    public DiffChart getChart() {
        return null;
    }

    @Override
    public String getRepName() {
        return null;
    }

    @Override
    public String getBranchName() {
        return null;
    }

    @Override
    public int getNumberOfCommits() {
        return 0;
    }

    @Override
    public TableModel getAuthors() {
        return null;
    }

    @Override
    public TableModel getAllFiles() {
        return null;
    }

    @Override
    public Author getAuthorByName(String name) {
        return null;
    }

    @Override
    public Author getAuthorByEmail(String email) {
        return null;
    }

    @Override
    public Collection<Commit> getLastCommits(Author author) {
        return null;
    }

    @Override
    public String getRemoteName() {
        return null;
    }

    @Override
    public long getTotalNumberOfLines() {
        return 0;
    }


    @Override
    public ArrayList<Branch> getListCurBranches() {
        return null;
    }

    @Override
    public ArrayList<Branch> getListMergedBranches() {
        return null;
    }

    @Override
    public HashMap<Author, ArrayList<Integer>> getCommitsByMonths(ArrayList<Author> allAvtors) {
        return null;
    }

    @Override
    public HashMap<Author, ArrayList<Integer>> getCommitsByDaysInCurMonth(ArrayList<Author> allAvtors) {
        return null;
    }

    @Override
    public HashMap<Author, ArrayList<Integer>> getCommitsByWeek(ArrayList<Author> allAvtors) {
        return null;
    }

    @Override
    public Set<Author> getAllAuthors() {
        return null;
    }

    @Override
    public HashMap<Author, HashMap<LocalDate, Integer>> getCommitsByCustomDate(ArrayList<Author> allAvtors) {
        return null;
    }

    @Override
    public String getUrl() {
        return null;
    }

    @Override
    public HashMap<Author, ArrayList<Integer>> getCommitsByTime(ArrayList<Author> allAvtors) {
        return null;
    }

    @Override
    public int getBugFixesCount(Author selectedAuthor) {
        return 0;
    }

    @Override
    public long getTotalLinesAdded() {
        return 0;
    }

    @Override
    public long getTotalLinesRemoved() {
        return 0;
    }
}
