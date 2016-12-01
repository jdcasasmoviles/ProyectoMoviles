package com.jdcasas.appeldonante;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapaHospitalesID extends AppCompatActivity {
    String Respuesta="";
    String distrito="";
    private CameraUpdate mCamera;
    private GoogleMap mMap;
    double coorcamaraX,coorcamaraY;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_hospitales_id);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //PROCESO PARA OBTENER EL NOMBRE DE USUARIO
        Bundle extras = getIntent().getExtras();
        //Obtenemos datos enviados en el intent.
        if (extras != null) {
            Respuesta = extras.getString("cadenacoordenadas");
            distrito = extras.getString("distrito");
        }
        else Respuesta = "error";
        String fila="";
        String fila2="";

        //toma la cadena 0 hasta un *
        fila = Respuesta.substring(0, Respuesta.indexOf('*'));
        coorcamaraX=Double.parseDouble(fila);
        //mocha la cadena desde * en adelante hasta el final
        Respuesta = Respuesta.substring(Respuesta.indexOf('*') + 1, Respuesta.length());
        //toma la cadena 0 hasta un *
        fila = Respuesta.substring(0, Respuesta.indexOf('*'));
        coorcamaraY=Double.parseDouble(fila);
        //mocha la cadena desde * en adelante hasta el final
        Respuesta = Respuesta.substring(Respuesta.indexOf('*') + 1, Respuesta.length());
        //toma la cadena 0 hasta un *
        fila = Respuesta.substring(0, Respuesta.indexOf('*'));
        //mocha la cadena desde * en adelante hasta el final
        Respuesta = Respuesta.substring(Respuesta.indexOf('*') + 1, Respuesta.length());
        //toma la cadena 0 hasta un *
        fila2 = Respuesta.substring(0, Respuesta.indexOf('*'));
        //mocha la cadena desde * en adelante hasta el final
        Respuesta = Respuesta.substring(Respuesta.indexOf('*') + 1, Respuesta.length());
        System.out.println("Antes del while"+coorcamaraX+" "+coorcamaraY+" "+fila+" "+fila2);
        setUpMapIfNeeded();
        setMarker(new LatLng(coorcamaraX, coorcamaraY), fila,
                fila2 , 0.5F, 0.5F, 0.5F, R.drawable.maphospital);
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
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map)).getMap();
            if (mMap != null) {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mMap.setMyLocationEnabled(true);
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        mCamera = CameraUpdateFactory.newLatLngZoom(new LatLng(
                coorcamaraX, coorcamaraY), 14);
        mMap.animateCamera(mCamera);
    }

    private void setMarker(LatLng position, String title, String info,
                           float opacity, float dimension1, float dimension2, int icon){
        mMap.addMarker(new MarkerOptions()
                .position(position)
                .title(title)
                .snippet(info)
                .alpha(opacity)
                .anchor(dimension1, dimension2)
                .icon(BitmapDescriptorFactory.fromResource(icon)));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}
