package com.jdcasas.appeldonante;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jdcasas.appeldonante.library.Httppostaux;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class LoginActivity extends AppCompatActivity {
    ///VARIABLES
    EditText user;
    EditText pass;
    Button blogin;
    TextView registrar;
    Httppostaux post;
    boolean result_back;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        post=new Httppostaux();
        user= (EditText) findViewById(R.id.EditTextUsuario);
        pass= (EditText) findViewById(R.id.EditTextContraseña);
        blogin= (Button) findViewById(R.id.buttonlogin);
        registrar=(TextView) findViewById(R.id.link_to_register);
		//ACCION DE BOTON LOGIN
        blogin.setOnClickListener(new View.OnClickListener(){
			public void onClick(View view){
				//EXTRAEMOS DATOS DE LOS EDITEXT
				String usuario=user.getText().toString();
				String passw=pass.getText().toString();
				//VERIFICAMOS SI ESTAN EN BLANCO
        		if( checklogindata( usuario , passw )==true){
        		//si pasamos esa validacion ejecutamos el asynctask pasando el usuario y clave como parametros
        		new asynclogin().execute(usuario,passw);
        		}
				else{//si detecto un error en la primera validacion vibrar y mostrar un Toast con un mensaje de error.
				 err_login();
        		}
        	}
        													});
        registrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Al registro..... ", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LoginActivity.this, RegistrarseActivity.class);
                startActivity(intent);
                finish();
            }
        });
  
    }


    //vibra y muestra un Toast
    public void err_login(){
    	Vibrator vibrator =(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
	    vibrator.vibrate(200);
	    Toast toast1 = Toast.makeText(getApplicationContext(),"Error:Nombre de usuario o password incorrectos", Toast.LENGTH_SHORT);
 	    toast1.show();    	
    }
    
        /*Valida el estado del logueo solamente necesita como parametros el usuario y passw*/
    public boolean loginstatus(String username ,String password ) {
    	int logstatus=-1;
    	
    	/*Creamos un ArrayList del tipo nombre valor para agregar los datos recibidos por los parametros anteriores
    	 * y enviarlo mediante POST a nuestro sistema para relizar la validacion*/ 
    	ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
     		
		    		postparameters2send.add(new BasicNameValuePair("usuario",username));
		    		postparameters2send.add(new BasicNameValuePair("password",password));

		   //realizamos una peticion y como respuesta obtenes un array JSON
      // String IP_Server="192.168.1.42";
	 String IP_Server="masterwishmaster.esy.es";//IP DE NUESTRO PC LOCALHOST
        String URL_connect="http://"+IP_Server+"/Donante/acces.php";//ruta en donde estan nuestros archivos
        JSONArray jdata=post.getserverdata(postparameters2send, URL_connect);
		    SystemClock.sleep(950);
		    //si lo que obtuvimos no es null
		    	if (jdata!=null && jdata.length() > 0){

		    		JSONObject json_data; //creamos un objeto JSON
					try {
                        json_data = jdata.getJSONObject(0); //leemos el primer segmento en nuestro caso el unico
                        logstatus=json_data.getInt("logstatus");//accedemos al valor
                        Log.e("loginstatus","logstatus= "+logstatus);//muestro por log que obtuvimos
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//validamos el valor obtenido
		    		 if (logstatus==0){// [{"logstatus":"0"}] 
		    			 Log.e("loginstatus ", "invalido");
		    			 return false;
		    		 }
		    		 else{// [{"logstatus":"1"}]
		    			 Log.e("loginstatus ", "valido");
		    			 return true;
		    		 }

			  }else{	//json obtenido invalido verificar parte WEB.
		    			 Log.e("JSON  ", "ERROR");
			    		return false;
			  }
    	
    }
    
      //validamos si no hay ningun campo en blanco
    public boolean checklogindata(String username ,String password ){
    	
    if 	(username.equals("") || password.equals("")){
    	Log.e("Login ui", "checklogindata user or pass error");
    return false;
    
    }else{
    	
    	return true;
    }
    
}


/*		CLASE ASYNCTASK
 * 
 * usaremos esta para poder mostrar el dialogo de progreso mientras enviamos y obtenemos los datos
 * podria hacerse lo mismo sin usar esto pero si el tiempo de respuesta es demasiado lo que podria ocurrir    
 * si la conexion es lenta o el servidor tarda en responder la aplicacion sera inestable.
 * ademas observariamos el mensaje de que la app no responde.     
 */
    
    class asynclogin extends AsyncTask< String, String, String > {
    	 
    	String user,pass;
        protected void onPreExecute() {
        	//para el progress dialog
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Autenticando....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
 
		protected String doInBackground(String... params) {
			//obtnemos usr y pass
			user=params[0];
			pass=params[1];
			//enviamos y recibimos y analizamos los datos en segundo plano.
    		if (loginstatus(user,pass)==true){    		    		
    			return "ok"; //login valido
    		}else{    		
    			return "err"; //login invalido     	          	  
    		}
        	
		}
       
		/*Una vez terminado doInBackground segun lo que halla ocurrido 
		pasamos a la sig. activity
		o mostramos error*/
        protected void onPostExecute(String result) {

           pDialog.dismiss();//ocultamos progess dialog.
           Log.e("onPostExecute=",""+result);
            if(result.equals("err")){
                Vibrator vibrator =(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(200);
                Toast toast1 = Toast.makeText(getApplicationContext(),"Error no hay conexion al servidor", Toast.LENGTH_SHORT);
                toast1.show();
            }
           if (result.equals("ok")){
               //PARA LA CONEXION AL SERVIDOR
               conexionGet Conexion= null;
               try {
                   Conexion = new conexionGet();
               } catch (JSONException e) {
                   e.printStackTrace();
               }
               Conexion.execute(user);
            }else{
            	err_login();
            }
		}
		
        }

    class conexionGet extends AsyncTask<String,Void,String> {
        String respuestaServidor = "";

        public conexionGet() throws JSONException {

        }

        protected String doInBackground(String... args) {
            String usuario = (String) args[0];
            BaseDatos medica = new BaseDatos(1);
            String link = medica.buscarBDUsuario(usuario);
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
                JSONArray arrayBD =new JSONArray(result);
                String s1="",s2="",s3="",s4="",s5="",s6="",s7="";
                for (int i = 0; i < arrayBD.length(); i++) {
                    JSONObject jsonChildNode = arrayBD.getJSONObject(i);
                     s1 = jsonChildNode.optString("usuario");
                     s2 = jsonChildNode.optString("telefono");
                     s3 = jsonChildNode.optString("dni");
                     s4 = jsonChildNode.optString("tiposangre");
                    s4=Numcambiotiposangre(s4);
                    s5 = jsonChildNode.optString("nombres");
                    s6 = jsonChildNode.optString("apellidos");
                    s7 = jsonChildNode.optString("email");
                }
                System.out.println("cadenasss : " + s1 + "---" + s2 + "---" + s3 + "---" + s4+ "---" + s5+ "---" + s6);
                Intent i=new Intent(LoginActivity.this, UsuarioSeleccionado.class);
                i.putExtra("usuario",s1);
                i.putExtra("telefono",s2);
                i.putExtra("dni",s3);
                i.putExtra("tiposangre",s4);
                i.putExtra("nombres",s5);
                i.putExtra("apellidos",s6);
                i.putExtra("email",s7);
                System.out.println("Menu usuario");
                startActivity(i);
                finish();
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
