package com.jdcasas.appeldonante;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

;

public class MainActivity extends AppCompatActivity {
    GridView grid;
    String[] web = {
            "Registrarse",
            "Hospitales",
            "Login" ,
            "Compatibilidad",
            "Buscar Donantes",
            "Mapa Hospitales",
            "Acerca de"

    } ;
    int[] imageId = {
            R.drawable.q1,
            R.drawable.q2,
            R.drawable.q3,
            R.drawable.q4,
            R.drawable.q5,
            R.drawable.q6,
            R.drawable.about
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CustomGrid adapter = new CustomGrid(MainActivity.this, web, imageId);
        grid = (GridView) findViewById(R.id.grid);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(MainActivity.this, "You Clicked at " +web[+ position], Toast.LENGTH_SHORT).show();
                Intent intent;
                if(web[+ position].equals("Registrarse")){
                     intent = new Intent(MainActivity.this,RegistrarseActivity.class);
                    startActivity(intent);
                }
                else if(web[+ position].equals("Hospitales")){
                     intent = new Intent(MainActivity.this, HospitalesActivity.class);
                    startActivity(intent);
                }
                else if(web[+ position].equals("Login")){
                    intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                else if(web[+ position].equals("Compatibilidad")){
                    intent = new Intent(MainActivity.this, CompatibilidadActivity.class);
                    startActivity(intent);
                }
                else if(web[+ position].equals("Buscar Donantes")){
                    intent = new Intent(MainActivity.this, BuscarDonantesActivity.class);
                    startActivity(intent);
                }
                else if(web[+ position].equals("Mapa Hospitales")){
                   intent = new Intent(MainActivity.this, MapaHospitalesActivity.class);
                    startActivity(intent);
                }
                else if(web[+ position].equals("Acerca de")){
                    intent = new Intent(MainActivity.this, AcercadeActivity.class);
                    startActivity(intent);
                }

            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Acerca_de:
                Toast.makeText(getApplicationContext(), "Opcion 1!", Toast.LENGTH_LONG).show();
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Acerca de :")
                        .setMessage("Aplicacion hecha por : Tomas J. Casas Rodriguez\nDanny Julca\nEstudiantes de Ciencias de la Computacion\nDe la Universidad Nacional de Ingeneria\nPara el curso : PROGRAMACIÓN EN DISPOSITIVOS MÓVILES")
                        .setPositiveButton("Aceptar",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                builder.create().show();
                return true;
            case R.id.Configuracion:
                Toast.makeText(getApplicationContext(), "Configuracion ", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, ConfiguracionActivity.class);
                startActivity(intent);
                return true;
            case R.id.Salir:
                Toast.makeText(getApplicationContext(), "Saliendo.... ", Toast.LENGTH_LONG).show();
                finish();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}

