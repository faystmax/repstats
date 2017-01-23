package org.ams.repstats.userinterface;

import com.selesse.gitwrapper.Author;
import com.selesse.gitwrapper.Commit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.table.TableModel;
import java.awt.image.BufferedImage;
import java.util.Collection;

/**
 * Created with IntelliJ IDEA
 * User: Максим
 * Date: 27.12.2016
 * Time: 19:57
 */
public class SvnUInterface implements UInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(SvnUInterface.class);

    @Override
    public boolean сhooseProjectDirectory(String Path) {
        return false;
    }

    @Override
    public boolean startProjectAnalyze() {
        return false;
    }

    @Override
    public BufferedImage getChart() {
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
    public Collection<Commit> getLastCommits(Author author) {
        return null;
    }
}
