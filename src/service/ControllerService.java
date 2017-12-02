package service;

/**
 * Interface that defines the user interface FXML file locations.
 * Needs to be updated anytime when a new stage is added.
 */
public interface ControllerService {
    String MAIN_MENU = "../views/main_menu.fxml";
    String STAFF_LOGIN = "../views/staff_login.fxml";
    String STAFF_HOME = "../views/staff_home.fxml";
    String CUSTOMER_LOGIN = "../views/customer_login.fxml";
    String CUSTOMER_HOME = "../views/customer_home.fxml";
    String REGISTRATION = "../views/registration.fxml";
    String MODIFY_PRODUCTS = "../views/modify_products.fxml";
    String EDIT_PRODUCTS = "../views/edit_products.fxml";
}