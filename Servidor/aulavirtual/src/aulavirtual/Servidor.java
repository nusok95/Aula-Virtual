package aulavirtual;

import basededatos.CargaEducativa;
import basededatos.Clase;
import basededatos.Periodo;
import basededatos.Usuario;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.apache.thrift.TException;
import org.json.simple.JSONObject;
import org.apache.thrift.TException;
import org.apache.thrift.transport.TSSLTransportFactory;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TSSLTransportFactory.TSSLTransportParameters;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor
 */

/**
 *
 * @author susana
 */
public class Servidor implements servicios.Iface{
    private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("aulavirtualPU",null);
    private UsuarioJpaController controllerCuenta = new UsuarioJpaController(entityManagerFactory);
    private CargaEducativaJpaController controllerCargaEducativa = new CargaEducativaJpaController(entityManagerFactory);
    private ClaseJpaController controllerClase = new ClaseJpaController(entityManagerFactory);
    private EntityManager entityManager = entityManagerFactory.createEntityManager();
    private HashMap<String,ArrayList<String>> direcciones = new HashMap<String,ArrayList<String>>();
    private HashMap<String,ArrayList<String>> usuarios = new HashMap<String,ArrayList<String>>();
    private String ipMaestro="";

   
 @Override
    public String iniciarSesion(String username, String password) throws TException {
      JSONObject json = new JSONObject();
      Usuario user = new Usuario();
      Query query = entityManager.createQuery("SELECT u FROM Usuario u WHERE u.password = :password AND u.matricula = :matricula");
      System.out.println(query);
      query.setParameter("matricula", username);
      query.setParameter("password", password);
      try {
         user = (Usuario) query.getSingleResult();
         json.put("success", true);
         json.put("tipo_usuario",user.getTipo());
         json.put("nombre",user.getNombre()+ " "+user.getApellidoPaterno());
      } catch (Exception ex) {
          json.put("success",false);
      }
      System.out.println(json.toString());
      return json.toString();
    }
    @Override
    public String registrarse(String matricula, String nombre, String apellidoPaterno, String apellidoMaterno, String correo, String password, int tipoUsuario) {
        JSONObject json = new JSONObject();
        UsuarioJpaController controller = new UsuarioJpaController(entityManagerFactory);
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellidoPaterno(apellidoPaterno);
        usuario.setApellidoMaterno(apellidoMaterno);
        usuario.setMatricula(matricula);
        usuario.setCorreo(correo);
        usuario.setPassword(password);
        usuario.setTipo(tipoUsuario);
        try {
            controller.create(usuario);
            json.put("success", true);
        } catch (Exception ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            json.put("success", "False");
            json.put("message", ex.toString());
        }
        System.out.println(json.toString());
        
        return json.toString();
    }
    
    @Override
    public ArrayList<String> obtenerClasesUsuario(String matricula){
        CargaEducativa cargaEducativa = new CargaEducativa();
        ArrayList<String> listaClases = new ArrayList<String>();
        JSONObject json = new JSONObject();
        Clase clase = new Clase();
        Periodo periodo = new Periodo();
        Usuario maestro = new Usuario();
        Usuario usuario = new Usuario();
        Query queryUsuario = entityManager.createQuery("SELECT u FROM Usuario u WHERE u.matricula = :matricula");
        queryUsuario.setParameter("matricula", matricula);
        usuario = (Usuario) queryUsuario.getSingleResult();
        int idUsuario = usuario.getIdUsuario();
        
        Query query = entityManager.createQuery("SELECT c FROM CargaEducativa c WHERE c.cargaEducativaPK.idUsuario = :idUsuario");
        query.setParameter("idUsuario", idUsuario);
        List <CargaEducativa> clases = query.getResultList();
        
        for(CargaEducativa clasesAux: clases){
            json.put("ROL", clasesAux.getRol());
            clase = clasesAux.getClase();
            maestro = clase.getIdMaestro();
            json.put("CLASE",clase.getNombre());
             String nombreCompleto = maestro.getNombre()+" "+maestro.getApellidoPaterno()+" "
                    +maestro.getApellidoMaterno();
            json.put("MAESTRO",nombreCompleto);
            json.put("NRC",clase.getNrc());
            periodo = clase.getIdPeriodo();
            json.put("PERIODO",periodo.getNombre());
            json.put("CARPETA_COMPARTIDA",clase.getCarpetaCompartida());
            listaClases.add(json.toString());
            json.clear();
        }
        
        
        return listaClases;
    }
    
