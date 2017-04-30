package org.ams.repstats.controllers.mytask;

import javafx.application.Platform;
import javafx.concurrent.Task;
import org.ams.repstats.controllers.LoadingController;
import org.eclipse.jgit.api.Git;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created with IntelliJ IDEA
 * User: Maxim Amosov <faystmax@gmail.com>
 * Date: 01.05.2017
 * Time: 0:01
 */
public class RepTask extends Task {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyDownloadRepTask.class); ///< ссылка на логер


    private LoadingController loadingController;
    String url;


    public RepTask(LoadingController loadingController, String url) {
        this.loadingController = loadingController;
        this.url = url;
    }


    @Override
    public Void call() {
        try {
            Git git = Git.cloneRepository()
                    .setURI(url)
                    .setDirectory(new File("./cloneRep"))
                    .call();
            //TODO
            git.getRepository().close();
            Platform.runLater(() -> {

                updateProgress(1, 1);
            });
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            //оповещаем родительское окно об ошибке
            Platform.runLater(() -> {
                loadingController.downloadError(e);
                updateProgress(0, 1);
            });
        } finally {
            this.done();
        }
        return null;
    }
}
