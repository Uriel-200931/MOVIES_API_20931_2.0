package mx.edu.utxj.ti.idgs.dwi.demo_movies_200931;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public  class MainActivity extends AppCompatActivity {

    private Button btnGuardar;
    private Button btnBuscar;
    private Button btnActualizar;
    private Button btnEliminar;

    private EditText etcodigoBarras;
    private EditText ettitulo;
    private EditText etdirector;
    private EditText etaño;
    private EditText etgenero;
    private EditText etdescrpcion;
    private ListView lvmovies;
    private RequestQueue colaPeticiones;
    private JsonArrayRequest jsonArrayRequest;

    private ArrayList<String> origenDatos = new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    private String url = "http://192.168.0.101:3300/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnGuardar = findViewById(R.id.btnSave);
        btnActualizar = findViewById(R.id.btnUpdate);
        btnBuscar = findViewById(R.id.btnSearch);
        btnEliminar = findViewById(R.id.btnDelete);
        etcodigoBarras = findViewById(R.id.etCodigoBarras);
        ettitulo = findViewById(R.id.etTitulo);
        etdirector = findViewById(R.id.etDirector);
        etaño = findViewById(R.id.etAño);
        etgenero= findViewById(R.id.etGenero);
        etdescrpcion = findViewById(R.id.etDescrpcion);
        lvmovies = findViewById(R.id.lvMovies);
        colaPeticiones = Volley.newRequestQueue(this);
        listMovies();

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JsonObjectRequest peticion = new JsonObjectRequest(
                        Request.Method.GET,
                        url  + etcodigoBarras.getText().toString(),
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                if (response.has("status"))
                                    Toast.makeText(MainActivity.this, "pelicula no encontrada", Toast.LENGTH_SHORT).show();
                                else {
                                    try {
                                        etcodigoBarras.setText(String.valueOf(response.getInt("codigobarras")));
                                        ettitulo.setText(response.getString("titulo"));
                                        etdirector.setText(response.getString("director"));
                                        etaño.setText(String.valueOf(response.getInt("año")));
                                        etgenero.setText(response.getString("genero"));
                                        etdescrpcion.setText(response.getString("descripcion"));
                                        adapter.clear();
                                        lvmovies.setAdapter(adapter);
                                        listMovies();

                                    } catch (JSONException e) {
                                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this, "inserte un codigo de barras", Toast.LENGTH_SHORT).show();
                            }
                        }
                );
                colaPeticiones.add(peticion);
            }
        });


        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etcodigoBarras.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Por favor lene los campos", Toast.LENGTH_SHORT).show();
                }else{
                    JSONObject movie = new JSONObject();
                    try {
                        movie.put("codigobarras", etcodigoBarras.getText().toString());
                        movie.put("titulo", ettitulo.getText().toString());
                        movie.put("director",etdirector.getText().toString());
                        movie.put("año", etaño.getText().toString());
                        movie.put("genero", etgenero.getText().toString());
                        movie.put("descripcion", etdescrpcion.getText().toString());


                    } catch (JSONException e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                            Request.Method.POST,
                            url +"insertar",
                            movie,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        if (response.getString("status").equals("Movie insertada")) {
                                            Toast.makeText(MainActivity.this, "Pelicula Insertada!", Toast.LENGTH_SHORT).show();
                                            etcodigoBarras.setText("");
                                            ettitulo.setText("");
                                            etdirector.setText("");
                                            etaño.setText("");
                                            etgenero.setText("");
                                            etdescrpcion.setText("");
                                            adapter.clear();
                                            lvmovies.setAdapter(adapter);
                                            listMovies();
                                        }
                                    }catch (JSONException e) {
                                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                    );
                    colaPeticiones.add(jsonObjectRequest);
                }
            }
        });




        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etcodigoBarras.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Ingrese el codigo de barras", Toast.LENGTH_SHORT).show();
                } else {
                    JSONObject movies = new JSONObject();
                    try {
                        movies.put("codigobarras", etcodigoBarras.getText().toString());

                        if (!ettitulo.getText().toString().isEmpty()) {
                            movies.put("titulo", ettitulo.getText().toString());
                        }

                        if (!etdirector.getText().toString().isEmpty()) {
                            movies.put("director", etdirector.getText().toString());
                        }

                        if (!etaño.getText().toString().isEmpty()) {
                            movies.put("año", etaño.getText().toString());
                        }

                        if (!etgenero.getText().toString().isEmpty()) {
                            movies.put("genero", Float.parseFloat(etgenero.getText().toString()));
                        }

                        if (!etdescrpcion.getText().toString().isEmpty()) {
                            movies.put("descripcion", etdescrpcion.getText().toString());
                        }



                    } catch (JSONException e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    JsonObjectRequest actualizar = new JsonObjectRequest(
                            Request.Method.PUT,
                            url + "actualizar/" + etcodigoBarras.getText().toString(),
                            movies,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        if (response.getString("status").equals("Movie actualizada")) {
                                            Toast.makeText(MainActivity.this, "Movie actualizada", Toast.LENGTH_SHORT).show();
                                            etcodigoBarras.setText("");
                                            ettitulo.setText("");
                                            etdirector.setText("");
                                            etaño.setText("");
                                            etgenero.setText("");
                                            etdescrpcion.setText("");
                                            adapter.clear();
                                            lvmovies.setAdapter(adapter);
                                            listMovies();
                                        } else if (response.getString("status").equals("Not Found")) {
                                            Toast.makeText(MainActivity.this, "Movie no encontrada", Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (JSONException e) {
                                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                    );
                    colaPeticiones.add(actualizar);
                }
            }
        });

        //Button Delete
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etcodigoBarras.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Ingrese el codigo de barras", Toast.LENGTH_SHORT).show();
                } else {
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                            Request.Method.DELETE,
                            url + "eliminar/" + etcodigoBarras.getText().toString(),
                            null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        if (response.getString("status").equals("Movie eliminada")) {
                                            Toast.makeText(MainActivity.this, "Movie eliminada", Toast.LENGTH_SHORT).show();
                                            etcodigoBarras.setText("");
                                            ettitulo.setText("");
                                            etdirector.setText("");
                                            etaño.setText("");
                                            etgenero.setText("");
                                            etdescrpcion.setText("");
                                            adapter.clear();
                                            lvmovies.setAdapter(adapter);
                                            listMovies();
                                        } else if (response.getString("status").equals("Not Found")) {
                                            Toast.makeText(MainActivity.this, "Movie no encontrada", Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (JSONException e) {
                                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                    );
                    colaPeticiones.add(jsonObjectRequest);
                }
            }
        });




    }















    protected void listMovies() {
        jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for(int i = 0 ; i<response.length();i++){
                            try {
                                String codigobarras = response.getJSONObject(i).getString("codigobarras");
                                String titulo = response.getJSONObject(i).getString("titulo");
                                String genero = response.getJSONObject(i).getString("genero");
                                String descripcion = response.getJSONObject(i).getString("descripcion");
                                origenDatos.add(codigobarras+" -> "+titulo+" -> "+genero+descripcion);
                            } catch (JSONException e) {

                            }
                        }
                        adapter = new ArrayAdapter<>(MainActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, origenDatos);
                        lvmovies.setAdapter(adapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        colaPeticiones.add(jsonArrayRequest);
    }
}