    @Override
    public ArrayList<String> obtenerMaestros(){
      Usuario user = new Usuario();
      JSONObject json = new JSONObject();
      ArrayList<String> listaMaestros = new ArrayList<String>();
      Query query = entityManager.createQuery("SELECT u FROM Usuario u WHERE u.tipo = :tipo");
      query.setParameter("tipo",2);
      List <Usuario> maestros = query.getResultList();
        for (Usuario maestro: maestros) {
            String nombreCompleto = maestro.getNombre()+" "+maestro.getApellidoPaterno()+" "
                    +maestro.getApellidoMaterno();
            json.put(maestro.getIdUsuario(),nombreCompleto);
            listaMaestros.add(json.toString());
            json.clear();
        }
  
      return listaMaestros;
    }
    
    @Override
    public ArrayList<String> obtenerClases(){
        Clase clase = new Clase();
        Usuario maestro = new Usuario();
        int idMaestro = 0;
        ArrayList<String> listaClases = new ArrayList<String>();
        JSONObject json = new JSONObject();
        Query query = entityManager.createQuery("SELECT c FROM Clase c");
        List <Clase> clases = query.getResultList();
        for (Clase claseAux: clases) {
            json.put("ID",claseAux.getIdClase());
            json.put("NRC", claseAux.getNrc());
            json.put("NOMBRE",claseAux.getNombre());
            maestro = claseAux.getIdMaestro();
             String nombreCompleto = maestro.getNombre()+" "+maestro.getApellidoPaterno()+" "
                    +maestro.getApellidoMaterno();
            json.put("MAESTRO",nombreCompleto);
            json.put("CARPETA_COMPARTIDA", claseAux.getCarpetaCompartida());
            listaClases.add(json.toString());
            json.clear();
        }
        return listaClases;
    }
    
    @Override
    public ArrayList<String> obtenerPeriodos(){
       Periodo periodo = new Periodo();
       JSONObject json = new JSONObject();
       ArrayList<String> listaPeriodos = new ArrayList<String>();
       Query query = entityManager.createQuery("SELECT p FROM Periodo p");
       List <Periodo> periodos= query.getResultList();
        for (Periodo periodoAux: periodos) {
            json.put(periodoAux.getIdPeriodo(), periodoAux.getNombre());
            listaPeriodos.add(json.toString());
            json.clear();
        }
      return listaPeriodos;
    }
    
    public String registrarClase(String NRC, String nombreClase, int idPeriodo, String numeroPersonalMaestro,
            String carpetaCompartida){
        JSONObject json = new JSONObject();
        
        Periodo periodo = new Periodo();
        Query query = entityManager.createQuery("SELECT p FROM Periodo p WHERE p.idPeriodo = :idPeriodo");
        query.setParameter("idPeriodo", idPeriodo);
        periodo = (Periodo) query.getSingleResult();
        
        Usuario maestro = new Usuario();
        Query queryMaestro = entityManager.createQuery("SELECT u FROM Usuario u WHERE u.matricula = :matricula");
        queryMaestro.setParameter("matricula", numeroPersonalMaestro);
        maestro = (Usuario) queryMaestro.getSingleResult();
        
        Clase clase = new Clase();
        clase.setIdPeriodo(periodo);
        clase.setNombre(nombreClase);
        clase.setNrc(NRC);
        clase.setIdMaestro(maestro);
        clase.setCarpetaCompartida(carpetaCompartida);
        
         try {
            controllerClase.create(clase);
            json.put("success", true);
        } catch (Exception ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            json.put("success", "False");
            json.put("message", ex.toString());
        }
        return json.toString();        
    }
    
