package me.disto.distoapp.ui.modulo_configuracion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.Arrays;
import java.util.List;

import me.disto.distoapp.R;

public class RegistroPalabrasProblematicas extends AppCompatActivity {

    RecyclerView recyclerView;
    private int currentPage = 1;
    private boolean isLoading = false;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        linearLayoutManager = new LinearLayoutManager(this);
        setContentView(R.layout.activity_registro_palabras_problematicas);
        recyclerView = findViewById(R.id.recycler_palabras_problematicas);
        PagingConfig pagingConfig = new PagingConfig(
                /* pageSize */ 10, // Cantidad de elementos por página
                /* prefetchDistance */ 5, // Cantidad de elementos adicionales a cargar
                /* enablePlaceholders */ false,
                /* initialLoadSize */ 20, // Cantidad de elementos que se cargarán al inicio
                /* maxSize */ 100, // Tamaño máximo de la lista (opcional)
                /* jumpThreshold */ PagingConfig.MAX_SIZE_UNBOUNDED // Limite de salto (opcional)
        );
        List<String> palabras = Arrays.asList("Hola", "Mundo", "Android", "Java", "RecyclerView","Hola", "Mundo", "Android", "Java", "RecyclerView"
        ,"Hola", "Mundo", "Android", "Java", "RecyclerView","Hola", "Mundo", "Android", "Java", "RecyclerView",
                "Hola", "Mundo", "Android", "Java", "RecyclerView","Hola", "Mundo", "Android", "Java", "RecyclerView");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        PalabrasAdapter adapter = new PalabrasAdapter(palabras);
        recyclerView.setAdapter(adapter);
    }

}