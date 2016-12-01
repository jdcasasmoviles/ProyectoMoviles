package com.jdcasas.appeldonante;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;

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


/**
 * Created by Usuario on 10/10/2016.
 */

public class Fragment1 extends Fragment {
    private CameraUpdate mCamera;
    View rootView;
    View inflatedView;
    Spinner sp_distrito;
    Button buscar;

    public static Fragment1 newInstance() {
        Fragment1 fragment = new Fragment1();
        return fragment;
    }

    public Fragment1() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_1, container, false);
        this.sp_distrito = (Spinner) rootView.findViewById(R.id.sp_distrito);
        buscar= (Button) rootView.findViewById(R.id.bBuscarHospitalDistrito);
        loadSpinnerDistritos();
        //ACCION DE BOTON LOGIN
        buscar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                System.out.println("Spiner : "+ sp_distrito.getItemAtPosition(sp_distrito.getSelectedItemPosition()).toString());
                Toast.makeText(getActivity(), "Geolocalizando hospitales.... ", Toast.LENGTH_LONG).show();

                //PARA LA CONEXION AL SERVIDOR
                conexionGet Conexion= null;
                try {
                    Conexion = new conexionGet();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Conexion.execute(sp_distrito.getItemAtPosition(sp_distrito.getSelectedItemPosition()).toString());
            }
        });
        return rootView;
    }

    private void loadSpinnerDistritos() {

        // Create an ArrayAdapter using the string array and a default spinner
        // layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this.getActivity(), R.array.distritos_lima, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        this.sp_distrito.setAdapter(adapter);

        // This activity implements the AdapterView.OnItemSelectedListener
       // this.sp_distrito.setOnItemSelectedListener(this.getActivity());
    }

    class conexionGet extends AsyncTask<String,Void,String> {
        String respuestaServidor = "";
        String distrito="";
        public conexionGet() throws JSONException {
        }

        protected String doInBackground(String... args) {
             distrito = (String) args[0];
            BaseDatos medica = new BaseDatos(2);
            String link = medica.buscarBDHDistritoCoordenadas(distrito);
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
                    String s3 = jsonChildNode.optString("nombre_hospital");
                    String s4 = jsonChildNode.optString("direccion");
                    cadenacoordenadas=cadenacoordenadas+s1+"*"+s2+"*"+s3+"*"+s4+"*";
                    System.out.println("cadenasss : " + s1 + "---" + s2 + "---"+s3);
                }
                System.out.println("\n\ncadenacoordenadas : " + cadenacoordenadas);
                Intent i = new Intent(getActivity(), MapaHospitalesDistrito.class);
                i.putExtra("cadenacoordenadas",cadenacoordenadas);
                i.putExtra("distrito",distrito);
                startActivity(i);
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

    }

}