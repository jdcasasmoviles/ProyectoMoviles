package com.jdcasas.appeldonante;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;


public class Main2Activity extends AppCompatActivity {
    Button boton1,boton2,boton3,boton4,boton5,boton6;
    private int[] colors;
    String usuario,telefono,dni,tiposangre,nombres,apellidos;
    String mensajePersonalizado="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView letraUsuario = (TextView) findViewById(R.id.letraUsuario);
        //PROCESO PARA OBTENER EL NOMBRE DE USUARIO
        Bundle extras = getIntent().getExtras();
        //Obtenemos datos enviados en el intent.
        if (extras != null) {
            usuario = extras.getString("usuario");//usuario
            telefono = extras.getString("telefono");
            dni = extras.getString("dni");
            tiposangre = extras.getString("tiposangre");
            nombres = extras.getString("nombres");
            apellidos= extras.getString("apellidos");
        }
        else usuario = "error";

        mensajePersonalizado=mensajePersonalizado+"\n";
        mensajePersonalizado=mensajePersonalizado+"User\t: "+usuario+"\n";
        mensajePersonalizado=mensajePersonalizado+"Cell\t: "+telefono+"\n";
        mensajePersonalizado=mensajePersonalizado+"Dni\t: "+dni+"\n";
        mensajePersonalizado=mensajePersonalizado+"Tipo\t: "+tiposangre+"\n";
        letraUsuario.setText(mensajePersonalizado);
        colors = getResources().getIntArray(R.array.initial_colors);
        initCards(letraUsuario,colors);
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
                intent.putExtra("usuario", usuario);
                startActivity(intent);
            }
        });


        boton5=(Button)findViewById(R.id.buttonGPS);
        boton5.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(getApplicationContext(), "Gps Usuarios.... ", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Main2Activity.this, Geolocalizar.class);
                startActivity(intent);
            }
        });
    }

    public void initCards(TextView letraUsuario,int[] colors) {
        Random rnd = new Random();
        int i=(int)(rnd.nextDouble() * 50);
        letraUsuario.setBackgroundColor(colors[i]);
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
                Intent i = new Intent(Main2Activity.this, UsuarioSeleccionado.class);
                i.putExtra("usuario",usuario);
                i.putExtra("telefono",telefono);
                i.putExtra("dni",dni);
                i.putExtra("tiposangre",tiposangre);
                i.putExtra("nombres",nombres);
                i.putExtra("apellidos",apellidos);
                startActivity(i);
                finish();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

}
