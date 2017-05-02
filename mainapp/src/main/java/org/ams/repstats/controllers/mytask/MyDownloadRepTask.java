package org.ams.repstats.controllers.mytask;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.TextField;
import org.ams.repstats.controllers.CloneRepViewController;
import org.ams.repstats.controllers.stats.StatsRepositoryController;
import org.eclipse.jgit.api.Git;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created with IntelliJ IDEA
 * User: Maxim Amosov <faystmax@gmail.com>
 * Date: 10.04.2017
 * Time: 0:06
 */
public class MyDownloadRepTask extends Task {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyDownloadRepTask.class); ///< ссылка на логер

    private CloneRepViewController controller;
    private StatsRepositoryController fxViewInterfaceController;
    private TextField tbURL;

    public MyDownloadRepTask(CloneRepViewController controller, StatsRepositoryController fxViewInterfaceController, TextField tbURL) {
        this.controller = controller;
        this.fxViewInterfaceController = fxViewInterfaceController;
        this.tbURL = tbURL;
    }


    @Override
    public Void call() {
        try {
            updateProgress(-1, -1);
            Git git = Git.cloneRepository()
                    .setURI(tbURL.getText())
                    .setDirectory(new File("./cloneRep"))
                    .call();
            git.getRepository().close();
            Platform.runLater(() -> {
                fxViewInterfaceController.setNewRepDirectory(new File("./cloneRep"));
                updateProgress(1, 1);
            });
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            //оповещаем родительское окно об ошибке
            Platform.runLater(() -> {
                controller.downloadError();
                updateProgress(0, 1);
            });
        } finally {
            this.done();
            Platform.runLater(() -> controller.btExit.requestFocus());
        }
        return null;
    }
}
