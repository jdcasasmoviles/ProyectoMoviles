package com.jdcasas.appeldonante;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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


public class BuscarDonanteR extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    TableLayout tablaBD;
    EditText  editTextTipoSangre;
    Spinner sp_tipoSangre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_donante_r);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //SPINNER
        this.sp_tipoSangre = (Spinner) findViewById(R.id.sp_TipoDeSangre);
        loadSpinnertipoSangre();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
        this.sp_tipoSangre.setOnItemSelectedListener(this);
    }


    public void botonBuscarDonante(View view){
       // editTextTipoSangre= (EditText) findViewById(R.id.editTextTipoSangre);
        Spinner spinner = (Spinner) findViewById(R.id.sp_TipoDeSangre);
        Toast mensaje = Toast.makeText(getApplicationContext(), "Buscando......", Toast.LENGTH_LONG);
        mensaje.show();
        //CONSTRUYENDO LA TABLA
        tablaBD = (TableLayout) findViewById(R.id.tablaBDDonante);
        tablaBD.removeAllViews();
        //CREANDO TABLA PARA MOSTRAR
        TableRow tabla = new TableRow(this);
        tabla.setBackgroundColor(Color.CYAN);
        int numColumnas=4;
        //textview VARIABLES
        TextView view1 = new TextView(this);
        TextView view2 = new TextView(this);
        TextView view3 = new TextView(this);
        TextView view4 = new TextView(this);;
        //dar color a las letras
        view1.setTextColor(Color.BLACK);
        view2.setTextColor(Color.BLACK);
        view3.setTextColor(Color.BLACK);
        view4.setTextColor(Color.BLACK);
        //setea valores de textViews
        view1.setText("Telefono");
        view2.setText("Email");
        view3.setText("Sexo");
        view4.setText("Tipo");;
        //para la posicion de los textViews
        view1.setPadding(numColumnas, 1, numColumnas, 1);
        view2.setPadding(numColumnas, 1, numColumnas, 1);
        view3.setPadding(numColumnas, 1, numColumnas, 1);
        view4.setPadding(numColumnas, 1, numColumnas, 1);;
        //adiciona a la tabla
        tabla.addView(view1);
        tabla.addView(view2);
        tabla.addView(view3);
        tabla.addView(view4);
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
       // Conexion.execute(editTextTipoSangre.getText().toString());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

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
            String tiposangre = (String) args[0];
            BaseDatos medica = new BaseDatos(1);
            String link = medica.buscartiposangre(tiposangre);
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
                    String s1 = jsonChildNode.optString("telefono");
                    String s2 = jsonChildNode.optString("email");
                    String s3 = jsonChildNode.optString("sexo");
                    String s4 = jsonChildNode.optString("tiposangre");
                    s4=Numcambiotiposangre(s4);
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

