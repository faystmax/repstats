package org.ams.repstats.userinterface;

import com.oneandone.sales.svnstats.FileStats;
import com.oneandone.sales.svnstats.connectors.Repository;
import com.oneandone.sales.svnstats.connectors.svnkit.SvnRepository;
import com.oneandone.sales.svnstats.model.Revision;
import com.selesse.gitwrapper.myobjects.Author;
import com.selesse.gitwrapper.myobjects.Commit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.table.TableModel;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.List;

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
    public void closeRepository() {
        // TODO
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

    @Override
    public String getRemoteName() {
        return null;
    }
}
