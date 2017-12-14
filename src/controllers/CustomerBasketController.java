package controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import models.Product;

import java.util.HashMap;
import java.util.Map;

public class CustomerBasketController {
    @FXML
    private TableView<Map.Entry<Product, Integer>> tableViewBasket;
    @FXML
    private TableColumn<Map.Entry<Product, Integer>, String> tableColumnId;
    @FXML
    private TableColumn<Product, String> tableColumnName;
    @FXML
    private TableColumn<Product, Double> tableColumnPrice;
    @FXML
    private TableColumn<Product, Integer> tableColumnQuantity;

    private Map<Product, Integer> basket = new HashMap<>();


    public void initialize(Map<Product, Integer> basket) {
        this.basket = basket;

        tableColumnId = new TableColumn<>("Key");
        tableColumnId.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<Product, Integer>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<Product, Integer>, String> p) {
                String id = String.valueOf(p.getValue().getKey().getProductId());
                return new SimpleStringProperty(id);
            }
        });


        ObservableList<Map.Entry<Product, Integer>> products = FXCollections.observableArrayList(basket.entrySet());
        tableViewBasket = new TableView<>(products);

        tableViewBasket.getColumns().setAll(tableColumnId);
    }

    @FXML
    private void removeProductFromBasket(ActionEvent actionEvent) {
    }

    @FXML
    private void buyProducts(ActionEvent actionEvent) {
    }

    @FXML
    private void back(ActionEvent actionEvent) {
    }
}
