package com.jdcasas.appeldonante;
import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class RegistrarseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner sp_tipoSangre;
    String dni = "";
    String nombres = "";
    String apellidos = "";
    String sexo = "";
    String tiposangre = "";
    String latitud = "";
    String longitud = "";
    String email = "";
    String telefono = "";
    String usuario = "";
    String password = "";
    String disponibilidad = "";
    Button ButtonAutomatico;
    private LocationManager locManager;
    private LocationListener locListener;
    private static final long MIN_DISTANCE = 5;
    private static final long MIN_TIME = 10 * 10000; //10 segundos
    private LocationManager mLocationManager;
    private String mProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //SPINNER
        this.sp_tipoSangre = (Spinner) findViewById(R.id.sp_TipoDeSangre);
        //boton auto coor
        ButtonAutomatico = (Button) findViewById(R.id.ButtonAutomatico);
        loadSpinnertipoSangre();
        //ACCION DE BOTON LOGIN
        ButtonAutomatico.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Auto Coor.... ", Toast.LENGTH_LONG).show();
                comenzarLocalizacion();
            }
        });

    }

    private void comenzarLocalizacion() {
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(RegistrarseActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        mostrarPosicion(loc);
        locListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                mostrarPosicion(location);
            }

            public void onProviderDisabled(String provider) {
                Toast.makeText(getApplicationContext(), "Provider OFF", Toast.LENGTH_LONG).show();
                return;
            }

            public void onProviderEnabled(String provider) {
                Toast.makeText(getApplicationContext(), "Provider ON", Toast.LENGTH_LONG).show();
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.i("", "Provider Status: " + status);
                Toast.makeText(getApplicationContext(), "Provider Status: " + status, Toast.LENGTH_LONG).show();

            }
        };

