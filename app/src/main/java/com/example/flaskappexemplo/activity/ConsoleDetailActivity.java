package com.example.flaskappexemplo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
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

import static com.example.flaskappexemplo.util.Constants.DELETE;
import static com.example.flaskappexemplo.util.Constants.DELETE_CONSOLE;
import static com.example.flaskappexemplo.util.Constants.DELETE_WARN;
import static com.example.flaskappexemplo.util.Constants.URI_CONSOLE;

public class ConsoleDetailActivity extends AppCompatActivity {

    private TextView textName, textYear, textPrice, textTotalGames, textIsActive;
    private long id;
    private Console console;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_console_detail);

        id = getIntent().getLongExtra("ID",0);

        textName = findViewById(R.id.textName);
        textYear = findViewById(R.id.textYear);
        textPrice = findViewById(R.id.textPrice);
        textTotalGames = findViewById(R.id.textTotalGames);
        textIsActive = findViewById(R.id.textIsActive);
    }

    @Override
    protected void onStart(){
        super.onStart();
        loadConsole();
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

                    textName.setText(console.getName());
                    textYear.setText(String.valueOf(console.getYear()));
                    textPrice.setText(String.valueOf(console.getPrice()));
                    textTotalGames.setText(String.valueOf(console.getTotalGames()));
                    textIsActive.setText((console.isActive()) ? "SIM" : "NAO");
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

    public void updateConsole(View view) {
        Intent newConsole = new Intent(ConsoleDetailActivity.this, NewConsoleActivity.class);
        newConsole.putExtra("ID", console.getId());
        startActivity(newConsole);
    }

    public void deleteConsole(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(DELETE_WARN + console.getId() + "?");
        builder.setTitle(DELETE);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String url = URI_CONSOLE + "/" + id;
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(ConsoleDetailActivity.this, DELETE_CONSOLE +
                                    response.getLong("id"), Toast.LENGTH_SHORT).show();
                            Intent main = new Intent(ConsoleDetailActivity.this, MainActivity.class);
                            startActivity(main);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                APISingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
            }
        });
        builder.setNegativeButton("NO",null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
