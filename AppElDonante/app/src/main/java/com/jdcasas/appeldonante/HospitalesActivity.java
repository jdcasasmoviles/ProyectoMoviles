package com.jdcasas.appeldonante;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
        //SPINNER
        this.sp_distrito = (Spinner) findViewById(R.id.sp_distrito);
        loadSpinnerDistritos();
        }

    public void botonBuscarHospital(View view){
        Spinner spinner = (Spinner) findViewById(R.id.sp_distrito);
        Toast mensaje = Toast.makeText(getApplicationContext(), "Buscando......", Toast.LENGTH_LONG);
        mensaje.show();
        //cosas que aparecen en el layout
        tablaBD = (TableLayout) findViewById(R.id.TablaMisHospitales);
        tablaBD.removeAllViews();
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
        //dar estilo background
        viewHeaderId.setBackgroundResource(R.drawable.rounded_corners_cyan);
        viewHeaderHospital.setBackgroundResource(R.drawable.rounded_corners_cyan);
        viewHeaderDireccion.setBackgroundResource(R.drawable.rounded_corners_cyan);
        viewHeaderNecesitaTipo.setBackgroundResource(R.drawable.rounded_corners_cyan);
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
        //PARA LA CONEXION AL SERVIDOR
        conexionGet Conexion= null;
        try {
            Conexion = new conexionGet(this,tablaBD,tabla);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Conexion.execute(spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString());
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
    class conexionGet extends AsyncTask<String,Void,String> {
        private Context context;
        TableLayout tablaBD;
        TableRow tabla;
        String respuestaServidor = "";

        public conexionGet(Context context, TableLayout tablaBD, TableRow tabla) throws JSONException {
            this.context = context;
            this.tablaBD = tablaBD;
            this.tabla = tabla;
        }

        protected String doInBackground(String... args) {
            String distrito = (String) args[0];
            BaseDatos medica = new BaseDatos(2);
            String link = medica.buscardistrito(distrito);
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
            int numColumnas=4;
            try {
                System.out.println("result : " + result);
                JSONArray arrayBD =new JSONArray(result);
                for (int i = 0; i < arrayBD.length(); i++) {
                    JSONObject jsonChildNode = arrayBD.getJSONObject(i);
                    String s1 = jsonChildNode.optString("id_hospital");
                    String s2 = jsonChildNode.optString("nombre_hospital");
                    String s3 = jsonChildNode.optString("direccion");
                    String s4 = jsonChildNode.optString("necesita");
                    String necesira="";
                    for(int j=0;j<s4.length();j++){
                        necesira=necesira+Numcambiotiposangre(s4.charAt(j)+"")+" ";
                    }
                   s4=necesira;
                    System.out.println("cadenasss : " + s1 + "---" + s2 + "---" + s3 + "---" + s4);
                    tabla = new TableRow(context);
                    if (i % 2 == 0) {
                        tabla.setBackgroundColor(Color.WHITE);
                    } else {

                        tabla.setBackgroundColor(Color.LTGRAY);
                    }
                    TextView tv1 = new TextView(context);
                    tv1.setText(s1);
                    tv1.setPadding(numColumnas, 1, numColumnas, 1);
                    tabla.addView(tv1);

                    TextView tv2 = new TextView(context);
                    tv2.setText(s2);
                    tv2.setPadding(numColumnas, 1, numColumnas, 1);
                    tabla.addView(tv2);

                    TextView tv3 = new TextView(context);
                    tv3.setText(s3);
                    tv3.setPadding(numColumnas, 1, numColumnas, 1);
                    tabla.addView(tv3);

                    TextView tv4 = new TextView(context);
                    tv4.setText(s4);
                    tv4.setPadding(numColumnas, 1, numColumnas, 1);
                    tabla.addView(tv4);

                    //dar color a las letras
                    tv1.setTextColor(Color.BLACK);
                    tv2.setTextColor(Color.BLACK);
                    tv3.setTextColor(Color.BLACK);
                    tv4.setTextColor(Color.BLACK);


                    tablaBD.addView(tabla, new TableLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                            ActionBar.LayoutParams.MATCH_PARENT));
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

}
