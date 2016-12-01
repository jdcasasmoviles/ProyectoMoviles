package com.jdcasas.appeldonante;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class BuscarDonantesActivity extends AppCompatActivity {
    Spinner sp_tipoSangre;
    Button buscar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_donantes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //SPINNER
        this.sp_tipoSangre = (Spinner) findViewById(R.id.sp_TipoDeSangreBuscarDonante);
        loadSpinnertipoSangre();
        buscar= (Button) findViewById(R.id.BuscarBuscarDonante);
        //ACCION DE BOTON LOGIN
        buscar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Geolocalizando .... ", Toast.LENGTH_LONG).show();
                //PARA LA CONEXION AL SERVIDOR
               conexionGet Conexion= null;
                try {
                    Conexion = new conexionGet();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Conexion.execute(sp_tipoSangre.getItemAtPosition(sp_tipoSangre.getSelectedItemPosition()).toString());
            }
        });

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
      //  this.sp_tipoSangre.setOnItemSelectedListener(this);
    }

    class conexionGet extends AsyncTask<String,Void,String> {
        String respuestaServidor = "";
        String tipodedangre="";
        public conexionGet() throws JSONException {
        }

        protected String doInBackground(String... args) {
            tipodedangre = (String) args[0];
            BaseDatos medica = new BaseDatos(1);
            String link = medica.buscartiposangreCoordenadad(tipodedangre);
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
                        //json.put("response_", new JSONObject(result));
                        respuestaServidor=result;
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
                String cadenacoordenadas="";
                JSONArray arrayBD =new JSONArray(result);
                for (int i = 0; i < arrayBD.length(); i++) {
                    JSONObject jsonChildNode = arrayBD.getJSONObject(i);
                    String s1 = jsonChildNode.optString("latitud");
                    String s2 = jsonChildNode.optString("longitud");
                    String s3 = jsonChildNode.optString("tiposangre");
                    s3=Numcambiotiposangre(s3);
                    String s4 = jsonChildNode.optString("telefono");
                    String s5 = jsonChildNode.optString("sexo");
                    String s6 = jsonChildNode.optString("disponibilidad");
                    cadenacoordenadas=cadenacoordenadas+s1+"*"+s2+"*"+s3+"\t"+s4+"*"+s5+s6+"*";
                    System.out.println("cadenasss : " + s1 + "---" + s2 + "---"+s3);
                }
                System.out.println("\n\ncadenacoordenadas : " + cadenacoordenadas);
                if(cadenacoordenadas.equals("")){
                    Toast.makeText(getApplicationContext(), "El tipo sangre no esta en la BD", Toast.LENGTH_LONG).show();

                } else{
              Intent i = new Intent(BuscarDonantesActivity.this, MapaUsuariosTipoSangre.class);
                i.putExtra("cadenacoordenadas",cadenacoordenadas);
                startActivity(i);
                }
            } catch (JSONException e) {
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
