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

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.TransactionViewHolder> {
    private ArrayList<Transaction> transactions;
    public HistoryAdapter(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public HistoryAdapter.TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_item, parent, false);
        return new TransactionViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.TransactionViewHolder holder, int position) {
        holder.viewTransaccion.setText("Transaccion " + transactions.get(position).getId());
        holder.viewTipoVehiculo.setText("Tipo de vehiculo: " + transactions.get(position).getTipoVehiculo());
        holder.viewCantidad.setText("Cantidad: " + transactions.get(position).getCantidad());
        holder.viewTotal.setText("Total: " + transactions.get(position).getTotal());
        holder.viewFecha.setText("Fecha: " + transactions.get(position).getFechaFormateada());
        holder.viewEstacion.setText("Estacion: " + transactions.get(position).getEstacion().getUsername());
        holder.viewEstado.setText("Estado: " + transactions.get(position).getEstado());
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


    public class TransactionViewHolder extends RecyclerView.ViewHolder{
        TextView viewTransaccion, viewTipoVehiculo, viewCantidad, viewTotal, viewFecha, viewEstacion, viewEstado;
        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            viewTransaccion = itemView.findViewById(R.id.viewTransaccion);
            viewTipoVehiculo = itemView.findViewById(R.id.viewTipoVehiculo);
            viewCantidad = itemView.findViewById(R.id.viewCantidad);
            viewTotal = itemView.findViewById(R.id.viewTotal);
            viewFecha = itemView.findViewById(R.id.viewFecha);
            viewEstacion = itemView.findViewById(R.id.viewEstacion);
            viewEstado = itemView.findViewById(R.id.viewEstado);
        }
    }
}
