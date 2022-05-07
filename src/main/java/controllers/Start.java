package controllers;

import javafx.application.Application;
import javafx.stage.Stage;
import service.ControllerService;
import service.StageService;

public class Start extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Starts the app and opens Primary Stage.
     *
     * @param primaryStage Created by JavaFX system.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        StageService stage = new StageService();
        stage.loadStage(ControllerService.MAIN_MENU);
    }
}
