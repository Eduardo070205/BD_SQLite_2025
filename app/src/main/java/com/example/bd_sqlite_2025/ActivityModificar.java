package com.example.bd_sqlite_2025;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import db.EscuelaBD;
import entities.Alumno;

public class ActivityModificar extends Activity {

    private RecyclerView recyclerView;

    private RecyclerView.Adapter adapter;

    private RecyclerView.LayoutManager layoutManager;

    EscuelaBD bd;

    EditText cajaIdAlumno, cajaNombre;

    ArrayList<Alumno> datos = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificaciones);

        cajaIdAlumno = findViewById(R.id.cajaModificarNumControl);

        cajaNombre = findViewById(R.id.cajaModificarNombre);

        bd = EscuelaBD.getAppDatabase(this);

        recyclerView = findViewById(R.id.recyclerActualizar);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);


        new Thread(new Runnable() {
            @Override
            public void run() {
                datos = (ArrayList<Alumno>) bd.alumnoDAO().mostrarTodos();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        adapter = new CustomAdapter(datos);
                        recyclerView.setAdapter(adapter);

                    }
                });

            }
        }).start();

    }

    public void cargarDatos(View v){

        new Thread(new Runnable() {
            @Override
            public void run() {

                datos = (ArrayList<Alumno>) bd.alumnoDAO().mostrarPorNoControl(cajaIdAlumno.getText().toString());

                cajaNombre.setText(datos.get(0).getNombre());

            }
        }).start();

    }

    public void actualizarDatos(View v){

        String numControl = cajaIdAlumno.getText().toString(), nombre = cajaNombre.getText().toString().toUpperCase();

        new Thread(new Runnable() {
            @Override
            public void run() {


                int numFilas = bd.alumnoDAO().actualizarAlumnoPorNumControl(nombre, numControl);

                datos = (ArrayList<Alumno>) bd.alumnoDAO().mostrarTodos();


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        adapter = new CustomAdapter(datos);
                        recyclerView.setAdapter(adapter);

                        if (numFilas == 1){

                            Toast.makeText(getBaseContext(), "Actualización correcta", Toast.LENGTH_LONG).show();

                        }else{

                            Toast.makeText(getBaseContext(), "Actualización Incorrecta", Toast.LENGTH_LONG).show();

                        }


                    }
                });
            }
        }).start();

    }

    public void restablecer(View v){

        cajaNombre.setText("");
        cajaIdAlumno.setText("");


    }

}