    @Override
    public String inscribirClase(String matricula, String NRC, String rol) {
        JSONObject json = new JSONObject();
        Usuario usuario = new Usuario();
        Clase clase = new Clase();
        Query queryUsuario = entityManager.createQuery("SELECT u FROM Usuario u WHERE u.matricula = :matricula");
        queryUsuario.setParameter("matricula", matricula);
        usuario = (Usuario) queryUsuario.getSingleResult();
        
        Query queryClase = entityManager.createQuery("SELECT c FROM Clase c WHERE c.nrc = :nrc");
        queryClase.setParameter("nrc", NRC);
        clase = (Clase) queryClase.getSingleResult();
        
        CargaEducativa cargaEducativa = new CargaEducativa();
        cargaEducativa.setUsuario(usuario);
        cargaEducativa.setClase(clase);
        cargaEducativa.setRol(rol);
        try {
            controllerCargaEducativa.create(cargaEducativa);
            json.put("success", true);
        } catch (Exception ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            json.put("success", "False");
            json.put("message", ex.toString());
        }
        return json.toString(); 
       
    }
    
    @Override
    public void enviarMensaje(String mensaje,String clase) throws TException {
        
        ArrayList<String> ipUsuarios= direcciones.get(clase);
     
        for(String ipUsuario : ipUsuarios){
            try {
                TTransport transport;
                transport = new TSocket(ipUsuario, 9091);
                System.out.println(transport);
                transport.open();

                TProtocol protocol = new  TBinaryProtocol(transport);
                servicios.Client client = new servicios.Client(protocol);

                client.recibirMensaje(mensaje);

                transport.close();
            } catch (TException x) {
              x.printStackTrace();
            } 
        }
    }





    

    @Override
    public void entrarAula(String nombre, String ip, String clase, int rol) throws TException {
            ArrayList<String> ipUsuarios;
            ArrayList<String> nombres;
        if(this.direcciones.containsKey(clase) && this.usuarios.containsKey(clase)){
            ipUsuarios = direcciones.get(clase);
            ipUsuarios.add(ip);
            nombres = usuarios.get(clase);
            nombres.add(nombre);
        }
        else{
            nombres = new ArrayList<String>();
            nombres.add(nombre);
            usuarios.put(clase, nombres);
            
            ipUsuarios = new ArrayList<String>();
            ipUsuarios.add(ip);
            direcciones.put(clase, ipUsuarios);
        }
        if(rol ==1){
            this.ipMaestro = ip;
            
        }
        
        for(String ipUsuario : ipUsuarios){
            try {
                TTransport transport;
                transport = new TSocket(ipUsuario, 9091);
                System.out.println(transport);
                transport.open();
                TProtocol protocol = new  TBinaryProtocol(transport);
                servicios.Client client = new servicios.Client(protocol);
                if(rol==1)
                      client.activarSolicitudParticipacion();
                    client.actualizarUsuariosConectados(nombres);

                transport.close();
            } catch (TException x) {
              x.printStackTrace();
            } 
            
        }
        
        
      
      
    }
    
    @Override
    public void salirAula(String nombre, String ip, String clase,int rol) throws TException {
        ArrayList<String> ipUsuarios = null;
        ArrayList<String> nombres = null;
        if(this.direcciones.containsKey(clase) && this.usuarios.containsKey(clase)){
            ipUsuarios = direcciones.get(clase);
            nombres = usuarios.get(clase);
            ipUsuarios.remove(ip);
            nombres.remove(nombre);
            
        }else{
            System.out.println("Clase no encontrada");
        }
        
        for(String ipUsuario : ipUsuarios){
            try {
            TTransport transport;
            transport = new TSocket(ipUsuario, 9091);
            System.out.println(transport);
            transport.open();
            TProtocol protocol = new  TBinaryProtocol(transport);
            servicios.Client client = new servicios.Client(protocol);
//            if(rol==1)
//                    client.activarSolicitudParticipacion();

            client.actualizarUsuariosConectados(nombres);

            transport.close();
            } catch (TException x) {
              x.printStackTrace();
            } 
            
        }
    }
    
