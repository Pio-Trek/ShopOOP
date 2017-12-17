package service;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public final class LabelStatusService {

    /**
     * Constructor to prevent from accidentally instantiating this class
     */
    private LabelStatusService() {
        throw new IllegalStateException("Must not instantiate an element of this class");
    }

    public static void getConfirmation(Label labelName, String message) {
        labelName.setTextFill(Color.GREEN);
        labelName.setText(message);
    }

    public static void getError(Label labelName, String message) {
        labelName.setTextFill(Color.RED);
        labelName.setText(message);
    }

}
