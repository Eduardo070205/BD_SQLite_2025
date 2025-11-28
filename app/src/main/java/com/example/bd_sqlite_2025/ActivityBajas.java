package com.example.bd_sqlite_2025;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import db.EscuelaBD;
import entities.Alumno;

public class ActivityBajas extends Activity {

    private RecyclerView recyclerView;

    private RecyclerView.Adapter adapter;

    private RecyclerView.LayoutManager layoutManager;

    ArrayList<Alumno> datos = null;

    EscuelaBD bd;

    EditText cajaIdEliminar;

    Button btnBuscar, btnEliminar, btnRestablecer;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bajas);

        cajaIdEliminar = findViewById(R.id.cajaIdEliminar);

        bd = EscuelaBD.getAppDatabase(this);

        recyclerView = findViewById(R.id.recyclerEliminar);

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


    public void buscarAlumno(View v){

        new Thread(new Runnable() {
            @Override
            public void run() {
                datos = (ArrayList<Alumno>) bd.alumnoDAO().mostrarPorNoControl(cajaIdEliminar.getText().toString());
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

    public void eliminarAlumno(View v){

        new Thread(new Runnable() {
            @Override
            public void run() {

                int numFilas = bd.alumnoDAO().eliminarAlumnoPorNumControl(cajaIdEliminar.getText().toString());

                datos = (ArrayList<Alumno>) bd.alumnoDAO().mostrarTodos();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        adapter = new CustomAdapter(datos);
                        recyclerView.setAdapter(adapter);
                        if (numFilas == 1){

                            Toast.makeText(getBaseContext(), "Eliminación correcta", Toast.LENGTH_LONG).show();

                        }else{

                            Toast.makeText(getBaseContext(), "Eliminación Incorrecta", Toast.LENGTH_LONG).show();

                        }


                    }
                });

            }
        }).start();


    }

    public void restablecer(View v){


        cajaIdEliminar.setText("");


    }


}


