package com.jdcasas.appeldonante;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class UsuarioSeleccionado extends AppCompatActivity {
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ActionBar actionBar;
    TextView textView;
    String usuario,telefono,dni,tiposangre,nombres,apellidos,email;
    String nombrecompletoUsuario;
    private int[] colors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_seleccionado);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);
        drawerLayout = (DrawerLayout)
                findViewById(R.id.navigation_drawer);
        NavigationView navigationView = (NavigationView)
                findViewById(R.id.navigation_view);
        if (navigationView != null) {
            setupNavigation(navigationView);
        }
        setupNavigation(navigationView);
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
            email= extras.getString("email");
             nombrecompletoUsuario=nombres+" "+apellidos+"\t"+tiposangre;
         }
        else usuario = "error";
        TextView txt_usr = (TextView) findViewById(R.id.textViewLetraPersonal);
        TextView txt_bienvenidaTitulo = (TextView) findViewById(R.id.TextViewBienvenido);
        TextView txt_bienvenida = (TextView) findViewById(R.id.TextViewBienvenidoinfo);
        txt_bienvenidaTitulo.setText("\t\t\tBienvenido\t"+usuario);
        txt_bienvenida.setText("Telefono\t\t:\t"+telefono+"\nTipo Sangre\t\t:\t"+tiposangre);
        View header=navigationView.getHeaderView(0);
        TextView TextViewUsuario = (TextView)header.findViewById(R.id.TextViewUsuario);
        TextViewUsuario.setText(nombrecompletoUsuario);
       // TextView TextViewUsuario = (TextView) findViewById(R.id.TextViewUsuario);
      //  TextViewUsuario.setText(usuario);
        Random rnd = new Random();
        int i=(int)(rnd.nextDouble() * 50);
        colors = getResources().getIntArray(R.array.initial_colors);
        txt_usr.setBackgroundColor(colors[i]);
        String primeraLetra=""+usuario.charAt(0);
        primeraLetra= primeraLetra.toUpperCase();
        txt_usr.setText(primeraLetra);//cambiamos texto al nombre del usuario logueado
        // ACCIONES DEL MENU
        TextView textViewLetraPersonal=(TextView)findViewById(R.id.textViewLetraPersonal);
        textViewLetraPersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Menu alternativo.... ", Toast.LENGTH_LONG).show();
                Intent i = new Intent(UsuarioSeleccionado.this, Main2Activity.class);
                i.putExtra("usuario",usuario);
                i.putExtra("telefono",telefono);
                i.putExtra("dni",dni);
                i.putExtra("tiposangre",tiposangre);
                i.putExtra("nombres",nombres);
                i.putExtra("apellidos", apellidos);
                startActivity(i);
                finish();
            }
        });
        ImageView iv1=(ImageView)findViewById(R.id.imagenBuscarDonante);
        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Buscar Donante.... ", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(UsuarioSeleccionado.this, BuscarDonanteR.class);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });


        ImageView iv5=(ImageView)findViewById(R.id.imagengpsUsuarios);
        iv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Gps Usuarios.... ", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(UsuarioSeleccionado.this, Geolocalizar.class);
                startActivity(intent);
            }
        });
        ImageView iv6=(ImageView)findViewById(R.id.imagenHospitales);
        iv6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Lista Hospitales.... ", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(UsuarioSeleccionado.this, HospitalesActivity.class);
                startActivity(intent);
            }
        });
        ImageView iv7=(ImageView)findViewById(R.id.imageViewDisponibilidad);
        iv7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Disponibilidad.... ", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(UsuarioSeleccionado.this, Disponibilidad.class);
                intent.putExtra("usuario", usuario);
                startActivity(intent);
            }
        });

        ImageView iv9=(ImageView)findViewById(R.id.imagengpsHospitales);
        iv9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "GPS Hospitales.... ", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(UsuarioSeleccionado.this, MapaHospitalesActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
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
        }
        return super.onOptionsItemSelected(item);
    }

    public void setupNavigation(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.item_navigation_inbox:
                                menuItem.setChecked(true);
                                Toast.makeText(getApplicationContext(), "Disponibilidad ", Toast.LENGTH_LONG).show();
                                Intent intent1 = new Intent(UsuarioSeleccionado.this, Disponibilidad.class);
                                startActivity(intent1);
                                return true;
                            case R.id.item_navigation_starred:
                                menuItem.setChecked(true);
                                Toast.makeText(getApplicationContext(), "Compatibilidad ", Toast.LENGTH_LONG).show();
                                Intent intent2 = new Intent(UsuarioSeleccionado.this, CompatibilidadActivity.class);
                                startActivity(intent2);
                                return true;
                            case R.id.item_navigation_sent_mail:
                                menuItem.setChecked(true);
                                Toast.makeText(getApplicationContext(), "Hospitales ", Toast.LENGTH_LONG).show();
                                Intent intent3 = new Intent(UsuarioSeleccionado.this, HospitalesActivity.class);
                                startActivity(intent3);
                                return true;
                            case R.id.item_navigation_settings:
                                menuItem.setChecked(true);
                                Toast.makeText(getApplicationContext(), "About ", Toast.LENGTH_LONG).show();
                                Intent intent4 = new Intent(UsuarioSeleccionado.this, AcercadeActivity.class);
                                startActivity(intent4);
                                return true;
                        }
                        return true;
                    }
                });
    }

}
