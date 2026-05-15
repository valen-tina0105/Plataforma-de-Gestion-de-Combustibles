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
import co.edu.unipiloto.pgc.model.Rule;
import co.edu.unipiloto.pgc.model.Transaction;

public class RulesAdapter extends RecyclerView.Adapter<RulesAdapter.RuleViewHolder> {
    private ArrayList<Rule> rules;
    public RulesAdapter(ArrayList<Rule> rules) {
        this.rules = rules;
    }

    @NonNull
    @Override
    public RulesAdapter.RuleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rules_item, parent, false);
        return new RuleViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RulesAdapter.RuleViewHolder holder, int position) {
        holder.viewRegla.setText("Regla: " + rules.get(position).getId());
        holder.viewTipoVehiculo.setText("Tipo de vehiculo: " + rules.get(position).getTipoVehiculo());
        holder.viewPrecio.setText("Precio: " + rules.get(position).getPrecio());
        holder.viewCreadoPor.setText("Creado por: " + rules.get(position).getAdminUsername());
        holder.viewFecha.setText("Fecha: " + rules.get(position).getFechaFormateada());
    }

    @Override
    public int getItemCount() {
        return rules.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(ArrayList<Rule> newList) {
        this.rules = newList;
        notifyDataSetChanged();
    }

    public class RuleViewHolder extends RecyclerView.ViewHolder{
        TextView viewRegla, viewTipoVehiculo, viewPrecio,viewCreadoPor, viewFecha;
        public RuleViewHolder(@NonNull View itemView) {
            super(itemView);
            viewRegla = itemView.findViewById(R.id.viewRegla);
            viewTipoVehiculo = itemView.findViewById(R.id.viewTipoVehiculo);
            viewPrecio = itemView.findViewById(R.id.viewPrecio);
            viewCreadoPor = itemView.findViewById(R.id.viewCreadoPor);
            viewFecha = itemView.findViewById(R.id.viewFecha);
        }
    }
}
