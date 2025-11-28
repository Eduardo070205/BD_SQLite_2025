package com.example.bd_sqlite_2025;


import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import db.EscuelaBD;
import entities.Alumno;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ActivityConsultas extends Activity {

    private RecyclerView recyclerView;

    private RecyclerView.Adapter adapter;

    private RecyclerView.LayoutManager layoutManager;

    ArrayList<Alumno> datos = null;

    EditText cajaFiltro;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultas);

        EscuelaBD bd = EscuelaBD.getAppDatabase(this);
        cajaFiltro = findViewById(R.id.cajaBuscar);
        cajaFiltro.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Toast.makeText(ActivityConsultas.this, cajaFiltro.getText().toString(), Toast.LENGTH_SHORT).show();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        datos = (ArrayList<Alumno>) bd.alumnoDAO().mostrarPorNombre(cajaFiltro.getText().toString().toUpperCase());

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
        });


        //=================================================

        recyclerView = findViewById(R.id.recycleAlumnos);
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



}

class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {


    private ArrayList<Alumno> localDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView textView;

        public ViewHolder(View view){

            super(view);

            textView = view.findViewById(R.id.textviewAlumnos);

        }

        public TextView getTextView(){return textView;}

    }

    public CustomAdapter(ArrayList<Alumno> dataset){

        localDataSet = dataset;

    }

    @NonNull
    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.textview_recycleview, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.ViewHolder holder, int position) {

        holder.getTextView().setText(localDataSet.get(position).toString());

    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }


}
