package com.jdcasas.appeldonante;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class Main2Activity extends AppCompatActivity {
    Button boton1,boton2,boton3,boton4,boton5,boton6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //  add
        boton1=(Button)findViewById(R.id.boton1);
        boton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Buscar Donante.... ", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Main2Activity.this, BuscarDonanteR.class);
                startActivity(intent);
            }
        });

        boton2=(Button)findViewById(R.id.boton2);
        boton2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(getApplicationContext(), "Disponibilidad ", Toast.LENGTH_LONG).show();
                Intent intent=new Intent(Main2Activity.this,Disponibilidad.class);
                startActivity(intent);
            }
        });

        boton3=(Button)findViewById(R.id.boton3);
        boton3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(getApplicationContext(), "Conversar.... ", Toast.LENGTH_LONG).show();
                String number = "995949259";
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + number));
                if (ActivityCompat.checkSelfPermission(Main2Activity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(intent);
            }
        });
        boton4=(Button)findViewById(R.id.boton4);
        boton4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(getApplicationContext(), "Mensaje", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Intent.ACTION_SENDTO,
                        Uri.fromParts("mailto", getResources().getString(R.string.mail),null));
                intent.putExtra(Intent.EXTRA_SUBJECT,getResources().getString(R.string.subject));
                startActivity(Intent.createChooser(intent, getResources().getString(R.string.envio)));
            }
        });

        boton5=(Button)findViewById(R.id.boton5);
        boton5.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(getApplicationContext(), "Geolocalizar ", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.google.com.pe/maps/search/universidad+nacional+de+ingenieria/data=!4m2!2m1!4b1?sa=X&hl=es-MX&nogmmr=1"));
                startActivity(intent);
            }
        });
        boton6=(Button)findViewById(R.id.boton6);
        boton6.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(getApplicationContext(), "Usuario Seleccionado.... ", Toast.LENGTH_LONG).show();
                Intent intent=new Intent(Main2Activity.this,UsuarioSeleccionado.class);
                startActivity(intent);
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
