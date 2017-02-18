package org.ams.repstats.userinterface;

import com.selesse.gitwrapper.myobjects.Author;
import com.selesse.gitwrapper.myobjects.Commit;

import javax.swing.table.TableModel;
import java.awt.image.BufferedImage;
import java.util.Collection;

/**
 * Created with IntelliJ IDEA
 * User: Максим
 * Date: 27.12.2016
 * Time: 19:59
 */
public interface UInterface {
    boolean сhooseProjectDirectory(String Path);

    boolean startProjectAnalyze();

    BufferedImage getChart();

    String getRepName();

    String getBranchName();

    int getNumberOfCommits();

    TableModel getAuthors();

    TableModel getAllFiles();

    Author getAuthorByName(String name);

    Collection<Commit> getLastCommits(Author author);
}
