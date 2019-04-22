package com.example.predator.lab2t3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listView = null;
    private String data = null;
    private ArrayList<String> test = new ArrayList<>();
    private String[] stockIDs = {"NOK", "FB", "GOOGL", "AAPL"};
    private String[] names = {"Nokia", "Facebook", "Google", "Apple"};
    private Button add_button;
    private EditText editText_ID, editText_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        add_button = findViewById(R.id.button_add);
        editText_ID = findViewById(R.id.edit_text_id);
        editText_name = findViewById(R.id.edit_text_name);

        for (int i = 0; i < stockIDs.length; i++) {
            getJSONData(stockIDs[i], names[i]);
        }

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editText_name.getText().toString();
                String id = editText_ID.getText().toString();
                getJSONData(id, name);
                editText_ID.setText(null);
                editText_name.setText(null);
            }
        });
    }

    public void getJSONData(final String id, final String name) {
        String address = "https://financialmodelingprep.com/api/company/price/" + id;
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, address, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                data = response.replace("<pre>", "");
                String stringToAdd = null;

                try {
                    JSONObject object = new JSONObject(data);
                    String price = object.getJSONObject(id).getString("price");
                    stringToAdd = name + ": " + price + " USD";
                } catch (Exception e) {
                    e.printStackTrace();
                }

                test.add(stringToAdd);
                setupView();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }



    public void setupView() {
        listView = findViewById(R.id.listaview);
        final ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, test);
        listView.setAdapter(adapter);
    }
}
