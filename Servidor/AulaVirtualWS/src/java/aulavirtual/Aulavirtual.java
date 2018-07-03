/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aulavirtual;

import basededatos.Usuario;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;

/**
 *
 * @author susana
 */
public class Aulavirtual {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        
        try {
            TServerTransport serverTransport = new TServerSocket(9090);
            servicios.Processor processor = new servicios.Processor(new Servidor());
            TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));

            // Use this for a multithreaded server
            // TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));
            System.out.println("Starting the simple server...");
            server.serve();
        } catch (TTransportException e) {
            e.printStackTrace();
        }
    }
    
}
