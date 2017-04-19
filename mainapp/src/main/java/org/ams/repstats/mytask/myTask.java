package org.ams.repstats.mytask;

import javafx.concurrent.Task;
import javafx.scene.control.TextField;
import org.ams.repstats.controllers.CloneRepViewController;
import org.ams.repstats.controllers.FXViewInterfaceController;
import org.eclipse.jgit.api.Git;

import java.io.File;

/**
 * Created with IntelliJ IDEA
 * User: Maxim Amosov <faystmax@gmail.com>
 * Date: 10.04.2017
 * Time: 0:06
 */
public class myTask extends Task {
    private CloneRepViewController controller;
    private FXViewInterfaceController fxViewInterfaceController;
    private TextField tbURL;

    public myTask(CloneRepViewController controller, FXViewInterfaceController fxViewInterfaceController, TextField tbURL) {
        this.controller = controller;
        this.fxViewInterfaceController = fxViewInterfaceController;
        this.tbURL = tbURL;
    }


    @Override
    public Void call() {
        try {
            Git git = Git.cloneRepository()
                    .setURI(tbURL.getText())
                    .setDirectory(new File("./cloneRep"))
                    .call();
            git.getRepository().close();
            fxViewInterfaceController.setNewRepDirectory(new File("./cloneRep"));
            updateProgress(1, 1);
        } catch (Exception e) {
            e.printStackTrace();
            this.controller.showAlert("Ошибка", "Введённый вами репозиторий не существует," +
                    " либо у вас отсутствует подключение к интернету");
        } finally {
            this.done();
        }
        return null;
    }
}
