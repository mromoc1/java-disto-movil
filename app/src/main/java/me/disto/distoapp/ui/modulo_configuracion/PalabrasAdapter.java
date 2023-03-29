package me.disto.distoapp.ui.modulo_configuracion;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

public class PalabrasAdapter extends RecyclerView.Adapter<PalabrasAdapter.PalabraViewHolder>{
    /**
     * Called when RecyclerView needs a new  {ViewHolder} of the given type to represent
     * an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using
     * {onBindViewHolder(ViewHolder, int, List)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * #onBindViewHolder(ViewHolder, int)
     */

    private final List<String> palabras;

    public PalabrasAdapter(List<String> palabras) {
        this.palabras = palabras;
    }
    @NonNull
    @Override
    public PalabrasAdapter.PalabraViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new PalabraViewHolder(itemView);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the { ViewHolder#itemView} to reflect the item at the given
     * position.
     * <p>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use { ViewHolder#getBindingAdapterPosition()} which
     * will have the updated adapter position.
     * <p>
     * Override { #onBindViewHolder(ViewHolder, int, List)} instead if Adapter can
     * handle efficient partial bind.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
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
