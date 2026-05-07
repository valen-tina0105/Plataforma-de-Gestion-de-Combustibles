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
import co.edu.unipiloto.pgc.model.Delivery;

public class DeliveriesAdapter extends RecyclerView.Adapter<DeliveriesAdapter.DeliveriesViewHolder> {

    private ArrayList<Delivery> deliveries;

    public DeliveriesAdapter(ArrayList<Delivery> deliveries) {
        this.deliveries = deliveries;
    }

    @NonNull
    @Override
    public DeliveriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.delivery_item, parent, false);
        return new DeliveriesViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DeliveriesViewHolder holder, int position) {
        holder.viewEntrega.setText("Entrega " + (position + 1));
        holder.viewPlaca.setText("Placa: " + deliveries.get(position).getPlaca());
        holder.viewFecha.setText("Fecha: " + deliveries.get(position).getFechaFormateada());
        holder.viewCombustible.setText("Combustible: " + deliveries.get(position).getCombustible().getNombre());
        holder.viewCantidad.setText("Cantidad: " + deliveries.get(position).getCantidad());
        holder.viewEstacionDestino.setText("Estacion Destino: " + deliveries.get(position).getEstacion().getUsername());
        holder.viewDistribuidor.setText("Distribuido por: " + deliveries.get(position).getDistribuidor().getUsername());
    }

    @Override
    public int getItemCount() {
        return deliveries.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(ArrayList<Delivery> newList) {
        this.deliveries = newList;
        notifyDataSetChanged();
    }

    public static class DeliveriesViewHolder extends RecyclerView.ViewHolder {

        TextView viewEntrega, viewPlaca, viewFecha, viewCombustible, viewCantidad, viewEstacionDestino, viewDistribuidor;

        public DeliveriesViewHolder(@NonNull View itemView) {
            super(itemView);
            viewEntrega = itemView.findViewById(R.id.viewEntrega);
            viewPlaca = itemView.findViewById(R.id.viewPlaca);
            viewFecha = itemView.findViewById(R.id.viewFecha);
            viewCombustible = itemView.findViewById(R.id.viewCombustible);
            viewCantidad = itemView.findViewById(R.id.viewCantidad);
            viewEstacionDestino = itemView.findViewById(R.id.viewEstacionDestino);
            viewDistribuidor = itemView.findViewById(R.id.viewDistribuidor);

        }
    }
}
