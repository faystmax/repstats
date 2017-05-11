package org.ams.repstats.utils;

import com.selesse.gitwrapper.myobjects.GitRepositoryReader;
import com.sun.istack.internal.NotNull;
import org.ams.repstats.MysqlConnector;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA
 * User: Maxim Amosov <faystmax@gmail.com>
 * Date: 11.05.2017
 * Time: 20:03
 */
public class RepositoryDownloader {

    private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryDownloader.class); ///< ссылка на логер

    public static final String pathToSave = "./savedRepositories/";
    private static Properties prop = null;


    public static void loadRepositoriesFromProperties(Properties prop) {
        RepositoryDownloader.prop = prop;
        refreshAllRepositoryUrlFromDatabase();
    }

    private static void refreshAllRepositoryUrlFromDatabase() {
        // Извлекаем данные из базы
        try {
            PreparedStatement preparedStatement = MysqlConnector.prepeareStmt(MysqlConnector.selectAllRepositoryUrl);
            ResultSet rs = MysqlConnector.executeQuery();

            while (rs.next()) {
                // Проверяем есть ли такой ключ
                String url = rs.getString(3);
                if (!prop.containsKey(url)) {
                    prop.put(url, pathToSave + rs.getString(2));
                }

            }
            MysqlConnector.closeStmt();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Загрузка репозитория
     *
     * @param githubRemoteUrl Remote git http url which ends with .git.
     * @param branchName      Name of the branch which should be downloaded
     * @return
     * @throws Exception
     */
    public static File downloadRepoContent(@NotNull String githubRemoteUrl, @NotNull String branchName) throws Exception {
        File destinationFile = new File(getPath(githubRemoteUrl));

        updateRep(destinationFile);

        //delete any existing file
        FileUtils.deleteDirectory(destinationFile);
        Git call = Git.cloneRepository().setURI(githubRemoteUrl)
                .setBranch(branchName)
                .setDirectory(destinationFile)
                .call();
        call.getRepository().close();
        if (destinationFile.listFiles().length > 0) {
            return destinationFile;
        } else {
            return null;
        }
    }

    public static String getPath(String url) {
        if (prop.containsKey(url)) {
            return (String) prop.get(url);
        }
        String name = "cloneRep";
        prop.put(url, pathToSave + name);
        return (String) prop.get(url);
    }

    private static void updateRep(File destinationFile) {
        Git git = null;
        try {
            Repository repository = GitRepositoryReader.loadRepository(destinationFile).getRepository();
            LOGGER.info("Updating existig repository in {}", destinationFile.getAbsolutePath());
            git = new Git(repository);

            PullCommand pullCmd = git.pull();
            PullResult call = pullCmd.call();
            if (call.isSuccessful()) {
                LOGGER.info("Repository up to date!");
            } else {
                LOGGER.info("Updating error!");
            }
        } catch (GitAPIException | IOException e) {
            e.printStackTrace();
            LOGGER.info("Updating error!");
        } finally {
            if (git != null) {
                git.getRepository().close();
            }

        }

    }

}
