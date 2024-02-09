package com.example.fetch;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SQLHelper extends SQLiteOpenHelper {
    static final String database_name = "FetchAppDB";

    public static final String table_name = "FetchTable";

    public static final String _id_ = "id";

    public static final String _lid_ = "listId";

    public static final String _name_ = "name";

    private static final String create_query =
            "create table " + table_name + "(" +
                    _id_ + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    _lid_ + " INTEGER NULL, " +
                    _name_ + " TEXT NULL" +
            ");";

    public SQLHelper(Context c) {
        super(c, database_name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(create_query);
    }

    // not public since we dont need it to be public
    void addRow(JSONObject j) throws JSONException {
        int id = j.getInt(_id_);

        ContentValues values = new ContentValues();
        values.put(_id_, id);

        if (j.isNull(_lid_) || j.getString(_name_).equals("null")) values.putNull(_lid_);
        else values.put(_lid_, j.getInt(_lid_));

        if (j.isNull(_name_) || j.getString(_name_).equals("null")) values.putNull(_name_);
        else values.put(_name_, j.getString(_name_));

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(table_name, null, values);
//        db.close(); // Closing database connection
    }

    // add all the rows returned from the response
    public void addRows(JSONArray response) throws JSONException {
        for (int i = 0; i < response.length(); i++) {
            addRow(response.getJSONObject(i));
        }
    }

    public ArrayList<fRow> getAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT  * FROM " + table_name, null);
        ArrayList<fRow> ans = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                fRow row = new fRow();
                row.id = cursor.getInt(0);

                if (!cursor.isNull(1)) {
                    row.listId = cursor.getInt(1);
                }

                if (!cursor.isNull(2)) {
                    row.name = cursor.getString(2);
                }

                ans.add(row);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return ans;
    }

    public ArrayList<fRow> fetchQuery() {
        String query = "SELECT * " +
                        "FROM " + table_name +
                        " WHERE " + _name_ + " IS NOT NULL AND " + _name_ + " != ''" +
                        " ORDER BY " + _lid_ + ", " + _name_;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<fRow> ans = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                fRow row = new fRow();
                row.id = cursor.getInt(0);

                if (!cursor.isNull(1)) {
                    row.listId = cursor.getInt(1);
                }

                if (!cursor.isNull(2)) {
                    row.name = cursor.getString(2);
                }

                ans.add(row);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return ans;
    }

    public void clearDatabase() {
        this.getWritableDatabase().execSQL("DELETE FROM " + table_name);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + table_name);
        onCreate(db);
    }
}
