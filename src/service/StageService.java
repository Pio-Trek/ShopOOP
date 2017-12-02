package service;

import controllers.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import models.Clothing;
import models.Customer;
import models.Footwear;
import models.Staff;

import java.io.IOException;

import static service.ControllerService.*;


/**
 * Create new stages helper class.
 */
public class StageService {

    private final String ICON_URL = "../views/images/store.png";
    private final String TITLE = "Shop - Piotr Przechodzki";
    private final int WIDTH = 770;
    private final int HEIGHT = 645;

    /**
     * Create a first stage.
     *
     * @param controller For the Shop app the first stage location of FXML file is {@see main_menu.fxml}
     */
    public void loadStage(String controller) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        Stage primaryStage = new javafx.stage.Stage();
        loader.setLocation(Start.class.getResource(controller));
        Parent root = loader.load();

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.setTitle(TITLE);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream(ICON_URL)));
        primaryStage.setResizable(false);
        primaryStage.show();

    }

    /**
     * Create a new stage using helper interface with list/path of FXML files) {@link ControllerService}
     *
     * @param actionEvent Event respond for hiding old stage (window)
     * @param controller  This is a new location of FXML file.
     */
    public void loadStage(ActionEvent actionEvent, String controller) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        Stage primaryStage = new javafx.stage.Stage();
        loader.setLocation(Start.class.getResource(controller));
        Parent root = loader.load();

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.setTitle(TITLE);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream(ICON_URL)));
        primaryStage.setResizable(false);
        primaryStage.show();
        // Hides previous window
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();

    }

    /**
     * Create a new stage for {@link Customer} object using helper interface
     * with list/path of FXML files) {@link ControllerService}
     *
     * @param actionEvent Event respond for hiding old stage (window)
     * @param customer    Passes {@link Customer} object to a new stage.
     * @param controller  This is a new location of FXML file.
     */
    public void loadStage(ActionEvent actionEvent, Customer customer, String controller) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        Stage primaryStage = new javafx.stage.Stage();
        loader.setLocation(Start.class.getResource(controller));
        Parent root = loader.load();

        switch (controller) {
            case CUSTOMER_HOME: {
                CustomerHomeController c = loader.getController();

                // Passes the {@link Customer} object to the {@link CustomerHomeController}
                c.initialize(customer);
                break;
            }
        }


        Scene scene = new Scene(root, WIDTH, HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.setTitle(TITLE);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream(ICON_URL)));
        primaryStage.setResizable(false);
        primaryStage.show();
        // Hides previous window
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
    }

    /**
     * Create a new stage for {@link Staff} object using helper interface
     * with list/path of FXML files) {@link ControllerService}
     *
     * @param actionEvent Event respond for hiding old stage (window)
     * @param staff       Passes {@link Staff} object to a new stage.
     * @param controller  This is a new location of FXML file.
     */
    public void loadStage(ActionEvent actionEvent, Staff staff, String controller) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        Stage primaryStage = new javafx.stage.Stage();
        loader.setLocation(Start.class.getResource(controller));
        Parent root = loader.load();

        switch (controller) {
            case STAFF_HOME: {
                StaffHomeController c = loader.getController();
                c.initialize(staff);
                break;
            }
            case MODIFY_PRODUCTS: {
                ModifyProductController c = loader.getController();
                c.initialize(staff);
                break;
            }
            case EDIT_PRODUCTS: {
                EditProductsController c = loader.getController();
                c.initialize(staff);
                break;
            }
        }

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.setTitle(TITLE);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream(ICON_URL)));
        primaryStage.setResizable(false);
        primaryStage.show();
        // Hides previous window
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
    }

    /**
     * Create a new stage {@link EditProductsController} for {@link Staff}
     * and {@link Clothing} or {@link Footwear}
     *
     * @param actionEvent Event respond for hiding old stage (window)
     * @param staff       Passes {@link Staff} object to a new stage.
     * @param clothing    Passes {@link Clothing} object to a new stage
     * @param footwear    Passes {@link Footwear} object to a new stage
     */
    public void loadStage(ActionEvent actionEvent, Staff staff, Clothing clothing, Footwear footwear) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        Stage primaryStage = new javafx.stage.Stage();
        loader.setLocation(Start.class.getResource(EDIT_PRODUCTS));
        Parent root = loader.load();

        EditProductsController c = loader.getController();
        if (clothing != null) {
            c.initialize(staff, clothing);
        } else {
            c.initialize(staff, footwear);
        }

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.setTitle(TITLE);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream(ICON_URL)));
        primaryStage.setResizable(false);
        primaryStage.show();
        // Hides previous window
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
    }
}