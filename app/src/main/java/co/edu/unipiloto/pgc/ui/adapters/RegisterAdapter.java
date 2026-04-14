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
import co.edu.unipiloto.pgc.model.Register;

public class RegisterAdapter extends RecyclerView.Adapter<RegisterAdapter.RegistersViewHolder> {

    private ArrayList<Register> registers;

    public RegisterAdapter(ArrayList<Register> registers) {
        this.registers = registers;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(ArrayList<Register> newList) {
        this.registers = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RegistersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.register_item, parent, false);
        return new RegistersViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RegistersViewHolder holder, int position) {
        holder.viewRegistro.setText("Registro " + (position + 1));
        holder.viewEstacion.setText("Estacion: " + registers.get(position).getEstacion().getUsername());
        holder.viewCombustible.setText("Tipo de combustible: " + registers.get(position).getCombustible().getNombre());
        holder.viewCantidad.setText("Cantidad: " + registers.get(position).getCantidad());
        holder.viewFecha.setText("Fecha: " + registers.get(position).getFechaFormateada());
    }

    @Override
    public int getItemCount() {
        return registers.size();
    }

    public class RegistersViewHolder extends RecyclerView.ViewHolder {

        TextView viewRegistro, viewEstacion, viewCombustible, viewCantidad, viewFecha;

        public RegistersViewHolder(@NonNull View itemView) {
            super(itemView);
            viewRegistro = itemView.findViewById(R.id.viewRegistro);
            viewEstacion = itemView.findViewById(R.id.viewEstacion);
            viewCombustible = itemView.findViewById(R.id.viewCombustible);
            viewCantidad = itemView.findViewById(R.id.viewCantidad);
            viewFecha = itemView.findViewById(R.id.viewFecha);
        }
    }
}


