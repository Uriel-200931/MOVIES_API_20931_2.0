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

    private EditText etmatricula;
    private EditText ettitulo;
    private EditText etdirector;
    private EditText etaño;
    private EditText etactores;
    private EditText etrating;
    private EditText etdescrpcion;
    private ListView lvmovies;
    private RequestQueue colaPeticiones;
    private JsonArrayRequest jsonArrayRequest;

    private ArrayList<String> origenDatos = new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    private String url = "http://192.168.0.100:3300/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnGuardar = findViewById(R.id.btnSave);
        btnActualizar = findViewById(R.id.btnUpdate);
        btnBuscar = findViewById(R.id.btnSearch);
        btnEliminar = findViewById(R.id.btnDelete);
        etmatricula = findViewById(R.id.etMatricula);
        ettitulo = findViewById(R.id.etTitulo);
        etdirector = findViewById(R.id.etDirector);
        etaño = findViewById(R.id.etAño);
        etactores = findViewById(R.id.etActores);
        etrating = findViewById(R.id.etRating);
        etdescrpcion = findViewById(R.id.etDescrpcion);
        lvmovies = findViewById(R.id.lvMovies);
        colaPeticiones = Volley.newRequestQueue(this);
        listMovies();

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JsonObjectRequest peticion = new JsonObjectRequest(
                        Request.Method.GET,
                        url +etmatricula.getText().toString(),
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                if (response.has("status"))
                                    Toast.makeText(MainActivity.this, "pelicula no encontrado", Toast.LENGTH_SHORT).show();
                                else {
                                    try {
                                        etmatricula.setText(String.valueOf(response.getInt("matricula")));
                                        ettitulo.setText(response.getString("titulo"));
                                        etdirector.setText(response.getString("director"));
                                        etaño.setText(String.valueOf(response.getInt("año")));
                                        etactores.setText(response.getString("actores"));
                                        etrating.setText(String.valueOf(response.getInt("rating")));
                                        etdescrpcion.setText(response.getString("descripcion"));
                                        listMovies()
                                    } catch (JSONException e) {
                                     Toast.makeText(MainActivity.this, "Pelicula no encontrada", Toast.LENGTH_LONG).show();
                                        
                                    }
                                }
                            }

                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                               Toast.makeText(MainActivity.this, "Pelicula no encontrada", Toast.LENGTH_LONG).show();
                            }
                        }
                );
                colaPeticiones.add(peticion);
            }
        });


        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject producto = new JSONObject();
                try {
                    producto.put("titulo", ettitulo.getText().toString());
                    producto.put("director", etdirector.getText().toString());
                    producto.put("año", Float.parseFloat(etaño.getText().toString()));
                    producto.put("actores", etdirector.getText().toString());
                    producto.put("rating", Float.parseFloat(etrating.getText().toString()));
                    producto.put("descripcion", etdescrpcion.getText().toString());
                    producto.put("matricula", Float.parseFloat(etmatricula.getText().toString()));
                } catch (JSONException e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.POST,
                        url + "insertar/",
                        producto,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if (response.getString("status").equals("Pelicula insertada")) {
                                        Toast.makeText(MainActivity.this, "Pelicula insertada con ÉXITO!", Toast.LENGTH_SHORT).show();
                                        ettitulo.setText("");
                                        etdirector.setText("");
                                        etaño.setText("");
                                        etactores.setText("");
                                        etrating.setText("");
                                        etdescrpcion.setText("");
                                        etmatricula.setText("");
                                        origenDatos.clear();
                                        adapter.clear();
                                        listMovies();
                                    }
                                } catch (JSONException e) {
                                     Toast.makeText(MainActivity.this,"Error al Guardar", Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                               Toast.makeText(MainActivity.this, "Error en el servidor", Toast.LENGTH_LONG).show();
                            }
                        }
                );
                colaPeticiones.add(jsonObjectRequest);
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
                        origenDatos.clear();
                        //Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                        for(int i = 0;i < response.length(); i++){
                            try {
                                Integer  matricula = response.getJSONObject(i).getInt("matricula");
                                String titulo =response.getJSONObject(i).getString("titulo");
                                Integer  año = response.getJSONObject(i).getInt("año");
                                String actores =response.getJSONObject(i).getString("actores");

                                origenDatos.add(matricula+" - "+ titulo + " - "+año+" - "+actores);
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
                        Toast.makeText(MainActivity.this, error.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
        );
        colaPeticiones.add(jsonArrayRequest);
    }
}






