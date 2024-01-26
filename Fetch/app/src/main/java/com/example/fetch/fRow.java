package com.example.fetch;

public class fRow {
    public int id = -1;
    public Integer listId = null;
    public String name = null;

    fRow() {
        id = -1;
        listId = null;
        name = null;
    }

    fRow(int _id, int _lid, String _name) {
        id = _id;
        listId = _lid;
        name = _name;
    }
}
