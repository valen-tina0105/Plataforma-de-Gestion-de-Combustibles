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
import co.edu.unipiloto.pgc.model.Movement;

public class MovementsAdapter extends RecyclerView.Adapter<MovementsAdapter.MovementsViewHolder> {

    private ArrayList<Movement> movements;

    public MovementsAdapter(ArrayList<Movement> movements) {
        this.movements = movements;
    }

    @NonNull
    @Override
    public MovementsAdapter.MovementsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movement_item,parent,false);
        return new MovementsViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MovementsAdapter.MovementsViewHolder holder, int position) {
        holder.viewMovimiento.setText("Movimiento " + movements.get(position).getId());
        holder.viewTipo.setText("Tipo: " + movements.get(position).getTipo());
        holder.viewCantidad.setText("Cantidad: " + movements.get(position).getCantidad());
        holder.viewTotal.setText("Total: " + movements.get(position).getTotal());
        holder.viewFecha.setText("Fecha: " + movements.get(position).getFecha());
        holder.viewEstacion.setText("Estacion: " + movements.get(position).getEstacion().getUsername());
        holder.viewTipoMovimiento.setText("Tipo de movimiento: " + movements.get(position).getTipoMovimiento());
    }

    @Override
    public int getItemCount() {
        return movements.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(ArrayList<Movement> newList) {
        this.movements = newList;
        notifyDataSetChanged();
    }

    public class MovementsViewHolder extends RecyclerView.ViewHolder {

        TextView viewMovimiento, viewTipo, viewCantidad, viewTotal, viewFecha, viewEstacion, viewTipoMovimiento;
        public MovementsViewHolder(@NonNull View itemView) {
            super(itemView);
            viewMovimiento = itemView.findViewById(R.id.viewMovimiento);
            viewTipo = itemView.findViewById(R.id.viewTipo);
            viewCantidad = itemView.findViewById(R.id.viewCantidad);
            viewTotal = itemView.findViewById(R.id.viewTotal);
            viewFecha = itemView.findViewById(R.id.viewFecha);
            viewEstacion = itemView.findViewById(R.id.viewEstacion);
            viewTipoMovimiento = itemView.findViewById(R.id.viewTipoMovimiento);
        }
    }
}