    @Override
    public void actualizarUsuariosConectados(List<String> conectados) throws TException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public static void main(String[] args) throws Exception {
        Servidor servidor = new Servidor();
        servidor.iniciarSesion("admin","admin");
        //servidor.registrarse("s14011632", "Susana", "González", "Portilla", "susana@gmail.com", "camilo123",3);
        servidor.registrarClase("83497", "Desarrollo de sistemas web", 1, "508977", "https://drive.google.com/drive/folders/0ByECkZhfaQpVbE91amFZSkw0OHM");
        System.out.println(servidor.obtenerClases());
//        System.out.println(servidor.obtenerMaestros());
//        System.out.println(servidor.obtenerPeriodos());
//        servidor.registrarClase("231z132", "Estructuras de datos",1 ,"123412");
//        servidor.inscribirClase("s140116420", "231z132","Alumno");
//        System.out.println(servidor.obtenerClases());
//        System.out.println(servidor.obtenerClasesUsuario("s140116420"));
    
    }

    @Override
    public void recibirMensaje(String mensaje) throws TException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void pedirParticipacion(String nombre, String ip) throws TException {
        ArrayList<String> datosAlumno = new ArrayList<>();
        datosAlumno.add(nombre);
        datosAlumno.add(ip);
        try {
            TTransport transport;
            transport = new TSocket(ipMaestro, 9091);
            System.out.println(transport);
            transport.open();
            TProtocol protocol = new  TBinaryProtocol(transport);
            servicios.Client client = new servicios.Client(protocol);
            
            client.mostrarSolicitudParticipacion(datosAlumno);
            //client.mostrarSolicitudParticipacion();

            transport.close();
        } catch (TException x) {
              x.printStackTrace();
        } 
    }

    @Override
    public void mostrarSolicitudParticipacion(List<String> datosAlumno) throws TException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void otorgarParticipacion(String ipAlumno) throws TException {
        try {
            TTransport transport;
            transport = new TSocket(ipMaestro, 9091);
            System.out.println(transport);
            transport.open();
            TProtocol protocol = new  TBinaryProtocol(transport);
            servicios.Client client = new servicios.Client(protocol);
            
            client.dejarControl();
            //client.mostrarSolicitudParticipacion();

            transport.close();
        } catch (TException x) {
              x.printStackTrace();
        } 
        
        try {
            TTransport transport;
            transport = new TSocket(ipAlumno, 9091);
            System.out.println(transport);
            transport.open();
            TProtocol protocol = new  TBinaryProtocol(transport);
            servicios.Client client = new servicios.Client(protocol);
            
            client.obtenerControl();
            //client.mostrarSolicitudParticipacion();

            transport.close();
        } catch (TException x) {
              x.printStackTrace();
        } 
    }

    @Override
    public void mostrarParticipacion(String ipAlumno) throws TException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void pedirControl(String ipAlumno) throws TException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void recuperarControl(String ipAlumno) throws TException {
        try {
            TTransport transport;
            transport = new TSocket(ipAlumno, 9091);
            System.out.println(transport);
            transport.open();
            TProtocol protocol = new  TBinaryProtocol(transport);
            servicios.Client client = new servicios.Client(protocol);
            
            client.dejarControl();
            //client.mostrarSolicitudParticipacion();

            transport.close();
        } catch (TException x) {
              x.printStackTrace();
        } 
        
        try {
            TTransport transport;
            transport = new TSocket(ipMaestro, 9091);
            System.out.println(transport);
            transport.open();
            TProtocol protocol = new  TBinaryProtocol(transport);
            servicios.Client client = new servicios.Client(protocol);
            
            client.obtenerControl();
            //client.mostrarSolicitudParticipacion();

            transport.close();
        } catch (TException x) {
              x.printStackTrace();
        } 
    }

    @Override
    public void actualizarParticipantes() throws TException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    

    @Override
    public void activarSolicitudParticipacion() throws TException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void desactivarSolicitudParticipacion() throws TException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void obtenerControl() throws TException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void dejarControl() throws TException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String registrarClase(String NRC, String nombreClase, int idPeriodo, String numeroPersonalMaestro) throws TException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    
}
