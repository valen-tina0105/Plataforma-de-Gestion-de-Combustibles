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
import co.edu.unipiloto.pgc.model.Transaction;

public class PurchaseAdapter extends RecyclerView.Adapter<PurchaseAdapter.PurchaseViewHolder> {
    private ArrayList<Transaction> transactions;
    public PurchaseAdapter(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public PurchaseAdapter.PurchaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.purchase_item, parent, false);
        return new PurchaseViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PurchaseAdapter.PurchaseViewHolder holder, int position) {
        holder.viewCompra.setText("Compra " + (position + 1));
        holder.viewTipoVehiculo.setText("Tipo de vehiculo: " + transactions.get(position).getTipoVehiculo());
        holder.viewCombustible.setText("Tipo de combustible: " + transactions.get(position).getCombustible());
        holder.viewCantidad.setText("Cantidad: " + transactions.get(position).getCantidad());
        holder.viewTotal.setText("Total: " + transactions.get(position).getTotal());
        holder.viewFecha.setText("Fecha: " + transactions.get(position).getFechaFormateada());
        holder.viewEstacion.setText("Estacion: " + transactions.get(position).getEstacion().getUsername());
        holder.viewUsuario.setText("Usuario: " + transactions.get(position).getUsuario().getUsername());
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(ArrayList<Transaction> newList) {
        this.transactions = newList;
        notifyDataSetChanged();
    }

    public class PurchaseViewHolder extends RecyclerView.ViewHolder{
        TextView viewCompra, viewTipoVehiculo, viewEstacion, viewCantidad, viewTotal, viewFecha, viewCombustible, viewUsuario;
        public PurchaseViewHolder(@NonNull View itemView) {
            super(itemView);
            viewCompra = itemView.findViewById(R.id.viewCompra);
            viewTipoVehiculo = itemView.findViewById(R.id.viewTipoVehiculo);
            viewCombustible = itemView.findViewById(R.id.viewCombustible);
            viewCantidad = itemView.findViewById(R.id.viewCantidad);
            viewTotal = itemView.findViewById(R.id.viewTotal);
            viewFecha = itemView.findViewById(R.id.viewFecha);
            viewEstacion = itemView.findViewById(R.id.viewEstacion);
            viewUsuario = itemView.findViewById(R.id.viewUsuario);
        }
    }
}
