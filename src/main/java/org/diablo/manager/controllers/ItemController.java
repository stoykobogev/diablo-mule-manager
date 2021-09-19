package org.diablo.manager.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.diablo.manager.services.DataService;
import org.diablo.manager.services.ViewService;

import java.util.List;
import java.util.stream.Collectors;

public class ItemController {

    @FXML
    private TextField itemName;

    @FXML
    private ListView<Text> itemList;

    @FXML
    public void initialize() {
        List<String> itemNames = DataService.getItemNames();

        for (String itemName : itemNames) {
            itemList.getItems().add(ViewService.generateText(itemName));
        }
    }

    @FXML
    public void onAddItem() {
        String newItemName = itemName.getText();
        if (newItemName != null && !newItemName.isBlank()) {
            itemList.getItems().add(ViewService.generateText(newItemName));

            saveItems();
        }
    }

    @FXML
    public void onRemoveItem() {
        int selectedItemIndex = itemList.getSelectionModel().getSelectedIndex();
        if (selectedItemIndex > -1) {
            itemList.getItems().remove(selectedItemIndex);

            saveItems();
        }
    }

    private void saveItems() {
        DataService.saveItemNames(itemList.getItems().stream().map(Text::getText).collect(Collectors.toList()));
    }
}
