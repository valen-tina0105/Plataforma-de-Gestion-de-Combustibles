package co.edu.unipiloto.pgc.ui.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.R;
import co.edu.unipiloto.pgc.dao.DeliveryDAO;
import co.edu.unipiloto.pgc.model.Delivery;
import co.edu.unipiloto.pgc.model.User;

public class ConfirmDeliveryAdapter extends RecyclerView.Adapter<ConfirmDeliveryAdapter.ViewHolder> {

    private ArrayList<Delivery> deliveries;
    private User user;
    private DeliveryDAO deliveryDAO;
    private Runnable reloadCallback;

    public ConfirmDeliveryAdapter(ArrayList<Delivery> deliveries, User user, DeliveryDAO dao, Runnable reloadCallback) {
        this.deliveries = deliveries;
        this.user = user;
        this.deliveryDAO = dao;
        this.reloadCallback = reloadCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_delivery_confirm, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Delivery delivery = deliveries.get(position);

        holder.txtPlaca.setText("Placa: " + delivery.getPlaca());
        holder.txtCombustible.setText("Combustible: " + delivery.getCombustible().getNombre());
        holder.txtCantidad.setText("Cantidad: " + delivery.getCantidad());
        holder.txtEstado.setText("Estado: " + delivery.getEstado());
        
        if ("ENTREGADO".equals(delivery.getEstado())) {
            holder.btnConfirmar.setVisibility(View.VISIBLE);
        } else {
            holder.btnConfirmar.setVisibility(View.GONE);
        }

        holder.btnConfirmar.setOnClickListener(v -> confirmDelivery(v.getContext(), delivery));
    }

    private void confirmDelivery(Context context, Delivery delivery) {
        new AlertDialog.Builder(context)
                .setTitle("Confirmar recepción")
                .setMessage("¿Deseas confirmar la recepción de esta entrega?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    try {
                        deliveryDAO.confirmDelivery(delivery.getId(), user.getId());
                        Toast.makeText(context, "Entrega confirmada", Toast.LENGTH_SHORT).show();
                        reloadCallback.run();
                    } catch (Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    public int getItemCount() {
        return deliveries.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtPlaca, txtCombustible, txtCantidad, txtEstado;
        Button btnConfirmar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPlaca = itemView.findViewById(R.id.txtPlaca);
            txtCombustible = itemView.findViewById(R.id.txtCombustible);
            txtCantidad = itemView.findViewById(R.id.txtCantidad);
            txtEstado = itemView.findViewById(R.id.txtEstado);
            btnConfirmar = itemView.findViewById(R.id.btnConfirmar);
        }
    }
}
