package service;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.StageStyle;

import java.util.Optional;

/**
 * Popup the JavaFX Alert Dialog.
 */
public class AlertService {

    /**
     * Constructor to prevent from accidentally instantiating this class
     */
    private AlertService() {
        throw new IllegalStateException("Must not instantiate an element of this class");
    }

    public static Optional<ButtonType> showDialog(Alert.AlertType alertType, String title, String headerText, String dialogMessage) {
        Alert alert = new Alert(alertType);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(dialogMessage);
        return alert.showAndWait();
    }
}
