package org.diablo.manager.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import org.diablo.manager.entities.Item;
import org.diablo.manager.entities.Mule;
import org.diablo.manager.services.DataService;
import org.diablo.manager.services.ViewService;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ViewController {

    @FXML
    private TreeView<Text> viewMuleTree;

    @FXML
    private ListView<GridPane> viewItemList;

    @FXML
    public void initialize() {
        initializeMules(DataService.getMules());

        DataService.subscribeToMules(this::initializeMules);
    }

    private void initializeMules(List<Mule> mules) {
        TreeItem<Text> rootItem = new TreeItem<>();
        TreeMap<String, Integer> itemQuantityMap = new TreeMap<>();

        for (Mule mule : mules) {
            TreeItem<Text> muleTreeItem = new TreeItem<>(ViewService.generateText(mule.getName()));

            for (Item item : mule.getItems()) {
                String itemName = item.getName();
                TreeItem<Text> itemTreeItem = new TreeItem<>(ViewService.generateText(itemName));

                String description = item.getDescription();
                if (description != null && !description.isBlank()) {
                    TreeItem<Text> descriptionTreeItem = new TreeItem<>(
                            ViewService.generateText("Description: " + description));
                    itemTreeItem.getChildren().add(descriptionTreeItem);
                }

                String note = item.getNote();
                if (note != null && !note.isBlank()) {
                    TreeItem<Text> noteTreeItem = new TreeItem<>(
                            ViewService.generateText("Note: " + note));
                    itemTreeItem.getChildren().add(noteTreeItem);
                }

                muleTreeItem.getChildren().add(itemTreeItem);

                if (itemQuantityMap.containsKey(itemName)) {
                    itemQuantityMap.put(itemName, itemQuantityMap.get(itemName) + 1);
                } else {
                    itemQuantityMap.put(itemName, 1);
                }
            }

            rootItem.getChildren().add(muleTreeItem);

            int row = 0;
            for (Map.Entry<String, Integer> entry : itemQuantityMap.entrySet()) {
                GridPane gridPane = new GridPane();
                gridPane.setPrefWidth(viewItemList.getPrefWidth() - 20);
                gridPane.getColumnConstraints().add(new ColumnConstraints((gridPane.getPrefWidth() / 5) * 4)); // column 0 is 100 wide
                gridPane.getColumnConstraints().add(new ColumnConstraints(gridPane.getPrefWidth() / 5));

                gridPane.add(ViewService.generateText(entry.getKey()), 0, row);
                gridPane.add(ViewService.generateText(entry.getValue().toString()), 1, row);

                row++;

                viewItemList.getItems().add(gridPane);
            }
        }

        viewMuleTree.setRoot(rootItem);
        viewMuleTree.setShowRoot(false);
    }
}
