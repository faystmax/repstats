package org.ams.repstats.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.ams.gitapiwrapper.GitApi;
import org.ams.repstats.utils.properties.CssSetter;
import org.ams.repstats.utils.properties.EStartWindow;
import org.ams.repstats.utils.properties.StartWindowSetter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA
 * User: Maxim Amosov <faystmax@gmail.com>
 * Date: 21.04.2017
 * Time: 20:02
 */
public class SettingsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SettingsController.class); ///< ссылка на логер

    //region <<<  элементы UI
    @FXML
    private ImageView imCheck;
    @FXML
    private TextField tbPassword;
    @FXML
    private TextField tbUsername;
    @FXML
    private RadioButton whiteButton;
    @FXML
    private RadioButton blueButton;
    @FXML
    private RadioButton greenButton;
    @FXML
    private RadioButton blackButton;
    @FXML
    private RadioButton pinkButton;
    @FXML
    private RadioButton repButton;
    @FXML
    private RadioButton projectButton;
    @FXML
    private RadioButton developerButton;
    @FXML
    private RadioButton teamButton;
    @FXML
    private Button btExit;
    //endregion

    @FXML
    public void initialize() {

        // Группа выбора стиля
        ToggleGroup groupStyle = new ToggleGroup();

        // кидаем radiobutton-ы в эту группу
        whiteButton.setToggleGroup(groupStyle);
        greenButton.setToggleGroup(groupStyle);
        blackButton.setToggleGroup(groupStyle);
        blueButton.setToggleGroup(groupStyle);
        pinkButton.setToggleGroup(groupStyle);

        // из Property подтягиваем css
        if (CssSetter.getCurrentStyleSheet().equals(CssSetter.defaultStyleSheet)) {
            whiteButton.setSelected(true);
        } else if (CssSetter.getCurrentStyleSheet().equals(CssSetter.greenStyleSheet)) {
            greenButton.setSelected(true);
        } else if (CssSetter.getCurrentStyleSheet().equals(CssSetter.blackStyleSheet)) {
            blackButton.setSelected(true);
        } else if (CssSetter.getCurrentStyleSheet().equals(CssSetter.blueStyleSheet)) {
            blueButton.setSelected(true);
        } else if (CssSetter.getCurrentStyleSheet().equals(CssSetter.pinkStyleSheet)) {
            pinkButton.setSelected(true);
        }

        // ставим лстенеры на смену стиля
        groupStyle.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle old_toggle, Toggle new_toggle) {
                if (new_toggle == whiteButton) {
                    CssSetter.setDefaultStyleSheet();
                } else if (new_toggle == greenButton) {
                    CssSetter.setGreenStyleSheet();
                } else if (new_toggle == blackButton) {
                    CssSetter.setBlackStyleSheet();
                } else if (new_toggle == blueButton) {
                    CssSetter.setBlueStyleSheet();
                } else if (new_toggle == pinkButton) {
                    CssSetter.setPinkStyleSheet();
                }
            }
        });

        // Группа начального окна
        ToggleGroup groupStartWindow = new ToggleGroup();

        // кидаем radiobutton-ы в эту группу
        repButton.setToggleGroup(groupStartWindow);
        projectButton.setToggleGroup(groupStartWindow);
        developerButton.setToggleGroup(groupStartWindow);
        teamButton.setToggleGroup(groupStartWindow);


        // из Property подтягиваем startWindow
        if (StartWindowSetter.getCurrentStartWindow().equals(EStartWindow.REP)) {
            repButton.setSelected(true);
        } else if (StartWindowSetter.getCurrentStartWindow().equals(EStartWindow.PROJECT)) {
            projectButton.setSelected(true);
        } else if (StartWindowSetter.getCurrentStartWindow().equals(EStartWindow.DEVELOPER)) {
            developerButton.setSelected(true);
        } else if (StartWindowSetter.getCurrentStartWindow().equals(EStartWindow.TEAM)) {
            teamButton.setSelected(true);
        }

        // ставим листенеры на смену начального окна
        groupStartWindow.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle old_toggle, Toggle new_toggle) {
                if (new_toggle == repButton) {
                    StartWindowSetter.setRepStartWindow();
                } else if (new_toggle == projectButton) {
                    StartWindowSetter.setProjectStartWindow();
                } else if (new_toggle == developerButton) {
                    StartWindowSetter.setDeveloperStartWindow();
                } else if (new_toggle == teamButton) {
                    StartWindowSetter.setTeamtStartWindow();
                }
            }
        });

        // подгрузка git username и password
        tbUsername.setText(GitApi.getUsername());
        tbPassword.setText(GitApi.getPassword());
    }

    /**
     * Закрытие окна настроек
     *
     * @param event
     */
    public void exitButtonAction(ActionEvent event) {
        Stage stage = (Stage) btExit.getScene().getWindow();
        stage.close();
    }

    /**
     * Проверка и запоминание логина и пароля git
     * @param event
     */
    public void CheckAndRememberUsernameAndPass(ActionEvent event) {
        if (GitApi.checkUsernameAndPass(tbUsername.getText(), tbPassword.getText())) {
            GitApi.setUsernameAndPasswoed(tbUsername.getText(), tbPassword.getText());
            Image successIcon = new Image(getClass().getClassLoader().getResourceAsStream("icons/success.png"));
            imCheck.setImage(successIcon);
        } else {
            Image errorIcon = new Image(getClass().getClassLoader().getResourceAsStream("icons/error.png"));
            imCheck.setImage(errorIcon);
        }
    }
}
