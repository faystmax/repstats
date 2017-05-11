package org.ams.repstats.userinterface;

import com.google.common.collect.Multimap;
import com.google.common.io.Files;
import com.selesse.gitwrapper.analyzer.BranchAnalyzer;
import com.selesse.gitwrapper.analyzer.BranchDetails;
import com.selesse.gitwrapper.myobjects.*;
import org.ams.repstats.graph.DiffChart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

/**
 * Created with IntelliJ IDEA
 * User: Maxim Amosov <faystmax@gmail.com>
 * Date: 27.12.2016
 * Time: 10:38
 */
public class GitUInterface implements UInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(GitUInterface.class);

    private String gitPath = null;
    private BranchAnalyzer branchAnalyzer = null;
    private BranchDetails branchDetails = null;

    @Override
    public String getRepName() {
        return branchAnalyzer.getGitRoot().getName();
    }

    @Override
    public String getBranchName() {
        return branchDetails.getBranch().getName();
    }

    @Override
    public int getNumberOfCommits() {
        return branchDetails.getCommits().size();
    }

    @Override
    public TableModel getAuthors() {
        DefaultTableModel tablemodel = new DefaultTableModel();
        tablemodel.addColumn("Name");
        tablemodel.addColumn("Commits Count");
        tablemodel.addColumn("Lines added");
        tablemodel.addColumn("Lines removed");
        tablemodel.addColumn("Net Contribution");
        tablemodel.addColumn("Email");

        for (Author author : branchDetails.getAuthorToCommitMap().keySet()) {
            String name = "";
            String email = "";
            int commitCount = 0;
            int linesAdded = 0;
            int linesRemoved = 0;
            int netContribution = 0;

            name = author.getName();
            email = author.getEmailAddress();
            commitCount = branchDetails.getAuthorToCommitMap().get(author).size();
            Collection<CommitDiff> commitDiffs = branchDetails.getAuthorToCommitDiffsMap().get(author);
            for (CommitDiff commitDiff : commitDiffs) {
                linesAdded = linesAdded + commitDiff.getLinesAdded();
                linesRemoved = linesRemoved + commitDiff.getLinesRemoved();
            }
            netContribution = linesAdded - linesRemoved;
            //Write to TableModel
            tablemodel.addRow(new Object[]{name, commitCount, linesAdded, linesRemoved, netContribution, email});
        }
        return tablemodel;
    }

    @Override
    public TableModel getAllFiles() {
        DefaultTableModel tablemodel = new DefaultTableModel();
        tablemodel.addColumn("Path");
        tablemodel.addColumn("IsBinary");
        tablemodel.addColumn("NumberOfLines");


        for (GitFile gitfile : branchDetails.getGitFileList()) {
            String path = gitfile.getPath();
            String isBinary = (gitfile.isBinary() ? "true" : "false");
            String numberOfLines = (gitfile.isBinary() ? "n/a" : String.valueOf(gitfile.getNumberOfLines()));

            //Write to TableModel
            tablemodel.addRow(new Object[]{path, isBinary, numberOfLines});
        }
        return tablemodel;
    }

    public DiffChart getChart() {
        if (branchDetails != null) {
            Multimap<Author, CommitDiff> authorToCommitDiffMap = branchDetails.getAuthorToCommitDiffsMap();
            ArrayList<CommitDiff> commitDiffList = null;
            for (Author author : authorToCommitDiffMap.keySet()) {
                commitDiffList = new ArrayList<CommitDiff>(authorToCommitDiffMap.get(author));
            }
            DiffChart DiffChart = new DiffChart(commitDiffList);
            return DiffChart;
        }
        return null;
    }

    @Override
    public boolean сhooseProjectDirectory(String gitPath) {
        this.gitPath = sanitizePath(gitPath);
        boolean isValidGitPath = GitRepositoryReader.isValidGitRoot(this.gitPath);

        if (!isValidGitPath) {
            LOGGER.error("Error: {} is an invalid Git root", this.gitPath);
            return false;
        }
        return true;
    }

    private static String sanitizePath(String gitPath) {
        String absolutePath = new File(gitPath).getAbsolutePath();
        absolutePath = Files.simplifyPath(absolutePath);
        return absolutePath;
    }

    @Override
    public boolean startProjectAnalyze() {
        String branchName = "master";

        try {
            File gitRoot = new File(gitPath);
            String repositoryName = gitRoot.getName();
            LOGGER.debug("Creating a BranchAnalyzer for {} on branch {}", gitRoot.getAbsolutePath(), branchName);
            branchAnalyzer = new BranchAnalyzer(gitRoot, branchName);
            branchDetails = branchAnalyzer.getBranchDetails();

        } catch (Exception e) {
            LOGGER.error("Error while starting analyzer project analyze");
            return false;
        }
        return true;
    }

    @Override
    public boolean startProjectAnalyze(LocalDate start, LocalDate end) {
        String branchName = "master";

        try {
            File gitRoot = new File(gitPath);
            String repositoryName = gitRoot.getName();
            LOGGER.debug("Creating a BranchAnalyzer for {} on branch {}", gitRoot.getAbsolutePath(), branchName);
            branchAnalyzer = new BranchAnalyzer(gitRoot, branchName);
            branchDetails = branchAnalyzer.getBranchDetails(start, end);

        } catch (Exception e) {
            LOGGER.error("Error while starting analyzer project analyze");
            return false;
        }
        return true;
    }

    /**
     * Закрываем репозиторий
     */
    @Override
    public void closeRepository() {
        try {
            LOGGER.debug("Closing git Repository...");
            if (branchDetails == null) {
                LOGGER.debug("Nothing to close...");

            } else {
                branchDetails.closeRepository();
                LOGGER.debug("Git Repository closed - {}.", new File(gitPath).getAbsolutePath());
            }
        } catch (Exception e) {
            LOGGER.error("Error while closing git Repository");
        }
    }


    @Override
    public Author getAuthorByName(String name) {
        for (Author author : branchDetails.getAuthorToCommitMap().keySet()) {
            if (author.getName().equals(name)) {
                return author;
            }
        }
        return null;
    }

    @Override
    public Author getAuthorByEmail(String email) {
        for (Author author : branchDetails.getAuthorToCommitMap().keySet()) {
            if (author.getEmailAddress().equals(email)) {
                return author;
            }
        }
        return null;
    }

    @Override
    public Collection<Commit> getLastCommits(Author author) {
        Collection<Commit> commits;
        for (Author authortmp : branchDetails.getAuthorToCommitMap().keySet()) {
            if (authortmp.equals(author)) {
                commits = branchDetails.getAuthorToCommitMap().get(author);
                return commits;
            }
        }
        return null;
    }

    @Override
    public String getRemoteName() {
        branchDetails.getRepository().getRepository().getRemoteNames();
        return "";
    }

    @Override
    public long getTotalNumberOfLines() {
        return branchDetails.getTotalNumberOfLines();
    }


    @Override
    public ArrayList<Branch> getListCurBranches() {
        return branchDetails.getListCurBranches();
    }

    @Override
    public ArrayList<Branch> getListMergedBranches() {
        return branchDetails.getListMergedBranches();
    }

    @Override
    public HashMap<Author, ArrayList<Integer>> getCommitsByMonths(ArrayList<Author> allAvtors) {
        return branchDetails.getCommitsByMonths(allAvtors);
    }

    @Override
    public HashMap<Author, ArrayList<Integer>> getCommitsByDaysInCurMonth(ArrayList<Author> allAvtors) {
        return branchDetails.getCommitsByDaysInCurMonth(allAvtors);
    }

    @Override
    public HashMap<Author, ArrayList<Integer>> getCommitsByWeek(ArrayList<Author> allAvtors) {
        return branchDetails.getCommitsByWeek(allAvtors);
    }

    @Override
    public Set<Author> getAllAuthors() {
        return branchDetails.getAuthorToCommitMap().keySet();
    }
}


