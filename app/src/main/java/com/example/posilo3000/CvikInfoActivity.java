package com.example.posilo3000;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.posilo3000.models.ZapisKalendar;

import net.sourceforge.jtds.jdbc.DateTime;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CvikInfoActivity extends AppCompatActivity {

    private Button btnPridat;
    private TextView txtPopis, txtNazov;

    private static String ip = "25.90.113.29";
    private static String port = "1433;";
    private static String Classes = "net.sourceforge.jtds.jdbc.Driver";
    private static String database = "CvicmeApp";
    private static String username = "test";
    private static String password = "12345";
    // private static String url = "jdbc:sqlserver://"+ip+":"+port+"/"+database;
    private static String url = "jdbc:jtds:sqlserver://25.90.113.29;database=CvicmeApp;user=test;password=12345;";

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";
    public static final String SWTICH1 = "switch1";

    private Connection connection = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cvik_info);


        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);

        boolean local = sharedPreferences.getBoolean("LOCAL", false);

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
      //  }





        txtNazov = findViewById(R.id.txtNazov);
        txtPopis = findViewById(R.id.txtPopis);
        btnPridat = findViewById(R.id.btnPridat);

        Intent intent = getIntent();
        final String nazov = intent.getStringExtra("nazov");
        final String popis = intent.getStringExtra("popis");
        final int id = intent.getIntExtra("id", 0);

        txtNazov.setText(nazov);
        txtPopis.setText(popis);

        final ArrayList<String> vybrateDni = new ArrayList<>();

        btnPridat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                LayoutInflater li = LayoutInflater.from(getApplicationContext());
                final View promptsView = li.inflate(R.layout.checkbox_den, null);
                final EditText hours = (EditText) promptsView.findViewById(R.id.editHours);
                final EditText minutes = (EditText) promptsView.findViewById(R.id.editMinutes);


                AlertDialog.Builder builder = new AlertDialog.Builder(CvikInfoActivity.this);
                final String[] checkboxDni= new String[]{"PON","UT","STR","STV","PIA","SO","NE"};
                final List<String> list = Arrays.asList(checkboxDni);

                final boolean[] checked = new boolean[]{false, false, false, false, false, false, false};


                builder.setTitle("Vyber deň a čas");
                builder.setMultiChoiceItems(checkboxDni, checked, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            checked[which] = isChecked;
                            String currentItem = list.get(which);

                        }
                    });

                builder.setView(promptsView);

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i<checked.length; i++){
                            boolean kliknute = checked[i];
                            if(kliknute){
                                vybrateDni.add(list.get(i));
                            }
                        }

                       String cas = hours.getText().toString() +":" +minutes.getText().toString();
                       // SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");



                        for(String s : vybrateDni){
                            int i = 0;
                            if(s.equals("PON")||s.equals("UT")||s.equals("STR")||s.equals("STV")||s.equals("PIA")||s.equals("SO")||s.equals("NE")){
                                if(connection != null){

                                    Statement statement = null;
                                    try {
                                        statement = connection.createStatement();
                                        statement.executeUpdate("INSERT INTO [CvicmeApp].[dbo].[Kalendar] VALUES ('"+s+"',"+id+",'"+cas+"')");
                                       // statement.executeUpdate("INSERT INTO [CvicmeApp].[dbo].[Kalendar] VALUES ('PON', 1, '10:00')");

                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                }
                                else{
                                    SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("DEN"+i, s);
                                    editor.putInt("ID"+i, id);
                                    editor.putString("CAS"+i, cas);
                                    editor.putString("NAZOV"+i, nazov);
                                    editor.putString("POPIS"+i, popis);
                                    editor.putInt("POCET", i);
                                    editor.apply();
                                    editor.commit();
                                }
                            }
                            i++;
                        }

                        vybrateDni.clear();
                        String s = "ahoj";
                    }
                });



                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();




            }
        });


    }
}