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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        return new TransactionViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.TransactionViewHolder holder, int position) {
        holder.viewHistorial.setText("Transaccion " + (position + 1));
        holder.viewTipoVehiculo.setText("Tipo de vehiculo: " + transactions.get(position).getTipoVehiculo());
        holder.viewCombustible.setText("Tipo de combustible: " + transactions.get(position).getCombustible());
        holder.viewCantidad.setText("Cantidad: " + transactions.get(position).getCantidad());
        holder.viewTotal.setText("Total: " + transactions.get(position).getTotal());
        holder.viewFecha.setText("Fecha: " + transactions.get(position).getFechaFormateada());
        holder.viewEstacion.setText("Estacion: " + transactions.get(position).getEstacionUsername());
        holder.viewEstado.setText("Estado: " + transactions.get(position).getEstado());
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder{
        TextView viewHistorial, viewTipoVehiculo, viewCantidad, viewTotal, viewFecha, viewEstacion, viewEstado, viewCombustible;
        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            viewHistorial = itemView.findViewById(R.id.viewHistorial);
            viewTipoVehiculo = itemView.findViewById(R.id.viewTipoVehiculo);
            viewCombustible = itemView.findViewById(R.id.viewCombustible);
            viewCantidad = itemView.findViewById(R.id.viewCantidad);
            viewTotal = itemView.findViewById(R.id.viewTotal);
            viewFecha = itemView.findViewById(R.id.viewFecha);
            viewEstacion = itemView.findViewById(R.id.viewEstacion);
            viewEstado = itemView.findViewById(R.id.viewEstado);
        }
    }
}
