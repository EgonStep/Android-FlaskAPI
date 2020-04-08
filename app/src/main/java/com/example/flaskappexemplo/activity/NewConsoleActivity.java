package com.example.flaskappexemplo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.flaskappexemplo.R;
import com.example.flaskappexemplo.model.Console;
import com.example.flaskappexemplo.util.APISingleton;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.flaskappexemplo.util.Constants.CREATED_CONSOLE;
import static com.example.flaskappexemplo.util.Constants.IS_ACTIVE_FLAG;
import static com.example.flaskappexemplo.util.Constants.UPDATED_CONSOLE;
import static com.example.flaskappexemplo.util.Constants.URI_CONSOLE;

public class NewConsoleActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText editName, editYear, editPrice, editTotalGames;
    private Spinner spinnerIsActive;
    private long id;
    private boolean isActive;
    private Console console;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_console);

        editName = findViewById(R.id.editName);
        editYear = findViewById(R.id.editYear);
        editPrice = findViewById(R.id.editPrice);
        editTotalGames = findViewById(R.id.editTotalGames);
        spinnerIsActive = findViewById(R.id.spinnerIsActive);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(NewConsoleActivity.this,
                android.R.layout.simple_spinner_item, IS_ACTIVE_FLAG);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIsActive.setAdapter(adapter);
        spinnerIsActive.setOnItemSelectedListener(this);

        id = getIntent().getLongExtra("ID",0);

        if (id != 0) {
            loadConsole();
        }
    }

    private void loadConsole() {
        String url = URI_CONSOLE + "/" + id;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                console = new Console();
                try {
                    console.setId(response.getLong("id"));
                    console.setName(response.getString("name"));
                    console.setYear(response.getInt("year"));
                    console.setPrice(response.getDouble("price"));
                    console.setTotalGames(response.getInt("total_games"));
                    console.setActive(response.getBoolean("is_active"));

                    editName.setText(console.getName());
                    editYear.setText(String.valueOf(console.getYear()));
                    editPrice.setText(String.valueOf(console.getPrice()));
                    editTotalGames.setText(String.valueOf(console.getTotalGames()));
                    spinnerIsActive.setSelection((console.isActive()) ? 0 : 1);
                } catch (JSONException e) {
                    e.printStackTrace();
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

    private void createConsole(String url, final int method){
        JSONObject object = new JSONObject();

        try {
            object.put("name", editName.getText().toString());
            object.put("year", Integer.parseInt(editYear.getText().toString()));
            object.put("price", Double.parseDouble(editPrice.getText().toString()));
            object.put("total_games", Integer.parseInt(editTotalGames.getText().toString()));
            object.put("is_active", isActive);

            JsonObjectRequest request = new JsonObjectRequest(method, url, object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String message;
                        if(method == Request.Method.POST)
                            message = CREATED_CONSOLE + response.getLong("id");
                        else
                            message = UPDATED_CONSOLE + response.getLong("id");
                        Toast.makeText(NewConsoleActivity.this, message, Toast.LENGTH_SHORT).show();
                        Intent main = new Intent(NewConsoleActivity.this, MainActivity.class);
                        startActivity(main);
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) { }
            });

            APISingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void saveConsole(View view){
        if (id != 0)
            createConsole(URI_CONSOLE + "/" + id, Request.Method.PUT);
        else
            createConsole(URI_CONSOLE, Request.Method.POST);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                isActive = true;
                break;
            case 1:
                isActive = false;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) { }
}
