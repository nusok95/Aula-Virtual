/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aulavirtual;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Random;

/**
 *
 * @author Carlos
 */ 
public class ServidorTest {
    
    public ServidorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Prueba de unidad del método IniciarSesión de la clase Servidor
     */
//    @Test
//    public void testIniciarSesion() throws Exception {
//        System.out.println("iniciarSesion");
//        String username = "s14011642";
//        String password = "12345";
//        Servidor instance = new Servidor();
//        boolean expResult = true;
//        boolean result = instance.iniciarSesion(username, password);
//        assertEquals(expResult, result);
//        
//    }
    
     /**
     * Prueba de unidad del método IniciarSesión de la clase Servidor
     */
    @Test
    public void testIniciarSesionWrongUsername() throws Exception {
        }

    /**
     * Test of registrarse method, of class Servidor.
     */
    @Test
    public void testRegistrarse() {
       
    }

    /**
     * Prueba de unidad del método Registrarse con una matricula ya existente
     *
     */
     @Test
    public void testRegistrarseRepeatedMatricula() {
       
    }
    
     /**
     * Prueba de unidad del método Registrarse con una matricula ya existente
     *
     */
     @Test
    public void testRegistrarseFieldTooLong() {
       
    }

    
}
