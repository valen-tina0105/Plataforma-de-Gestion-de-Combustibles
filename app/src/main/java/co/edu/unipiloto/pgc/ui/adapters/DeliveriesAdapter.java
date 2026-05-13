package co.edu.unipiloto.pgc.ui.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.R;
import co.edu.unipiloto.pgc.dao.DeliveryDAO;
import co.edu.unipiloto.pgc.model.Delivery;

public class DeliveriesAdapter extends RecyclerView.Adapter<DeliveriesAdapter.DeliveriesViewHolder> {

    private ArrayList<Delivery> deliveries;
    private DeliveryDAO deliveryDAO;
    private Runnable reloadCallback;

    public DeliveriesAdapter(ArrayList<Delivery> deliveries) {
        this.deliveries = deliveries;
    }

    public DeliveriesAdapter(ArrayList<Delivery> deliveries, DeliveryDAO dao, Runnable reloadCallback) {
        this.deliveries = deliveries;
        this.deliveryDAO = dao;
        this.reloadCallback = reloadCallback;
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
        Delivery delivery = deliveries.get(position);
        holder.viewEntrega.setText("Entrega #" + delivery.getId());
        holder.viewPlaca.setText("Placa: " + delivery.getPlaca());
        holder.viewFecha.setText("Fecha: " + delivery.getFechaFormateada());
        holder.viewCombustible.setText("Combustible: " + delivery.getCombustible().getNombre());
        holder.viewCantidad.setText("Cantidad: " + delivery.getCantidad());
        holder.viewEstacionDestino.setText("Estacion Destino: " + delivery.getEstacion().getUsername());
        holder.viewDistribuidor.setText("Distribuido por: " + delivery.getDistribuidor().getUsername());
        holder.viewEstado.setText("Estado: " + delivery.getEstado());

        if ("PENDIENTE".equals(delivery.getEstado())) {
            holder.btnRealizarEntrega.setVisibility(View.VISIBLE);
        } else {
            holder.btnRealizarEntrega.setVisibility(View.GONE);
        }

        holder.btnRealizarEntrega.setOnClickListener(v -> showDeliveryDialog(v.getContext(), delivery));
    }

    private void showDeliveryDialog(Context context, Delivery delivery) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Realizar Entrega");
        builder.setMessage("Ingrese la placa del camión");

        final EditText input = new EditText(context);
        input.setHint("Placa del vehículo");
        builder.setView(input);

        builder.setPositiveButton("Confirmar Entrega", (dialog, which) -> {
            String placa = input.getText().toString().trim();
            if (placa.isEmpty()) {
                Toast.makeText(context, "La placa es obligatoria", Toast.LENGTH_SHORT).show();
                return;
            }
            if (deliveryDAO != null) {
                deliveryDAO.markAsDelivered(delivery.getId(), placa);
                Toast.makeText(context, "Entrega realizada. Estado: ENTREGADO", Toast.LENGTH_SHORT).show();
                if (reloadCallback != null) {
                    reloadCallback.run();
                }
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
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
        TextView viewEntrega, viewPlaca, viewFecha, viewCombustible, viewCantidad, viewEstacionDestino, viewDistribuidor, viewEstado;
        Button btnRealizarEntrega;

        public DeliveriesViewHolder(@NonNull View itemView) {
            super(itemView);
            viewEntrega = itemView.findViewById(R.id.viewEntrega);
            viewPlaca = itemView.findViewById(R.id.viewPlaca);
            viewFecha = itemView.findViewById(R.id.viewFecha);
            viewCombustible = itemView.findViewById(R.id.viewCombustible);
            viewCantidad = itemView.findViewById(R.id.viewCantidad);
            viewEstacionDestino = itemView.findViewById(R.id.viewEstacionDestino);
            viewDistribuidor = itemView.findViewById(R.id.viewDistribuidor);
            viewEstado = itemView.findViewById(R.id.viewEstado);
            btnRealizarEntrega = itemView.findViewById(R.id.btnRealizarEntrega);
        }
    }
}
