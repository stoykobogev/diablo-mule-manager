package org.diablo.manager.entities;

public class Item {

    private String name;
    private String description;
    private String note;

    public Item() {}

    public Item(String name, String description, String note) {
        this.name = name;
        this.description = description;
        this.note = note;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
