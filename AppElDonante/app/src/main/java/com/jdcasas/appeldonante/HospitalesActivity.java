package com.jdcasas.appeldonante;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class HospitalesActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    //VARIABLES
    TableLayout tablaBD;
    String user="";
    Spinner sp_distrito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospitales);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView txt_usr = (TextView) findViewById(R.id.textView12);

        txt_usr.setText("\nUsuario Invitado" + "\n\n");//cambiamos texto al nombre del usuario logueado
        //SPINNER
        this.sp_distrito = (Spinner) findViewById(R.id.sp_distrito);
        loadSpinnerDistritos();

        //cosas que aparecen en el layout
        tablaBD = (TableLayout) findViewById(R.id.TablaMisHospitales);
        //CREANDO TABLA PARA MOSTRAR
        TableRow tabla = new TableRow(this);
        tabla.setBackgroundColor(Color.CYAN);
        //textview VARIABLES
        TextView viewHeaderId = new TextView(this);
        TextView viewHeaderHospital = new TextView(this);
        TextView viewHeaderDireccion = new TextView(this);
        TextView viewHeaderNecesitaTipo = new TextView(this);
        //dar color a las letras
        viewHeaderId.setTextColor(Color.BLACK);
        viewHeaderHospital.setTextColor(Color.BLACK);
        viewHeaderDireccion.setTextColor(Color.BLACK);
        viewHeaderNecesitaTipo.setTextColor(Color.BLACK);
        //setea valores de textViews
        viewHeaderId.setText("Id");
        viewHeaderHospital.setText("Nombre");
        viewHeaderDireccion.setText("Direccion");
        viewHeaderNecesitaTipo.setText("Necesita tipo");
        //para la posicion de los textViews
        viewHeaderId.setPadding(4, 1, 4, 1);
        viewHeaderHospital.setPadding(4, 1, 4, 1);
        viewHeaderDireccion.setPadding(4, 1, 4, 1);
        viewHeaderNecesitaTipo.setPadding(4, 1, 4, 1);
        //adiciona a la tabla
        tabla.addView(viewHeaderId);
        tabla.addView(viewHeaderHospital);
        tabla.addView(viewHeaderDireccion);
        tabla.addView(viewHeaderNecesitaTipo);
        tablaBD.addView(tabla, new TableLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT));
            for (int i = 1; i < 5; i++) {
                String id="00"+i;
                String nombre="Hospital  "+i;
                String direccion="Av. distriatal 0"+i+"0 Lote "+i;
                String Necesitatipo="Tipo "+i;
                tabla = new TableRow(this);
                //AGREGA COLORES A LAS FILAS
                if (i % 2 == 0) {
                    tabla.setBackgroundColor(Color.LTGRAY);
                }
                else{
                    tabla.setBackgroundColor(Color.WHITE);
                }

                TextView viewId = new TextView(this);
                viewId.setText(id);
                viewId.setPadding(6, 1, 6, 1);
                tabla.addView(viewId);

                TextView viewNombre = new TextView(this);
                viewNombre.setText(nombre);
                viewNombre.setPadding(6, 1, 6, 1);
                tabla.addView(viewNombre);

                TextView viewDireccion = new TextView(this);
                viewDireccion.setText(direccion);
                viewDireccion.setPadding(6, 1, 6, 1);
                tabla.addView(viewDireccion);

                TextView viewNecesitaTipo = new TextView(this);
                viewNecesitaTipo.setText(Necesitatipo);
                viewNecesitaTipo.setPadding(6, 1, 6, 1);
                tabla.addView(viewNecesitaTipo);

                //dar color a las letras
                viewId.setTextColor(Color.BLACK);
                viewNombre.setTextColor(Color.BLACK);
                viewDireccion.setTextColor(Color.BLACK);
                viewNecesitaTipo.setTextColor(Color.BLACK);

                tablaBD.addView(tabla, new TableLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                        ActionBar.LayoutParams.MATCH_PARENT));
            }


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

    private void loadSpinnerDistritos() {

        // Create an ArrayAdapter using the string array and a default spinner
        // layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.distritos_lima, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        this.sp_distrito.setAdapter(adapter);

        // This activity implements the AdapterView.OnItemSelectedListener
        this.sp_distrito.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}
