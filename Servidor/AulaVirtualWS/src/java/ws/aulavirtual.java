/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws;

import aulavirtual.Servidor;
import java.util.ArrayList;
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

/**
 * REST Web Service
 *
 * @author Carlos
 */
@Path("aulavirtual")
public class aulavirtual {
    Servidor servidor = new Servidor();

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of aulavirtual
     */
    public aulavirtual() {
    }

    @GET
    @Path("maestros")
    @Produces(MediaType.APPLICATION_JSON)
    public String obtenerMaestros(){
        ArrayList<String> respuesta = new ArrayList<String>();
        respuesta = servidor.obtenerMaestros();
        return respuesta.toString();
    }
    
    
    @GET
    @Path("clases")
    @Produces(MediaType.APPLICATION_JSON)
    public String obtenerClases(){
        ArrayList<String> respuesta = new ArrayList<String>();
        respuesta = servidor.obtenerClases();
        return respuesta.toString();
    }
    
    @GET
    @Path("periodos")
    @Produces(MediaType.APPLICATION_JSON)
    public String obtenerPeriodos(){
        ArrayList<String> respuesta = new ArrayList<String>();
        respuesta = servidor.obtenerPeriodos();
        return respuesta.toString();
    }
    
    
    @POST
    @Path("registrarclase")
    @Produces(MediaType.APPLICATION_JSON)
    public String registrarClase(
            @FormParam("nrc") String nrc,
            @FormParam("nombre") String nombre,
            @FormParam("idPeriodo") Integer idPeriodo,
            @FormParam("numeroPersonalMaestro") String numeroPersonalMaestro,
            @FormParam("carpetaCompartida") String carpetaCompartida){
        Servidor servidor = new Servidor();
        return servidor.registrarClase(nrc, nombre, 0, numeroPersonalMaestro, carpetaCompartida);
    }
    
    /**
     * inscribirClase(String matricula, String NRC, String rol)
     */
    @POST
    @Path("inscribirclase")
    @Produces(MediaType.APPLICATION_JSON)
    public String InscribirClase(
            @FormParam("matricula") String matricula,
            @FormParam("nrc") String nrc,
            @FormParam("rol") String rol){
        Servidor servidor = new Servidor();
        return servidor.inscribirClase(matricula, nrc, rol);
    }
    
    
    
    

}
