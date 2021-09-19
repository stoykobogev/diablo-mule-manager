package org.diablo.manager.services;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.diablo.manager.entities.Item;

import java.util.List;

public class ViewService {

    private static final Font DEFAULT_FONT = Font.font("Calibri", 14);
    private static final Insets DEFAULT_ITEM_PANE_PADDING = new Insets(5, 0, 5, 0);

    public static Text generateText(String value) {
        Text text = new Text(value);
        text.setFont(DEFAULT_FONT);

        return text;
    }

    public static <T extends Parent> GridPane generateItemPane(Item item) {
        return doGenerateItemPane(item.getName(), item.getDescription(), item.getNote());
    }

    public static <T extends Parent> GridPane generateItemPane() {
        return doGenerateItemPane(null, null, null);
    }

    private static <T extends Parent> GridPane doGenerateItemPane(
            String itemName, String itemDescription, String itemNote) {

        GridPane pane = generateGridPane();
        List<String> saveditemNames = DataService.getItemNames();
        ChoiceBox<String> itemNamesChoiceBox =
                new ChoiceBox<>(FXCollections.observableArrayList(saveditemNames));
        itemNamesChoiceBox.setPrefWidth(250);

        if (itemName != null) {
            for (String savedItemName : saveditemNames) {
                if (savedItemName.equals(itemName)) {
                    itemNamesChoiceBox.getSelectionModel().select(savedItemName);
                    break;
                }
            }
        }

        pane.add(generateLabel("Name:"), 0, 0);
        pane.add(itemNamesChoiceBox, 1, 0);
        pane.add(generateLabel("Description:"), 0, 1);
        pane.add(generateItemTextField(itemDescription), 1, 1);
        pane.add(generateLabel("Note:"), 0, 2);
        pane.add(generateItemTextField(itemNote), 1, 2);

        return pane;
    }

    private static Label generateLabel(String text) {
        Label label = new Label(text);
        label.setFont(DEFAULT_FONT);

        return label;
    }

    private static TextField generateItemTextField(String text) {
        TextField textField = new TextField();
        textField.setFont(DEFAULT_FONT);
        textField.setPrefWidth(250);
        textField.setText(text);

        return textField;
    }

    private static GridPane generateGridPane() {
        GridPane pane = new GridPane();
        pane.setHgap(6);
        pane.setVgap(6);
        pane.setPadding(DEFAULT_ITEM_PANE_PADDING);

        return pane;
    }
}
