package com.example.posilo3000.adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.posilo3000.CvikyActivity;
import com.example.posilo3000.MojPlanActivity;
import com.example.posilo3000.R;
import com.example.posilo3000.models.Cvik;
import com.example.posilo3000.models.ZapisKalendar;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class AdapterNetusimCiSaTotoDa extends RecyclerView.Adapter<AdapterNetusimCiSaTotoDa.MyViewHolder> {

    private Context context;
    private ArrayList<String> dni;
    private HashMap<String, Boolean> dni2;
    private int pocet;
    private ArrayList<ZapisKalendar> zapis;
    private OnItemClickListener mListener;
    private String nazov;
    private Context contextPovodny;

    private static String ip = "25.90.113.29";
    private static String port = "1433;";
    private static String Classes = "net.sourceforge.jtds.jdbc.Driver";
    private static String database = "CvicmeApp";
    private static String username = "test";
    private static String password = "12345";
    // private static String url = "jdbc:sqlserver://"+ip+":"+port+"/"+database;
    private static String url = "jdbc:jtds:sqlserver://25.90.113.29;database=CvicmeApp;user=test;password=12345;";
    private Connection connection = null;

    public AdapterNetusimCiSaTotoDa( Context context, ArrayList<ZapisKalendar> zapis,/* ArrayList<String> dni, HashMap<String, Boolean> dni2,*/ int pocet, String nazov) {
        this.context = context;

//        this.dni = dni;
//        this.dni2 = dni2;
        this.zapis = zapis;
        this.pocet = pocet;
        this.nazov = nazov;

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
        View view = inflater.inflate(R.layout.den_items, parent,false);

        return new MyViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {


            int j = position;
        final HashMap <String, String> tmpNazov = new HashMap<>();
        final HashMap <String, String> tmpCas = new HashMap<>();
        HashMap<String, Integer> tmpId = new HashMap<>();
        int pocet = 0;
        for(ZapisKalendar z : zapis){

            if(z.getDen().equals(nazov)){
                tmpId.put(z.getCvik().getNazov(), z.getCvik().getCvikId());
                tmpNazov.put(nazov+String.valueOf(pocet), z.getCvik().getNazov());
                tmpCas.put(nazov+String.valueOf(pocet), z.getCas());
                pocet++;
            }
            else{
                pocet = 0;
            }
        }
            holder.txtNazovRozbalene.setText(tmpNazov.get(nazov+String.valueOf(position)));
            holder.txtCasRozbalene.setText(tmpCas.get(nazov+String.valueOf(position)));



        try {
            Class.forName(Classes);
            // connection = DriverManager.getConnection(url, username, password);
            connection = DriverManager.getConnection(url);
           // Toast.makeText(context, "SUCCESS", Toast.LENGTH_SHORT).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show();
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(context, "FAILURE", Toast.LENGTH_SHORT).show();
        }

        if(!holder.txtCasRozbalene.getText().toString().equals(null) || tmpId.get(holder.txtNazovRozbalene.getText().toString()) == null){

        }

        final String casDelete = holder.txtCasRozbalene.getText().toString();
        final int id = (tmpId.get(holder.txtNazovRozbalene.getText().toString())); /*)==null ? 1000 : tmpId.get(holder.txtNazovRozbalene.getText().toString());*/

            holder.btnZrusit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(connection != null){

                        Statement statement = null;
                        try {
                            statement = connection.createStatement();
                           // ResultSet resultSet =  statement.executeUpdate("DELETE INTO [CvicmeApp].[dbo].[Kalendar] VALUES ('"+s+"',"+id+",'"+cas+"')");
                            statement.executeUpdate("DELETE FROM CvicmeApp.dbo.Kalendar WHERE NazovDen='"+nazov+"' AND CasCviku='"+casDelete+"' AND IdCviku = "+id+" ");

//                           Intent intent = new Intent(context,MojPlanActivity.class);
//                           context.startActivity(intent);
                            tmpNazov.remove(nazov+String.valueOf(position));
                            tmpCas.remove(nazov+String.valueOf(position));

//                            for(int i = zapis.size(); i>0; i--){
//                                Object[] temp = zapis.toArray();
////                                if(zapis.getDen().equals(nazov) && z.getCas().equals(casDelete));
////                                {
////                                    zapis.remove(z);
////                                }
//                               ZapisKalendar s = new ZapisKalendar(temp[i]);
//
//                            }


//                            Iterator<ZapisKalendar> i = zapis.iterator();
//                            while (i.hasNext()) {
//                                ZapisKalendar s = i.next(); // must be called before you can call i.remove()
//                                // Do something
//                                if(s.getDen().equals(nazov) && s.getCas().equals(casDelete))
//                                i.remove();
//                            }


                           notifyItemRemoved(position);
                        notifyItemRangeChanged(position, getItemCount());
                         //   notifyDataSetChanged();
//                            Intent intent = new Intent(context,MojPlanActivity.class);
//                           context.startActivity(intent);



                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }

                }
            });
    }

    @Override
    public int getItemCount() {
        int pt = pocet;
        return pocet;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txtNazovRozbalene, txtCasRozbalene;
        Button btnZrusit;


        public MyViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            txtNazovRozbalene = itemView.findViewById(R.id.txtNazovRozbalene);
            txtCasRozbalene = itemView.findViewById(R.id.txtCasRozbalene);
            btnZrusit = itemView.findViewById(R.id.btnZrusit);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }
    }
}
