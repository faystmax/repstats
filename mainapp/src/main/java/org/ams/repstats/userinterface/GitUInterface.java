package org.ams.repstats.userinterface;

import com.google.common.collect.Multimap;
import com.google.common.io.Files;
import com.selesse.gitwrapper.analyzer.BranchAnalyzer;
import com.selesse.gitwrapper.analyzer.BranchDetails;
import com.selesse.gitwrapper.myobjects.*;
import org.ams.repstats.graph.DiffChart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created with IntelliJ IDEA
 * User: Максим
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


        for (Author author : branchDetails.getAuthorToCommitMap().keySet()) {
            String name = "";
            int commitCount = 0;
            int linesAdded = 0;
            int linesRemoved = 0;
            int netContribution = 0;

            name = author.getName();
            commitCount = branchDetails.getAuthorToCommitMap().get(author).size();
            Collection<CommitDiff> commitDiffs = branchDetails.getAuthorToCommitDiffsMap().get(author);
            for (CommitDiff commitDiff : commitDiffs) {
                linesAdded = linesAdded + commitDiff.getLinesAdded();
                linesRemoved = linesRemoved + commitDiff.getLinesRemoved();
            }
            netContribution = linesAdded - linesRemoved;
            //Write to TableModel
            tablemodel.addRow(new Object[]{name, commitCount, linesAdded, linesRemoved, netContribution});
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

    public BufferedImage getChart() {
        if (branchDetails != null) {
            try {
                Multimap<Author, CommitDiff> authorToCommitDiffMap = branchDetails.getAuthorToCommitDiffsMap();
                ArrayList<CommitDiff> commitDiffList = null;
                for (Author author : authorToCommitDiffMap.keySet()) {
                    commitDiffList = new ArrayList<CommitDiff>(authorToCommitDiffMap.get(author));
                }
                DiffChart chart = new DiffChart(commitDiffList);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                chart.writeChart(stream);
                ByteArrayInputStream in = new ByteArrayInputStream(stream.toByteArray());
                BufferedImage img = null;
                img = ImageIO.read(in);
                return img;
            } catch (IOException e) {
                LOGGER.error("Error while creating Chart");
            }
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
        }
        return true;
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


}


