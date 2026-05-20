package co.edu.unipiloto.pgc.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.List;

import co.edu.unipiloto.pgc.R;
import co.edu.unipiloto.pgc.ui.models.OdometerReportItem;

public class OdometerReportAdapter extends RecyclerView.Adapter<OdometerReportAdapter.ViewHolder> {

    private final List<OdometerReportItem> items;
    private final DecimalFormat distanceFormat = new DecimalFormat("0.0");

    public OdometerReportAdapter(List<OdometerReportItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.odometer_report_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OdometerReportItem item = items.get(position);
        holder.textDelivery.setText("Entrega #" + item.getDeliveryId());
        holder.textDistance.setText(distanceFormat.format(item.getDistance()) + " m");
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textDelivery;
        private final TextView textDistance;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textDelivery = itemView.findViewById(R.id.textDeliveryId);
            textDistance = itemView.findViewById(R.id.textDeliveryDistance);
        }
    }
}