//       locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 15000, 0, locListener);
    }

    private void mostrarPosicion(Location loc) {
        double lat1=0;
        double lng1=0;
        EditText etLatitud = (EditText) findViewById(R.id.editTextCoorX);
        EditText etLongitud = (EditText) findViewById(R.id.editTextCoorY);
        if (loc != null) {
            etLatitud.setText(String.valueOf(loc.getLatitude()));
            etLongitud.setText(String.valueOf(loc.getLongitude()));
            Log.i("", String.valueOf(loc.getLatitude() + " - " +
                    String.valueOf(loc.getLongitude())));
        } else {
            etLatitud.setText("-12.017124");
            etLongitud.setText("-77.050744");
        }
    }

    public void botonReset(View view)
    {   finish();
        Toast.makeText(getApplicationContext(), "Reset.... ", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, RegistrarseActivity.class);
        startActivity(intent);
    }
    public void botonRegistrar(View view)
    {   EditText etDni = (EditText) findViewById(R.id.editTextDni);
        dni=etDni.getText().toString();
        EditText etNombres = (EditText) findViewById(R.id.editTextNombres);
        nombres=etNombres.getText().toString();
        EditText ettApellidos = (EditText) findViewById(R.id.editTextApellidos);
        apellidos=ettApellidos.getText().toString();
        Spinner spTipoSangre = (Spinner) findViewById(R.id.sp_TipoDeSangre);
        tiposangre=spTipoSangre.getItemAtPosition(spTipoSangre.getSelectedItemPosition()).toString();
        tiposangre=cambiotiposangre(tiposangre);
        EditText etLatitud = (EditText) findViewById(R.id.editTextCoorX);
        latitud=etLatitud.getText().toString();
        EditText etLongitud = (EditText) findViewById(R.id.editTextCoorY);
        longitud=etLongitud.getText().toString();
        EditText etEmail = (EditText) findViewById(R.id.editTextEmail);
        email=etEmail.getText().toString();
        EditText etTelefono = (EditText) findViewById(R.id.editTextTelefono);
        telefono=etTelefono.getText().toString();
        EditText etUsuario = (EditText) findViewById(R.id.editTextUsuario);
        usuario=etUsuario.getText().toString();
        EditText etPassword = (EditText) findViewById(R.id.editTextPass);
        password=etPassword.getText().toString();
        CheckBox cbDisponibilidad = (CheckBox) findViewById(R.id.cbDisponibilidad);
        if (cbDisponibilidad.isChecked()) {
            disponibilidad="S";
        }
        else{
            disponibilidad="N";
        }
        System.out.println("dni "+dni+"\n" +
                "dni "+dni+"\n" +
                "nombres "+nombres+"\n" +
                "apellidos "+apellidos+"\n" +
                "sexo "+sexo+"\n" +
                "tiposangre "+tiposangre+"\n" +
                "latitud "+latitud+"\n" +
                "longitud "+longitud+"\n" +
                "email "+email+"\n" +
                "telefono "+telefono+"\n" +
                "usuario "+usuario+"\n" +
                "password "+password+"\n"+
                "disponibilidad "+disponibilidad+"\n");
        if(!dni.equals("")&& !nombres.equals("")&& !apellidos.equals("")&& !sexo.equals("")&& !tiposangre.equals("")&& !latitud.equals("")&& !longitud.equals("")&& !email.equals("")&& !telefono.equals("")&& !usuario.equals("")&& !password.equals("")&& !disponibilidad.equals("")){
            Toast.makeText(getApplicationContext(), "Conectando... ", Toast.LENGTH_LONG).show();
            //PARA LA CONEXION AL SERVIDOR
            conexionGet Conexion= null;
            try {
                Conexion = new conexionGet(this);
                Conexion.execute(dni, nombres, apellidos, sexo, tiposangre, latitud, longitud, email, telefono, usuario, password, disponibilidad);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else{
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("INFORME:")
                    .setMessage("Hay campos sin llenar")
                    .setPositiveButton("Aceptar",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
            builder.create().show();
        }


    }
    public void rbSexo(View view)
    {
        boolean marcado = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.radioButtonM:
                if (marcado) {
                    sexo="M";
                }
                break;
            case R.id.radioButtonF:
                if (marcado) {
                    sexo="F";
                }
                break;
        }
    }

        private void loadSpinnertipoSangre() {

        // Create an ArrayAdapter using the string array and a default spinner
        // layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.array_tipoSangre, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        this.sp_tipoSangre.setAdapter(adapter);

        // This activity implements the AdapterView.OnItemSelectedListener
        this.sp_tipoSangre.setOnItemSelectedListener(this);
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

    public String cambiotiposangre(String tiposangre){
        if(tiposangre.equals("A+")){
            tiposangre="1";
            return tiposangre;
        }
        else if(tiposangre.equals("A-")){
            tiposangre="2";
            return tiposangre;
        }
        else if(tiposangre.equals("B+")){
            tiposangre="3";
            return tiposangre;
        }
        else if(tiposangre.equals("B-")){
            tiposangre="4";
            return tiposangre;
        }
        else if(tiposangre.equals("AB+")){
            tiposangre="5";
            return tiposangre;
        }
        else if(tiposangre.equals("AB-")){
            tiposangre="6";
            return tiposangre;
        }
        else if(tiposangre.equals("O+")){
            tiposangre="7";
            return tiposangre;
        }
        else if(tiposangre.equals("O-")){
            tiposangre="8";
            return tiposangre;
        }
        return tiposangre;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    class conexionGet extends AsyncTask<String,Void,String> {
        private Context context;
        String respuestaServidor = "";

        public conexionGet(Context context) throws JSONException {
            this.context = context;
        }

        protected String doInBackground(String... args) {
            BaseDatos medica = new BaseDatos(1);
            String[] datos = new String[13];

            for(int i=0;i<12;i++){
                datos[i] = (String) args[i];
                System.out.println("datos :  "+datos[i] );
            }
            String link = medica.insertarUsuario(datos);
            System.out.println("url..  :  " + link);
            try {
                URL url = new URL(link);
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(link));
                HttpResponse response = client.execute(request);
                if (response.getStatusLine().getStatusCode() == 200) {
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        InputStream instream = entity.getContent();
                        String result = convertStreamToString(instream);
                        Log.d("result ****", String.valueOf((result)));
                        respuestaServidor = result;
                        instream.close();
                    }
                } else {
                    Log.d("result **** error", String.valueOf((0)));
                }
                return respuestaServidor;
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }

        }

        protected void onPostExecute(String result) {
            try {
                System.out.println("result : " + result);
                JSONArray arrayBD =new JSONArray(result);
                String cadenaResultado ="";
                for (int i = 0; i < arrayBD.length(); i++) {
                    JSONObject jsonChildNode = arrayBD.getJSONObject(i);
                    cadenaResultado =cadenaResultado+"dni : "+ jsonChildNode.optString("dni")+"\n";
                    cadenaResultado =cadenaResultado+"nombres : "+ jsonChildNode.optString("nombres")+"\n";
                    cadenaResultado =cadenaResultado+"apellidos : "+ jsonChildNode.optString("apellidos")+"\n";
                    cadenaResultado =cadenaResultado+"sexo : "+ jsonChildNode.optString("sexo")+"\n";
                    cadenaResultado =cadenaResultado+"tipo sangre : "+Numcambiotiposangre(jsonChildNode.optString("tiposangre"))+"\n";
                    cadenaResultado =cadenaResultado+"latitud : "+ jsonChildNode.optString("latitud")+"\n";
                    cadenaResultado =cadenaResultado+"longitud : "+ jsonChildNode.optString("longitud")+"\n";
                    cadenaResultado =cadenaResultado+"email : "+ jsonChildNode.optString("email")+"\n";
                    cadenaResultado =cadenaResultado+"telefono : "+ jsonChildNode.optString("telefono")+"\n";
                    cadenaResultado =cadenaResultado+"usuario : "+ jsonChildNode.optString("usuario")+"\n";
                    cadenaResultado =cadenaResultado+"password : "+ jsonChildNode.optString("password")+"\n";
                    cadenaResultado =cadenaResultado+"disponibilidad : "+ jsonChildNode.optString("disponibilidad")+"\n";
                    cadenaResultado =cadenaResultado+"RECUERDE SU USUARIO Y PASSWORD\nGRACIAS";
                }
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("DATOS REGISTRADOS:")
                        .setMessage(cadenaResultado)
                        .setPositiveButton("Aceptar",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        Toast.makeText(getApplicationContext(), "Saliendo.... ", Toast.LENGTH_LONG).show();
                                        finish();
                                    }
                                });
                builder.create().show();
            } catch (JSONException e) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("ALERTA")
                        .setMessage("No se obtuvo respuesta del servidor")
                        .setPositiveButton("Aceptar",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                builder.create().show();
                e.printStackTrace();
            }
        }


        private String convertStreamToString(InputStream in) {
            int BUFFER_SIZE = 2000;
            InputStreamReader isr = new InputStreamReader(in);
            int charRead;
            String str = "";
            char[] inputBuffer = new char[BUFFER_SIZE];
            try {
                while ((charRead = isr.read(inputBuffer)) > 0) {
                    String readString = String.copyValueOf(inputBuffer, 0, charRead);
                    str += readString;
                    inputBuffer = new char[BUFFER_SIZE];
                }
                in.close();
            } catch (IOException e) {
                // Handle Exception
                e.printStackTrace();
                return "";
            }
            return str;
        }

        public String Numcambiotiposangre(String tiposangre){
            if(tiposangre.equals("1")){
                tiposangre="A+";
                return tiposangre;
            }
            else if(tiposangre.equals("2")){
                tiposangre="A-";
                return tiposangre;
            }
            else if(tiposangre.equals("3")){
                tiposangre="B+";
                return tiposangre;
            }
            else if(tiposangre.equals("4")){
                tiposangre="B-";
                return tiposangre;
            }
            else if(tiposangre.equals("5")){
                tiposangre="AB+";
                return tiposangre;
            }
            else if(tiposangre.equals("6")){
                tiposangre="AB-";
                return tiposangre;
            }
            else if(tiposangre.equals("7")){
                tiposangre="O+";
                return tiposangre;
            }
            else if(tiposangre.equals("8")){
                tiposangre="O-";
                return tiposangre;
            }
            return tiposangre;
        }

    }


    }
