package co.edu.unipiloto.pgc.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.R;
import co.edu.unipiloto.pgc.model.Movement;

public class MovementAdapter extends BaseAdapter {

    private ArrayList<Movement> movements;
    private Context context;

    public MovementAdapter(Context context, ArrayList<Movement> movements) {
        this.context = context;
        this.movements = movements;
    }

    @Override
    public int getCount() {
        return movements.size();
    }

    @Override
    public Object getItem(int position) {
        return movements.get(position);
    }

    @Override
    public long getItemId(int position) {
        return movements.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.movement_item, parent, false);
        }

        TextView viewMovimiento = convertView.findViewById(R.id.viewMovimiento);
        TextView viewTipo = convertView.findViewById(R.id.viewTipo);
        TextView viewCombustible = convertView.findViewById(R.id.viewCombustible);
        TextView viewCantidad = convertView.findViewById(R.id.viewCantidad);
        TextView viewTotal = convertView.findViewById(R.id.viewTotal);
        TextView viewFecha = convertView.findViewById(R.id.viewFecha);
        TextView viewEstacion = convertView.findViewById(R.id.viewEstacion);
        TextView viewUsuario = convertView.findViewById(R.id.viewUsuario);
        TextView viewTipoMovimiento = convertView.findViewById(R.id.viewTipoMovimiento);

        Movement m = movements.get(position);

        viewMovimiento.setText("Movimiento " + m.getId());
        viewTipo.setText("Tipo: " + (m.getTipoVehiculo() != null ? m.getTipoVehiculo() : "N/A"));
        viewCombustible.setText("Combustible: " + m.getCombustible().getNombre());
        viewCantidad.setText("Cantidad: " + m.getCantidad());
        viewTotal.setText("Total: " + (m.getTotal() != null ? m.getTotal() : "N/A"));
        viewFecha.setText("Fecha: " + m.getFecha());
        viewEstacion.setText("Estacion: " + m.getEstacion().getUsername());
        viewUsuario.setText("Usuario: " + (m.getUsuario() != null ? m.getUsuario().getUsername() : "N/A"));
        viewTipoMovimiento.setText("Tipo de movimiento: " + m.getTipoMovimiento());
        return convertView;
    }

    public void updateList(ArrayList<Movement> newList) {
        this.movements = newList;
        notifyDataSetChanged();
    }
}
