package me.disto.distoapp.ui.modulo_configuracion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import me.disto.distoapp.R;

public class RegistroPalabrasProblematicas extends AppCompatActivity  implements OnPalabrasMapAdapterListener {

    RecyclerView recyclerView;
    PalabrasMapAdapter adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_palabras_problematicas);
        recyclerView = findViewById(R.id.recycler_palabras_problematicas);
        TaskObtenerPalabras task = new TaskObtenerPalabras(this);
        task.execute();
    }

    @Override
    public void onPalabrasMapAdapterListener(PalabrasMapAdapter adapter) {
        adaptador = adapter;
        recyclerView.setAdapter(adaptador);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}