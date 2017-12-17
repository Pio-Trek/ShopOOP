package service;

import controllers.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import models.*;

import java.io.IOException;

import static service.ControllerService.*;


/**
 * Create new stages helper class.
 */
public class StageService {

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
        getStage(root, primaryStage);
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

        switch (controller) {
            case VIEW_PRODUCTS: {
                ViewProductsController c = loader.getController();
                Customer anonymousCustomer = new Customer();

                // Passes the {@link Customer} object to the {@link CustomerHomeController}
                c.initialize(anonymousCustomer);
                break;
            }
        }

        getStage(root, primaryStage);

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
                c.initialize(customer);
                break;
            }
            // Edit current Customer details
            case ADD_EDIT_CUSTOMER: {
                AddEditCustomerController c = loader.getController();
                c.initialize(customer);
                break;
            }
            case VIEW_PRODUCTS: {
                ViewProductsController c = loader.getController();
                c.initialize(customer);
                break;
            }
            case VIEW_ORDERS: {
                ViewOrdersController c = loader.getController();
                c.initialize(customer);
                break;
            }
        }

        getStage(root, primaryStage);

        // Hides previous window
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
    }

    public void loadStage(ActionEvent actionEvent, Customer customer, ObservableList<OrderLine> basket, String controller) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        Stage primaryStage = new javafx.stage.Stage();
        loader.setLocation(Start.class.getResource(controller));
        Parent root = loader.load();

        switch (controller) {
            case CUSTOMER_BASKET: {
                CustomerBasketController c = loader.getController();
                c.initialize(customer, basket);
                break;
            }
            case VIEW_PRODUCTS: {
                ViewProductsController c = loader.getController();
                c.initialize(customer, basket);
                break;
            }
            case ADD_EDIT_CUSTOMER: {
                AddEditCustomerController c = loader.getController();
                c.initialize(customer, basket);
                break;
            }
            case CUSTOMER_LOGIN: {
                CustomerLoginController c = loader.getController();
                c.initialize(customer, basket);
                break;
            }
        }

        getStage(root, primaryStage);

        // Hides previous window
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
    }

    public void loadStage(ActionEvent actionEvent, Customer customer, Staff staff, int orderId, String controller) throws Exception {

        FXMLLoader loader = new FXMLLoader();
        Stage primaryStage = new javafx.stage.Stage();
        loader.setLocation(Start.class.getResource(controller));
        Parent root = loader.load();

        if (customer != null) {
            ViewOrderLinesController c = loader.getController();
            c.initialize(customer, orderId);
        } else if (staff != null) {
            ViewOrderLinesController c = loader.getController();
            c.initialize(staff, orderId);
        } else {
            throw new IllegalStateException("ERROR: Customer and Staff object cannot be both null.");
        }

        getStage(root, primaryStage);

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
            case VIEW_PRODUCTS: {
                ViewProductsController c = loader.getController();
                c.initialize(staff);
                break;
            }
            case EDIT_PRODUCTS: {
                EditProductsController c = loader.getController();
                c.initialize(staff);
                break;
            }
            case VIEW_ORDERS: {
                ViewOrdersController c = loader.getController();
                c.initialize(staff);
                break;
            }
        }

        getStage(root, primaryStage);

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

        getStage(root, primaryStage);

        // Hides previous window
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
    }

    private void getStage(Parent root, Stage primaryStage) {
        Scene scene = new Scene(root, 770, 645);
        primaryStage.setScene(scene);
        String TITLE = "ShopOOP - Piotr Przechodzki";
        primaryStage.setTitle(TITLE);
        String ICON_URL = "../views/images/store.png";
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream(ICON_URL)));
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}