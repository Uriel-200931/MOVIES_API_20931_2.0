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
    private String url = "http://10.10.62.24:3300/";

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
                                        listMovies();
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
                JSONObject peliculass = new JSONObject();
                try {
                    peliculass.put("titulo", ettitulo.getText().toString());
                    peliculass.put("director", etdirector.getText().toString());
                    peliculass.put("año", etaño.getText().toString());
                    peliculass.put("actores", etdirector.getText().toString());
                    peliculass.put("rating", etrating.getText().toString());
                    peliculass.put("descripcion", etdescrpcion.getText().toString());
                    peliculass.put("matricula", etmatricula.getText().toString());
                }catch (JSONException error){
                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                }
                JsonObjectRequest salvar = new JsonObjectRequest(
                        Request.Method.POST,
                        url+"insertar",
                        peliculass,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                //Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                                try {
                                    if (response.getString("status").equals("Obra Guardada"))
                                        Toast.makeText(MainActivity.this, "¡Obra Guardada con exito", Toast.LENGTH_SHORT).show();
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
                                } catch (JSONException e){
                                    Toast.makeText(MainActivity.this,"PELI GUARDADA", Toast.LENGTH_SHORT).show();
                                }listMovies();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this, "PELI GUARDADA", Toast.LENGTH_LONG).show();
                            }
                        }
                );
                colaPeticiones.add(salvar);
            }
        });




        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etmatricula.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Primero use el BOTÓN BUSCAR!", Toast.LENGTH_SHORT).show();
                } else {
                    JSONObject peliupdate = new JSONObject();
                    try {
                        peliupdate.put("numcampeon", etmatricula.getText().toString());



                    } catch (JSONException e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    JsonObjectRequest actualizar = new JsonObjectRequest(
                            Request.Method.PUT,
                            url + "actualizar/" + etmatricula.getText().toString(),
                            peliupdate,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        if (response.getString("status").equals("peli Actualizada")) {
                                            Toast.makeText(MainActivity.this, " peli actualizada!", Toast.LENGTH_SHORT).show();
                                            etmatricula.setText("");
                                            ettitulo.setText("");
                                            etdirector.setText("");
                                            etaño.setText("");
                                            etactores.setText("");
                                            etrating.setText("");
                                            etdescrpcion.setText("");
                                            origenDatos.clear();
                                            adapter.clear();

                                        } else if (response.getString("status").equals("Not Found")) {
                                            Toast.makeText(MainActivity.this, "Campeón no encontrado", Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (JSONException e) {
                                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }listMovies();
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



/*
        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject actualizar = new JSONObject();
                try {
                    actualizar.put("matricula", etmatricula.getText().toString());
                    actualizar.put("titulo", ettitulo.getText().toString());
                    actualizar.put("director", etdirector.getText().toString());
                    actualizar.put("año", etaño.getText().toString());
                    actualizar.put("actores", etactores.getText().toString());
                    actualizar.put("rating", etrating.getText().toString());
                    actualizar.put("descripcion", etdescrpcion.getText().toString());

                }catch(JSONException error){
                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                }
                JsonObjectRequest actualizapeli = new JsonObjectRequest(
                        Request.Method.PUT,
                        url + "actualizar/" + etmatricula.getText().toString(),
                        9actualizar,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if (response.getString("status").equals("Obra Actualizada"))
                                        Toast.makeText(MainActivity.this, "Obra Actualizada exitosamente", Toast.LENGTH_SHORT).show();
                                    etmatricula.setText("");
                                    ettitulo.setText("");
                                    etdirector.setText("");
                                    etaño.setText("");
                                    etactores.setText("");
                                    etrating.setText("");
                                    etdescrpcion.setText("");
                                    origenDatos.clear();
                                    adapter.clear();

                                } catch (JSONException e) {
                                    Toast.makeText(MainActivity.this, "Error al actualizar", Toast.LENGTH_SHORT).show();
                                }
                                listMovies();;
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                );
                colaPeticiones.add(actualizapeli);
            }
        });

*/






        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etmatricula.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "ingrese la matricula", Toast.LENGTH_SHORT).show();
                } else {
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                            Request.Method.DELETE,
                            url + "delete/" + etmatricula.getText().toString(),
                            null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        if (response.getString("status").equals("peli eliminada")) {
                                            Toast.makeText(MainActivity.this, "pelicula eliminada!", Toast.LENGTH_SHORT).show();
                                            etmatricula.setText("");
                                            adapter.clear();
                                            lvmovies.setAdapter(adapter);
                                            listMovies();
                                        } else if (response.getString("status").equals("Not Found")) {
                                            Toast.makeText(MainActivity.this, "peli no encontrada", Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (JSONException e) {
                                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }listMovies();
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

/*
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JsonObjectRequest eliminapeli = new JsonObjectRequest(
                        Request.Method.DELETE,
                        url+"delete/"+etmatricula.getText().toString(),
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                if (response.has("status")) {
                                    Toast.makeText(MainActivity.this, "Pelicula eliminada", Toast.LENGTH_SHORT).show();
                                } else if (response.getString("status").equals("Not Found")) {
                                    Toast.makeText(MainActivity.this, "Pelicula no encontrada", Toast.LENGTH_SHORT).show();
                                }

                                } catch (JSONException e) {
                                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this, "Puede que no exista el id , error al eliminar.", Toast.LENGTH_LONG).show();
                            }
                        }
                );
                colaPeticiones.add(eliminapeli);
            }
        });*/















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






