package controllers;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import models.OrderLine;

public class CustomerBasketController {

    private final String POUND_SYMBOL = "\u00A3 ";

    @FXML
    private TableView<OrderLine> tableViewBasket;
    @FXML
    private TableColumn<OrderLine, Integer> tableColumnId;
    @FXML
    private TableColumn<OrderLine, String> tableColumnName;
    @FXML
    private TableColumn<OrderLine, String> tableColumnPrice;
    @FXML
    private TableColumn<OrderLine, Integer> tableColumnQuantity;
    @FXML
    private TableColumn<OrderLine, String> tableColumnTotal;
    @FXML
    private Label labelTotal;

    private ObservableList<OrderLine> basket = FXCollections.observableArrayList();


    public void initialize(ObservableList<OrderLine> basket) {
        this.basket = basket;

        tableColumnId.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getProduct().getProductId()));
        tableColumnName.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getProduct().getProductName()));
        tableColumnPrice.setCellValueFactory(p -> new SimpleObjectProperty<>(POUND_SYMBOL + p.getValue().getProduct().getPrice()));
        tableColumnQuantity.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getQuantity()));
        tableColumnTotal.setCellValueFactory(p -> new SimpleObjectProperty<>(POUND_SYMBOL + p.getValue().getLineTotal()));

        tableViewBasket.setItems(basket);

        double total = basket.stream().mapToDouble(OrderLine::getLineTotal).reduce(0, (x, y) -> x + y);

        labelTotal.setText("Total: " + POUND_SYMBOL + String.format("%.2f", total));
    }

    @FXML
    private void selectProduct() {
        OrderLine ol = tableViewBasket.getSelectionModel().selectedItemProperty().get();
        System.out.println(ol.getProduct().getProductName());

    }

    @FXML
    private void removeProductFromBasket(ActionEvent actionEvent) {
        OrderLine model = tableViewBasket.getSelectionModel().selectedItemProperty().get();
        basket.remove(model);

    }

    @FXML
    private void buyProducts(ActionEvent actionEvent) {
    }

    @FXML
    private void back(ActionEvent actionEvent) {
    }
}
