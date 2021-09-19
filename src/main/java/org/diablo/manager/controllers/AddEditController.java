package org.diablo.manager.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import org.diablo.manager.entities.Item;
import org.diablo.manager.entities.Mule;
import org.diablo.manager.services.DataService;
import org.diablo.manager.services.ViewService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class AddEditController {

    @FXML
    private ChoiceBox<String> muleChoices;

    @FXML
    private TextField editMuleName;

    @FXML
    private ListView<GridPane> editMuleItems;

    private final String newMuleChoice = "New...";
    private final Map<String, Mule> nameMuleMap = new TreeMap<>();
    private Mule selectedMule;
    private List<Mule> savedMules;

    @FXML
    public void initialize() {
        savedMules = DataService.getMules();
        initiateMules();

        DataService.subscribeToMules((mules) -> {
            muleChoices.getItems().clear();
            editMuleItems.getItems().clear();
            nameMuleMap.clear();
            selectedMule = null;
            editMuleName.setText(null);

            initiateMules();
        });

        DataService.subscribeToItems((itemNames) -> {
            if (selectedMule != null) {
                editMuleItems.getItems().clear();
                for (Item item : selectedMule.getItems()) {
                    editMuleItems.getItems().add(ViewService.generateItemPane(item));
                }
            }
        });

        muleChoices.setOnAction(actionEvent -> {
            editMuleItems.getItems().clear();
            String muleName = muleChoices.getValue();

            if (muleName != null) {
                if (newMuleChoice.equals(muleName)) {
                    selectedMule = new Mule();
                    editMuleName.setText(null);
                    addNewItem();
                } else {
                    selectedMule = nameMuleMap.get(muleName);
                    editMuleName.setText(selectedMule.getName());

                    for (Item item : selectedMule.getItems()) {
                        editMuleItems.getItems().add(ViewService.generateItemPane(item));
                    }
                }
            }
        });
    }

    @FXML
    public void onSaveMule() {
        String muleName = editMuleName.getText();

        if (selectedMule == null || muleName == null) return;

        if (!savedMules.contains(selectedMule)) {

            if (nameMuleMap.containsKey(muleName)) {
                return;
            }

            selectedMule = new Mule();
            savedMules.add(selectedMule);
        }

        selectedMule.setName(muleName);

        List<Item> items = new ArrayList<>();
        for (GridPane pane : editMuleItems.getItems()) {
            ObservableList<Node> children = pane.getChildren();
            String name = ((ChoiceBox<String>) children.get(1)).getSelectionModel().getSelectedItem();
            String description = ((TextField) children.get(3)).getText();
            String note = ((TextField) children.get(5)).getText();

            items.add(new Item(name, description, note));
        }

        selectedMule.setItems(items);

        DataService.saveMules(savedMules);
    }

    @FXML
    public void onAddItem() {
        if (muleChoices.getValue() != null) {
            addNewItem();
        }
    }

    @FXML
    public void onRemoveItem() {
        int selectedItemIndex = editMuleItems.getSelectionModel().getSelectedIndex();
        if (selectedItemIndex > -1) {
            editMuleItems.getItems().remove(selectedItemIndex);
        }
    }

    @FXML
    public void onDeleteMule() {
        if (selectedMule != null) {
            savedMules.remove(selectedMule);

            DataService.saveMules(savedMules);
        }
    }

    private void addNewItem() {
        editMuleItems.getItems().add(ViewService.generateItemPane());
    }

    private void initiateMules() {
        for (Mule mule : savedMules) {
            nameMuleMap.put(mule.getName(), mule);
        }

        muleChoices.getItems().add(newMuleChoice);
        muleChoices.getItems().addAll(savedMules.stream()
                .map(Mule::getName)
                .collect(Collectors.toList()));
    }
}
