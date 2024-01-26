package com.example.fetch;


import android.app.ProgressDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FirstFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FirstFrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String urlStr = "https://fetch-hiring.s3.amazonaws.com/hiring.json";

    ExecutorService executorService = Executors.newFixedThreadPool(4);

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button ffb;

    private TextView fetchText;

    private static SQLHelper database;

    public FirstFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FirstFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static FirstFrag newInstance(String param1, String param2) {
        FirstFrag fragment = new FirstFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_first, container, false);

        ffb = view.findViewById(R.id.ff_b);

        fetchText = view.findViewById(R.id.fetchText);

        fetchText.setMovementMethod(new ScrollingMovementMethod());

        ffb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            JSONArray response = fetchFromUrl();
                            fetchText.setText("entering data into the database...");

                            if (response != null) {
                                try {
                                    afterFetch(response);
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
                );
            }
        });

        database = new SQLHelper(getActivity());

        return view;
    }

    public JSONArray fetchFromUrl() {
        try {
            HttpURLConnection connection1 = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(urlStr);
                fetchText.setText("opening connection");
                connection1 = (HttpURLConnection) url.openConnection();
                fetchText.setText("opened connection");
                connection1.connect();
                fetchText.setText("made connection...now fetching");

                reader = new BufferedReader(new InputStreamReader(connection1.getInputStream()));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                return new JSONArray(buffer.toString());
            } catch (MalformedURLException e) {
                fetchText.setText("error");
                e.printStackTrace();
            } catch (IOException e) {
                fetchText.setText("error");
                e.printStackTrace();
            } finally {
                if (connection1 != null) {
                    connection1.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
        }

        return null;
    }

    public void displayTheQuery() {
        ArrayList<fRow> rows = database.fetchQuery();

        if (rows.size() == 0) {
            fetchText.setText("no rows display :)");
            return;
        }

        String text = "Please scroll to see more..\n\n\nid  |  listID  |  name\n\n";

        for (int i = 0; i < rows.size(); i++) {
            fRow row = rows.get(i);

            // assuming no ids are set to null
            text += String.valueOf(row.id);
            text += "    |    ";

            if (row.listId == null) {
                text += "null";
            } else {
                text += String.valueOf(row.listId);
            }
            text += "   |   ";

            if (row.name == null) {
                text += "null";
            } else {
                text += row.name;
            }

            text += "\n";
        }

        fetchText.setText(text);
    }

    public void displayAllRows() {
        ArrayList<fRow> rows = database.getAll();

        if (rows.size() == 0) {
            fetchText.setText("no rows to display :)");
            return;
        }

        String text = "Please scroll to see more..\n\n\n";

        for (int i = 0; i < rows.size(); i++) {
            fRow row = rows.get(i);

            // assuming no ids are set to null
            text += String.valueOf(row.id);
            text += "    |    ";

            if (row.listId == null) {
                text += "null";
            } else {
                text += String.valueOf(row.listId);
            }
            text += "   |   ";

            if (row.name == null) {
                text += "null";
            } else {
                text += row.name;
            }

            text += "\n";
        }

        fetchText.setText(text);
    }

    public void afterFetch(JSONArray response) throws JSONException {
        database.addRows(response);
        fetchText.setText("Data entered into the database, now displaying...");

//        displayAllRows();
        displayTheQuery();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}