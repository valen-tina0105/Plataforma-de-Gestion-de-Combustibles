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

    ArrayList<Delivery> deliveries;

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
        holder.viewEntrega.setText("Entrega " + deliveries.get(position).getId());
        holder.viewPlaca.setText("Placa: " + deliveries.get(position).getPlaca());
        holder.viewFecha.setText("Fecha: " + deliveries.get(position).getFechaFormateada());
        holder.viewTipoCombustible.setText("Fecha: " + deliveries.get(position).getTipoCombustible());
        holder.viewCantidad.setText("Cantidad: " + deliveries.get(position).getCantidad());
        holder.viewEstacionDestino.setText("Estacion Destino: " + deliveries.get(position).getEstacion().getUsername());
        holder.viewDistribuidor.setText("Distribuido por: " + deliveries.get(position).getDistribuidor().getUsername());
    }

    @Override
    public int getItemCount() {
        return deliveries.size();
    }

    public static class DeliveriesViewHolder extends RecyclerView.ViewHolder {

        TextView viewEntrega, viewPlaca, viewFecha, viewTipoCombustible, viewCantidad, viewEstacionDestino, viewDistribuidor;

        public DeliveriesViewHolder(@NonNull View itemView) {
            super(itemView);
            viewEntrega = itemView.findViewById(R.id.viewEntrega);
            viewPlaca = itemView.findViewById(R.id.viewPlaca);
            viewFecha = itemView.findViewById(R.id.viewFecha);
            viewTipoCombustible = itemView.findViewById(R.id.viewTipoCombustible);
            viewCantidad = itemView.findViewById(R.id.viewCantidad);
            viewEstacionDestino = itemView.findViewById(R.id.viewEstacionDestino);
            viewDistribuidor = itemView.findViewById(R.id.viewDistribuidor);

        }
    }
}
