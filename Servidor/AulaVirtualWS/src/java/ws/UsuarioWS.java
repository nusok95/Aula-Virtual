/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws;

/**
 *
 * @author Carlos
 */

import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import aulavirtual.Servidor;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.thrift.TException;

/**
 * REST Web Service
 *
 * @author susana
 */
@Path("usuario")
public class UsuarioWS {
    Servidor servidor = new Servidor();

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of UsuarioWS
     */
    public UsuarioWS() {
        
    }
    
    @POST
    @Path("registro")
    @Produces(MediaType.APPLICATION_JSON)
    public String registrarUsuario(
            @FormParam("matricula") String matricula,
            @FormParam("nombre") String nombre,
            @FormParam("apellidoPaterno") String apellidoPaterno,
            @FormParam("apellidoMaterno") String apellidoMaterno,
            @FormParam("correo") String correo,
            @FormParam("password") String password,
            @FormParam("tipoUsuario") Integer tipoUsuario ){
        Servidor servidor = new Servidor();
        return servidor.registrarse(matricula, nombre, apellidoPaterno, apellidoMaterno, correo, password, 1);
    }
    
    @POST
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    public String login(
            @FormParam("matricula") String matricula,
            @FormParam("password") String password){
        String respuesta = "";
        try {
            respuesta = servidor.iniciarSesion(matricula,password);
        } catch (TException ex) {
            Logger.getLogger(UsuarioWS.class.getName()).log(Level.SEVERE, null, ex);
        }
        return respuesta;
    }
    
    @GET
    @Path("getClases/{matricula}")
    @Produces(MediaType.APPLICATION_JSON)
    public String obtenerClasesUsuario(
            @PathParam("matricula") String matricula){
        ArrayList<String> respuesta = new ArrayList<String>();
        respuesta = servidor.obtenerClasesUsuario(matricula);
        return respuesta.toString();
    }
    
   
    
    
        
   
    }
    
