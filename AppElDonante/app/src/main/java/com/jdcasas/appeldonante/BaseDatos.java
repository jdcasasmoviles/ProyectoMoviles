package com.jdcasas.appeldonante;
public  class BaseDatos {
	String IP_Server="masterwishmaster.esy.es";//IP DE NUESTRO PC LOCALHOST
    //String IP_Server="192.168.1.42";
    String URL="" ;
    public BaseDatos(int numeroTabla){
        if(numeroTabla==1){
            URL = "http://"+IP_Server+"/Donante/serverTablaUsuarios.php";
            System.out.println("Estoy usando tabla usuarios");
        }
        else  if(numeroTabla==2){
            URL = "http://"+IP_Server+"/Donante/serverTablaHospitales.php";
            System.out.println("Estoy usando tabla hospitales");
        }
    }

     public String buscartiposangre(String tiposangre) {
         tiposangre=tiposangre.replace(" ","%20");
         tiposangre=cambiotiposangre(tiposangre);
            URL = URL + "?operacion=buscarBDDonante&tiposangre=" +tiposangre;
            System.out.println("URL buscar  BD : " + URL);
        return URL;
    }

    public String buscartiposangreCoordenadad(String tiposangre) {
        tiposangre=tiposangre.replace(" ","%20");
        tiposangre=cambiotiposangre(tiposangre);
        URL = URL + "?operacion=buscartiposangreCoordenadad&tiposangre=" +tiposangre;
        System.out.println("URL buscar  BD : " + URL);
        return URL;
    }

    public String buscardistrito(String distrito) {
        distrito=distrito.replace(" ", "%20");
        URL = URL + "?operacion=buscarBDHospital&distrito=" +distrito;
        System.out.println("URL buscar  BD x distrito : " + URL);
        return URL;
    }

    public String buscarBDUsuario(String usuario) {
        usuario=usuario.replace(" ", "%20");
        URL = URL + "?operacion=buscarBDUsuario&usuario=" +usuario;
        System.out.println("URL buscar  BD x usuario : " + URL);
        return URL;
    }

    public String buscarBDHDistritoCoordenadas(String distrito) {
        distrito=distrito.replace(" ", "%20");
        URL = URL + "?operacion=buscarBDHDistritoCoordenadas&distrito=" +distrito;
        System.out.println("URL buscar  BD hospitales buscar x distrito : " + URL);
        return URL;
    }

    public String buscarBDHidCoordenadas(String id_hospital) {
        id_hospital=id_hospital.replace(" ", "%20");
        URL = URL + "?operacion=buscarBDHidCoordenadas&id_hospital=" +id_hospital;
        System.out.println("URL buscar  BD hospitales buscar x distrito : " + URL);
        return URL;
    }

    public String buscarBDCoordenadas(String telefono) {
        telefono=telefono.replace(" ", "%20");
        URL = URL + "?operacion=buscarBDCoordenadas&telefono=" +telefono;
        System.out.println("URL buscar  BD usuario buscar x telefono : " + URL);
        return URL;
    }

    public String updateBDDisponibilidad(String usuario,String disponibilidad) {
        disponibilidad=disponibilidad.replace(" ", "%20");
        URL = URL + "?operacion=updateBDDisponibilidad&usuario=" +usuario+"&disponibilidad=" +disponibilidad;
        System.out.println("URL BD actualizar disponibilidad : " + URL);
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
