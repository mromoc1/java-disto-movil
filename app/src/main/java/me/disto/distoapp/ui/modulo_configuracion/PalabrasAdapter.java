package me.disto.distoapp.ui.modulo_configuracion;

import android.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

import me.disto.distoapp.ui.modulo_aprendizaje_lectura.TaskSubirArchivoLectura;
import me.disto.distoapp.ui.utils.UserConfig;

public class PalabrasAdapter extends RecyclerView.Adapter<PalabrasAdapter.PalabraViewHolder>{
    private List<String> palabras;

    public PalabrasAdapter(List<String> palabras) {
        this.palabras = palabras;
    }
    @NonNull
    @Override
    public PalabrasAdapter.PalabraViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new PalabraViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull PalabrasAdapter.PalabraViewHolder holder, int position) {
        String palabra = palabras.get(position);
        holder.textPalabra.setText(palabra);
        holder.textPalabra.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle("Eliminar palabra problemática")
                    .setMessage("¿Desea eliminar la palabra: " + palabra + "?")
                    .setPositiveButton("Aceptar", (dialog, id) -> {
                        // Acción que se realizará al pulsar el botón Aceptar

                    })
                    .setNegativeButton("Cancelar", (dialog, id) -> {
                        // Acción que se realizará al pulsar el botón Cancelar
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return palabras.size();
    }
    public static class PalabraViewHolder extends RecyclerView.ViewHolder {
        public TextView textPalabra;

        public PalabraViewHolder(View itemView) {
            super(itemView);
            textPalabra = itemView.findViewById(android.R.id.text1);
        }
    }
}
