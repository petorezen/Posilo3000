package com.example.posilo3000;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.posilo3000.adapters.AdapterMojPlan;
import com.example.posilo3000.models.Cvik;
import com.example.posilo3000.models.ZapisKalendar;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MojPlanActivity extends AppCompatActivity {


    private ExpandableListView expandableListView;
    private RecyclerView recyclerView;
    private AdapterMojPlan adapter;
    private ArrayList<String> dni;
    private HashMap<String, List<String>> listItem;
    private HashMap<String, Boolean> dni2;
    private boolean isExpanded = false;

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
        setContentView(R.layout.activity_moj_plan);

        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);

        boolean local = sharedPreferences.getBoolean("LOCAL", false);

        expandableListView = findViewById(R.id.expandableLV);
        listItem = new HashMap<>();

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

       // if(!local){
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
     //   }


        recyclerView = findViewById(R.id.recyclerViewPlan);

        dni = new ArrayList<>();
        dni.add("PON");
        dni.add("UT");
        dni.add("STR");
        dni.add("STV");
        dni.add("PIA");
        dni.add("SO");
        dni.add("NE");

        dni2 = new HashMap<>();

        dni2.put("PON", false);
        dni2.put("UT", false);
        dni2.put("STR", false);
        dni2.put("STV", false);
        dni2.put("PIA", false);
        dni2.put("SO", false);
        dni2.put("NE", false);

        ArrayList<ZapisKalendar> kalendar = new ArrayList<>();

        if(connection != null){

            Statement statement = null;
            try {
                statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT NazovDen, tb1.IdCviku, CasCviku, NazovCviku, Kategoria, popis FROM [CvicmeApp].[dbo].[Kalendar] tb1 LEFT JOIN [CvicmeApp].[dbo].[Cviky] tb2 ON tb2.IdCviku = tb1.IdCviku");
                while(resultSet.next()){


                   ZapisKalendar zapis = new ZapisKalendar(resultSet.getString("NazovDen"),resultSet.getString("CasCviku"),new Cvik(resultSet.getString("NazovCviku"), resultSet.getString("Kategoria"), resultSet.getString("popis"), resultSet.getInt("IdCviku"))  );
                    kalendar.add(zapis);

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else{

            int pocet = sharedPreferences.getInt("POCET", -1);

            for(int j = 0; j<=pocet; j++){
                int id = sharedPreferences.getInt("ID"+String.valueOf(j), 1000);

                ZapisKalendar z = new ZapisKalendar(sharedPreferences.getString("DEN"+String.valueOf(j), ""), sharedPreferences.getString("CAS"+String.valueOf(j), ""), new Cvik(sharedPreferences.getString("NAZOV"+String.valueOf(j), ""), "nie je", sharedPreferences.getString("POPIS"+String.valueOf(j),""), sharedPreferences.getInt("ID"+String.valueOf(j), -1) ));
                kalendar.add(z);
            }



        }

        adapter = new AdapterMojPlan(this, dni, dni2, kalendar);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

       /* adapter.setOnItemClickListener(new AdapterMojPlan.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(MojPlanActivity.this, dni.get(position).toString(), Toast.LENGTH_SHORT).show();

                if(!dni2.get(dni.get(position))){
                    dni2.put(dni.get(position), true);
                }

                else if(dni2.get(dni.get(position))){
                    dni2.put(dni.get(position), false);
                }

            }
        });*/
        adapter.notifyDataSetChanged();
    }
}