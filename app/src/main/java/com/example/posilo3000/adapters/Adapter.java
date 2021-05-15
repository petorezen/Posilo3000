package com.example.posilo3000.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.posilo3000.R;
import com.example.posilo3000.models.Cvik;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    private Context context;
    ArrayList<Cvik> cviky;
    private OnItemClickListener mListener;


    public Adapter(Context context, ArrayList<Cvik> cviky) {
        this.context = context;
        this.cviky = cviky;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_cvik, parent,false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Klik", Toast.LENGTH_SHORT).show();
            }
        });
        return new MyViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.nazov.setText(cviky.get(position).getNazov());
        holder.kategoria.setText(cviky.get(position).getKategoria());



    }

    @Override
    public int getItemCount() {
        return cviky.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nazov, kategoria;

        public MyViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            nazov = itemView.findViewById(R.id.txtNazovCviku);
            kategoria = itemView.findViewById(R.id.txtCategory);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);

                        }
                    }
                }
            });

        }
    }
}
