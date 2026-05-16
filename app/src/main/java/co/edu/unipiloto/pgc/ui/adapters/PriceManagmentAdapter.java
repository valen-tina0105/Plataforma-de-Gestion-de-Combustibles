package co.edu.unipiloto.pgc.ui.adapters;

import android.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.R;
import co.edu.unipiloto.pgc.dao.PriceDAO;
import co.edu.unipiloto.pgc.dto.UpdatePriceRequestDTO;
import co.edu.unipiloto.pgc.model.Price;

public class PriceManagmentAdapter extends RecyclerView.Adapter<PriceManagmentAdapter.PriceManagmentViewHolder> {
    private ArrayList<Price> prices;
    private PriceDAO priceDAO;
    public PriceManagmentAdapter(ArrayList<Price> prices, PriceDAO priceDAO) {
        this.prices = prices;
        this.priceDAO = priceDAO;
    }

    @NonNull
    @Override
    public PriceManagmentAdapter.PriceManagmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.price_managment_item, parent, false);
        return new PriceManagmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PriceManagmentAdapter.PriceManagmentViewHolder holder, int position) {
        Price price = prices.get(position);

        holder.tvFuelType.setText(price.getCombustible().getNombre());
        holder.tvFuelPrice.setText(String.valueOf(price.getPrecio()));

        holder.btnUpdatePrice.setOnClickListener(v -> {

            AlertDialog.Builder builder =
                    new AlertDialog.Builder(holder.itemView.getContext());

            builder.setTitle("Nuevo precio");

            final EditText input =
                    new EditText(holder.itemView.getContext());

            input.setHint("Ingrese el nuevo precio");

            input.setInputType(
                    InputType.TYPE_CLASS_NUMBER |
                            InputType.TYPE_NUMBER_FLAG_DECIMAL
            );

            input.setText(String.valueOf(price.getPrecio()));

            builder.setView(input);

            builder.setPositiveButton("Confirmar", (dialog, which) -> {

                String texto = input.getText().toString().trim();

                if(!texto.isEmpty()){

                    double newPrice = Double.parseDouble(texto);

                    priceDAO.updatePrice(
                            price.getId(),
                            new UpdatePriceRequestDTO(newPrice)
                    );
                    price.setPrecio(newPrice);

                    notifyItemChanged(holder.getAdapterPosition());
                }
            });

            builder.setNegativeButton("Cancelar",
                    (dialog, which) -> dialog.dismiss());

            builder.show();
        });
    }

    @Override
    public int getItemCount() {
        return prices.size();
    }

    public class PriceManagmentViewHolder extends RecyclerView.ViewHolder{

        TextView tvFuelType, tvFuelPrice;
        Button btnUpdatePrice;

        public PriceManagmentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFuelType = itemView.findViewById(R.id.tvFuelType);
            tvFuelPrice = itemView.findViewById(R.id.tvFuelPrice);
            btnUpdatePrice = itemView.findViewById(R.id.btnUpdatePrice);
        }
    }
}
