package com.jdcasas.appeldonante;
public  class BaseDatos {
	String IP_Server="masterwishmaster.esy.es";//IP DE NUESTRO PC LOCALHOST
    //String IP_Server="192.168.1.14";
    String URL="" ;
    public BaseDatos(int numeroTabla){
        if(numeroTabla==1){
            URL = "http://"+IP_Server+"/Donante/serverTablaUsuarios.php";
            System.out.println("Estoy usando tabla usuarios");
        }
    }

     public String buscartiposangre(String tiposangre) {
         tiposangre=tiposangre.replace(" ","%20");
         tiposangre=cambiotiposangre(tiposangre);
            URL = URL + "?operacion=buscarBDDonante&tiposangre=" +tiposangre;
            System.out.println("URL buscar  BD : " + URL);
        return URL;
    }

    public String insertarUsuario(String[] datos) {
        URL = URL + "?operacion=insertarUsuario";
        for(int i=0;i<12;i++)
        {
            datos[i] = datos[i].replace(" ","%20");
            System.out.println("datos :  "+datos[i] );
            URL = URL +"&datos"+i+"=" +datos[i];
        }
        System.out.println("URL insertar Usuario  BD : " + URL);
        return URL;
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

}
