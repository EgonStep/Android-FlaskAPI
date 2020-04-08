package com.example.flaskappexemplo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.flaskappexemplo.R;
import com.example.flaskappexemplo.model.Console;
import com.example.flaskappexemplo.util.APISingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.flaskappexemplo.util.Constants.URI_CONSOLE;

public class MainActivity extends AppCompatActivity implements ListView.OnItemClickListener{

    private ListView lvConsoles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvConsoles = findViewById(R.id.listConsoles);
    }

    @Override
    protected void onStart(){
        super.onStart();
        loadConsoles();
    }

    private void loadConsoles() {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URI_CONSOLE,
                null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                List<Console> consoles = new ArrayList<>();
                for(int i = 0; i < response.length(); i++){
                    try {
                        JSONObject object = (JSONObject) response.get(i);
                        Console console = new Console();
                        console.setId(object.getLong("id"));
                        console.setName(object.getString("name"));

                        consoles.add(console);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    setConsoleAdapter(consoles);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        APISingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    private void setConsoleAdapter(List<Console> consoles) {
        ArrayAdapter<Console> adapter = new ArrayAdapter<>(
                getApplicationContext(), android.R.layout.simple_list_item_1, consoles
        );
        lvConsoles.setAdapter(adapter);
        lvConsoles.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Console console = (Console) adapterView.getItemAtPosition(position);
        Intent detailConsole = new Intent(MainActivity.this, ConsoleDetailActivity.class);

        detailConsole.putExtra("ID",console.getId());
        startActivity(detailConsole);
    }

    public void newConsole(View view) {
        Intent newConsole = new Intent(MainActivity.this, NewConsoleActivity.class);
        startActivity(newConsole);
    }
}
