package com.jdcasas.appeldonante;
/**
 * Created by Usuario on 11/10/2016.
 */

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

public class Fragment2 extends Fragment {
    private CameraUpdate mCamera;
    View rootView;
    View inflatedView;
    EditText Hospitalid_hospital;
    Button buscar;
    public static Fragment2 newInstance() {
        Fragment2 fragment = new Fragment2();
        return fragment;
    }
    public Fragment2() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_2, container, false);
        buscar= (Button) rootView.findViewById(R.id.bBuscarHospitalId);
        Hospitalid_hospital= (EditText) rootView.findViewById(R.id.editTextHospitalDireccion);
        buscar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Geolocalizando hospital.... ", Toast.LENGTH_LONG).show();

                //PARA LA CONEXION AL SERVIDOR
                conexionGet Conexion= null;
                try {
                    Conexion = new conexionGet();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Conexion.execute(Hospitalid_hospital.getText().toString());
            }
        });
        return rootView;
    }

    class conexionGet extends AsyncTask<String,Void,String> {
        String respuestaServidor = "";
        String idhospital="";
        String distrito="";
        public conexionGet() throws JSONException {
        }

        protected String doInBackground(String... args) {
            idhospital = (String) args[0];
            BaseDatos medica = new BaseDatos(2);
            String link = medica.buscarBDHidCoordenadas(idhospital);
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
                     distrito= jsonChildNode.optString("distrito");
                    cadenacoordenadas=cadenacoordenadas+s1+"*"+s2+"*"+s3+"*"+s4+"*";
                }
                System.out.println("\n\ncadenacoordenadas : " + cadenacoordenadas);
                if(cadenacoordenadas.equals("")){
                    Toast.makeText(getActivity(), "El ID Hospital no esta en la BD", Toast.LENGTH_LONG).show();

                }
                else{
                    Intent i = new Intent(getActivity(), MapaHospitalesID.class);
                    i.putExtra("cadenacoordenadas",cadenacoordenadas);
                    i.putExtra("distrito",distrito);
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

    }

}