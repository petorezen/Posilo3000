package com.example.posilo3000.adapters;

import android.content.Context;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.posilo3000.R;
import com.example.posilo3000.models.Cvik;
import com.example.posilo3000.models.ZapisKalendar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdapterMojPlan extends RecyclerView.Adapter<AdapterMojPlan.MyViewHolder> {

    private Context context;
    private ArrayList<String> dni;
    private HashMap<String, Boolean> dni2;
    private ArrayList<ZapisKalendar> zapis;
    private OnItemClickListener mListener;
    private boolean isExpanded = false;
    private RecyclerView recyclerView;
    private AdapterNetusimCiSaTotoDa adapter;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public AdapterMojPlan(Context context, ArrayList<String> dni, HashMap<String, Boolean> dni2, ArrayList<ZapisKalendar> zapis) {
        this.context = context;
        this.dni = dni;
        this.zapis = zapis;
        this.dni2 = dni2;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.den_item, parent,false);

        return new MyViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {




        Map<String, Integer> pocetCvikov = new HashMap<>();
        pocetCvikov.put("PON",0);
        pocetCvikov.put("UT",0);
        pocetCvikov.put("STR",0);
        pocetCvikov.put("STV",0);
        pocetCvikov.put("PIA",0);
        pocetCvikov.put("SO",0);
        pocetCvikov.put("NE",0);


        for(ZapisKalendar z : zapis){

            pocetCvikov.put(z.getDen(), (pocetCvikov.get(z.getDen()))+1);

           // holder.txtNazovRozbalene.setText(z.getCvik().getNazov());
           // holder.txtCasRozbalene.setText(z.getCas());

        }

        for(ZapisKalendar zk : zapis){
            int pocet = pocetCvikov.get(holder.den.getText().toString());

            adapter = new AdapterNetusimCiSaTotoDa(context, zapis, pocet, holder.den.getText().toString());
            holder.rv.setAdapter(adapter);
            holder.rv.setLayoutManager(new LinearLayoutManager(context));

            holder.den.setText(dni.get(position));
            holder.pocetCvikov.setText(String.valueOf(pocetCvikov.get(dni.get(position))));


        }

        if(dni2.get(holder.den.getText().toString())){
           TransitionManager.beginDelayedTransition(holder.parent);
            holder.expandedRelLayout.setVisibility(View.VISIBLE);
            if(Integer.valueOf(holder.pocetCvikov.getText().toString()) == 0){
                holder.expandedRelLayout.setVisibility(View.GONE);
            }
        }
        else if(!dni2.get(holder.den.getText().toString())){
            holder.expandedRelLayout.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return dni.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView den, pocetCvikov, txtNazovRozbalene, txtCasRozbalene;
        RelativeLayout expandedRelLayout;
        Button btnZrusit;
        RecyclerView rv;
        CardView parent;

        public MyViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            rv = itemView.findViewById(R.id.recViewExpanded);
            den = itemView.findViewById(R.id.txtDen);
            pocetCvikov = itemView.findViewById(R.id.txtPocetCvikov);
            expandedRelLayout = itemView.findViewById(R.id.expandedRelLay);
            parent = itemView.findViewById(R.id.parent);
//            txtNazovRozbalene = itemView.findViewById(R.id.txtNazovRozbalene);
//            txtCasRozbalene = itemView.findViewById(R.id.txtCasRozbalene);
//            btnZrusit = itemView.findViewById(R.id.btnZrusit);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  /*  if(listener != null){

                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                            //listener.onItemClick(position);
                            notifyItemChanged(getAdapterPosition());
                        }
                    }*/
                    int position = getAdapterPosition();
                    if(!dni2.get(dni.get(position))){
                        dni2.put(dni.get(position), true);

                    }
                    else if(dni2.get(dni.get(position))){
                        dni2.put(dni.get(position), false);

                    }
                    notifyItemChanged(position);
                }
            });
        }
    }
}
