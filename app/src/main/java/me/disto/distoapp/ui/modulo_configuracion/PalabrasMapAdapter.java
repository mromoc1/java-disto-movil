package me.disto.distoapp.ui.modulo_configuracion;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

import me.disto.distoapp.R;

/**
 * provee vistas para el RecyclerView con datos desde mData.
 */
public class PalabrasMapAdapter extends RecyclerView.Adapter<PalabrasMapAdapter.ViewHolder> {

    private final Map<String, Integer> mData;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView mKeyTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mKeyTextView = (TextView) itemView.findViewById(R.id.textView_helio);
        }

        public TextView getKeyTextView() {
            return mKeyTextView;
        }
    }

    public PalabrasMapAdapter(Map<String, Integer> data) {

        Map<String,Integer> data2 = new HashMap<>();

        for(String key : data.keySet()){
            if(data.get(key) == 1){
                data2.put(key, data.get(key));
            }
        }
        mData = data2;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //crea una nueva vista
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.text_row_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //reemplaza el contenido de una vista (invocado por el administrador de diseño)
        holder.getKeyTextView().setText(mData.keySet().toArray()[position].toString());
        holder.getKeyTextView().setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle("Gestión de palabras problemáticas dice:")
                    .setMessage("¿Desea eliminar la palabra " + holder.getKeyTextView().getText().toString() + " ?")
                    .setPositiveButton("Aceptar", (dialog, id) -> {
                        // Acción que se realizará al pulsar el botón Aceptar
                        ObjectMapper objectMapper = new ObjectMapper();
                        try {
                            // Convierte el Map a JSON
                            String json_palabras_clasificadas = objectMapper.writeValueAsString("{'valor':1}");
                            //Log.d("JSON", json_palabras_clasificadas);
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(v.getContext(), "Procesamiento de lectura finalizado", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancelar", (dialog, id) -> {
                        // Acción que se realizará al pulsar el botón Cancelar
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

}
