package co.edu.unipiloto.pgc.ui.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.R;
import co.edu.unipiloto.pgc.model.Inventory;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.InventoryViewHolder> {
    private ArrayList<Inventory> inventories;
    public InventoryAdapter(ArrayList<Inventory> inventories) {
        this.inventories = inventories;
    }

    @NonNull
    @Override
    public InventoryAdapter.InventoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inventory_item, parent, false);
        return new InventoryViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull InventoryAdapter.InventoryViewHolder holder, int position) {
        holder.viewInventario.setText("Inventario " + (position + 1));
        holder.viewEstacion.setText("Estacion: " + inventories.get(position).getOwnerUsername());
        holder.viewCombustible.setText("Tipo de combustible: " + inventories.get(position).getCombustible().getNombre());
        holder.viewCantidadCombustible.setText("Cantidad de combustible: " + inventories.get(position).getCantidadActual());
        holder.viewCapacidadMaxima.setText("Capacidad máxima: " + inventories.get(position).getCapacidadMaxima());
        holder.viewNivelMinimo.setText("Nivel minimo: " + inventories.get(position).getNivelMinimo());
    }

    @Override
    public int getItemCount() {
        return inventories.size();
    }

    public class InventoryViewHolder extends RecyclerView.ViewHolder{
        TextView viewInventario, viewEstacion, viewCombustible, viewCantidadCombustible, viewCapacidadMaxima, viewNivelMinimo;
        public InventoryViewHolder(@NonNull View itemView) {
            super(itemView);
            viewInventario = itemView.findViewById(R.id.viewInventario);
            viewEstacion = itemView.findViewById(R.id.viewEstacion);
            viewCombustible = itemView.findViewById(R.id.viewCombustible);
            viewCantidadCombustible = itemView.findViewById(R.id.viewCantidadCombustible);
            viewCapacidadMaxima = itemView.findViewById(R.id.viewCapacidadMaxima);
            viewNivelMinimo = itemView.findViewById(R.id.viewNivelMinimo);
        }
    }
}
