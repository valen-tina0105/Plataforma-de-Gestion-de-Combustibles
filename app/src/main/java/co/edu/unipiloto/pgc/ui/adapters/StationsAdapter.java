package co.edu.unipiloto.pgc.ui.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import co.edu.unipiloto.pgc.R;
import co.edu.unipiloto.pgc.model.Price;
import co.edu.unipiloto.pgc.model.Station;
import co.edu.unipiloto.pgc.model.User;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class StationsAdapter extends RecyclerView.Adapter<StationsAdapter.StationViewHolder> {
    private ArrayList<Station> stations;
    private Context context;

    public StationsAdapter(Context context, ArrayList<Station> stations) {
        this.context = context;
        this.stations = stations;
    }

    @NonNull
    @Override
    public StationsAdapter.StationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.price_item, parent, false);
        return new StationsAdapter.StationViewHolder(view);
    }

    @Override
    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    public void onBindViewHolder(@NonNull StationViewHolder holder, int position) {

        holder.viewEstacion.setText("Estacion: " + stations.get(position).getNombre());
        holder.viewDireccion.setText("Dirección: " + stations.get(position).getDireccion());

        double distancia = stations.get(position).getDistancia();
        holder.viewDistancia.setText("Distancia: " + String.format("%.2f", distancia) + " km");

        NumberFormat formatoCOP = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));

        holder.viewCorriente.setText("Corriente: " + formatoCOP.format(stations.get(position).getPrecioCorriente()));
        holder.viewExtra.setText("Extra: " + formatoCOP.format(stations.get(position).getPrecioExtra()));
        holder.viewDiesel.setText("Diesel: " + formatoCOP.format(stations.get(position).getPrecioDiesel()));
        holder.viewGnv.setText("GNV: " + formatoCOP.format(stations.get(position).getPrecioGNV()));

        holder.itemView.setOnClickListener(v -> {
            Station station = stations.get(position);

            Uri uri = Uri.parse(
                    "https://www.google.com/maps/dir/?api=1" +
                            "&destination=" +
                            station.getLatitud() + "," +
                            station.getLongitud()
            );

            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setPackage("com.google.android.apps.maps");

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return  stations.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(ArrayList<Station> newList) {
        this. stations = newList;
        notifyDataSetChanged();
    }

    private void openRoute(Station station) {
        double lat = station.getLatitud();
        double lng = station.getLongitud();

        Uri uri = Uri.parse(
                "https://www.google.com/maps/dir/?api=1" +
                        "&destination=" + lat + "," + lng +
                        "&travelmode=driving"
        );

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setPackage("com.google.android.apps.maps");

        context.startActivity(intent);
    }

    public class StationViewHolder extends RecyclerView.ViewHolder {
        TextView viewEstacion,viewDireccion, viewDistancia, viewCorriente, viewExtra, viewDiesel, viewGnv;

        public StationViewHolder(@NonNull View itemView) {
            super(itemView);
            viewEstacion = itemView.findViewById(R.id.viewEstacion);
            viewDireccion = itemView.findViewById(R.id.viewDireccion);
            viewDistancia = itemView.findViewById(R.id.viewDistancia);
            viewCorriente = itemView.findViewById(R.id.viewCorriente);
            viewExtra = itemView.findViewById(R.id.viewExtra);
            viewDiesel = itemView.findViewById(R.id.viewDiesel);
            viewGnv = itemView.findViewById(R.id.viewGnv);
        }
    }
}
    
