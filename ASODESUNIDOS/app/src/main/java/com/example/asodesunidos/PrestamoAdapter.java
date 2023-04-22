package com.example.asodesunidos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PrestamoAdapter extends RecyclerView.Adapter<PrestamoAdapter.ViewHolder> {
   Context context;
   List<PrestamoModel> prestamos;
    public static final String PRESTAMO = "PrestamoAdapter.PRESTAMO";

    public PrestamoAdapter(Context context, List<PrestamoModel> prestamos) {
        this.context = context;
        this.prestamos = prestamos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fila_tabla_prestamos, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PrestamoAdapter.ViewHolder holder, int position) {
        if(prestamos != null && prestamos.size() > 0){
            PrestamoModel model = prestamos.get(position);
            holder.tvId.setText(model.getId());
            holder.tvTipo.setText(tipoPrestamo(model.getTipo()));
            holder.tvMonto.setText(model.getMonto());
            holder.tvSaldo.setText(model.getSaldo());
            if(position % 2 != 0){
                holder.trRecycler.setBackgroundColor(ContextCompat.getColor(context, R.color.spinnerbg));
            }
            holder.imgVer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, VerPrestamo.class);
                    intent.putExtra(PRESTAMO, holder.tvId.getText().toString());
                    context.startActivity(intent);
                }
            });
        }
    }

    public static String tipoPrestamo(String tipo){
        switch (tipo){
            case "1":{
                return "Hipotecario";
            }
            case "2":{
                return "Educación";
            }
            case "3":{
                return "Personal";
            }
            case "4":{
                return "Viajes";
            }
        }
        return "";
    }

    @Override
    public int getItemCount() {
        return prestamos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvMonto, tvTipo,  tvSaldo;
        ImageView imgVer;
        TableRow trRecycler;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvIdPrestamo);
            tvMonto = itemView.findViewById(R.id.tvMontoPrestamo);
            tvTipo = itemView.findViewById(R.id.tvTipoPrestamo);
            tvSaldo = itemView.findViewById(R.id.tvSaldoPrestamo);
            imgVer = itemView.findViewById(R.id.btnVer);
            trRecycler = itemView.findViewById(R.id.trRecycler);
        }
    }


}
