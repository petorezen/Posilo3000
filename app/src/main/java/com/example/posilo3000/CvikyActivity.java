package com.example.posilo3000;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Toast;

import com.example.posilo3000.adapters.Adapter;
import com.example.posilo3000.models.Cvik;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CvikyActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Adapter adapter;
    ArrayList<Cvik> cviky;


    private static String ip = "25.90.113.29";
    private static String port = "1433;";
    private static String Classes = "net.sourceforge.jtds.jdbc.Driver";
    private static String database = "CvicmeApp";
    private static String username = "test";
    private static String password = "12345";
   // private static String url = "jdbc:sqlserver://"+ip+":"+port+"/"+database;
    private static String url = "jdbc:jtds:sqlserver://25.90.113.29;database=CvicmeApp;user=test;password=12345;";

    private Connection connection = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cviky);

        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);

        boolean local = sharedPreferences.getBoolean("LOCAL", false);

        recyclerView = findViewById(R.id.recyclerView);

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //if(!local){
            try {
                Class.forName(Classes);
                // connection = DriverManager.getConnection(url, username, password);
                connection = DriverManager.getConnection(url);
                Toast.makeText(this, "SUCCESS", Toast.LENGTH_SHORT).show();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
            } catch (SQLException e) {
                e.printStackTrace();
                Toast.makeText(this, "FAILURE", Toast.LENGTH_SHORT).show();
            }
    //    }


        cviky = new ArrayList<>();
        if(connection != null){

            Statement statement = null;
            try {
                statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT IdCviku, NazovCviku, Popis, Kategoria FROM  [CvicmeApp].[dbo].Cviky");
                while(resultSet.next()){
                    Cvik cvik = new Cvik(resultSet.getString(2), resultSet.getString(4), resultSet.getString(3), resultSet.getInt(1));
                    cviky.add(cvik);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else{
             Cvik cvik1 = new Cvik("Brusaky","Brucho", "Toto je cvik brusak", 0);
             Cvik cvik2 = new Cvik("Kliky","Prsia", "Toto je cvik klik", 1);


             cviky.add(cvik1);
             cviky.add(cvik2);
        }



        adapter = new Adapter(this, cviky);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(CvikyActivity.this, cviky.get(position).getNazov().toString(), Toast.LENGTH_SHORT).show();

                String nazov = cviky.get(position).getNazov();
                String popis = cviky.get(position).getPopis();
                int id = cviky.get(position).getCvikId();

                Intent intent = new Intent(getApplicationContext(), CvikInfoActivity.class);
                intent.putExtra("nazov", nazov);
                intent.putExtra("popis", popis);
                intent.putExtra("id", id);
                startActivity(intent);

            }
        });

        adapter.notifyDataSetChanged();

    }
}