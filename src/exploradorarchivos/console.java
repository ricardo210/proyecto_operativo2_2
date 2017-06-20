package exploradorarchivos;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultTreeModel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author guitart
 */
public class console {
    public static void main(String[] args) {
        try {
            while(true){
                Registry repositorio = LocateRegistry.getRegistry("127.0.0.1", 1099);
                ServerNode skeleton = (ServerNode)repositorio.lookup("ServerNode");
                //DefaultTreeModel estructura = mensaje.getEstructura();
                getConsole(skeleton,"ip:port/");
            }
        } catch (RemoteException ex) {
            Logger.getLogger(console.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(console.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private static String getConsole(ServerNode serverSkeleton, String directory) throws RemoteException {
        Scanner sc = new Scanner(System.in);
        System.out.print(directory + "$ ");
        String input = sc.nextLine();
        String[] tokens = input.split(" ");
        switch(tokens[0]){
            case "msg":
                if(tokens.length == 2){
                    serverSkeleton.mensajeServer(tokens[1]);
                    return "sent";
                }
                break;
            case "mkdir":
                if(tokens.length < 2){
                    return "not created";
                }
                if(true)
                    System.out.println("created succesfully");
                else
                    System.out.println("not created");
                break;
        }
        return "command not found";
    }
    /*private static void agregarDirectorio(String nombre){
        try {
            mensaje.crearDirectorio(((NodoArbol)nodo_seleccionado2.getUserObject()).getNombre()+"/"+nombre,nodo_seleccionado2);
            cargarArbol();
        } catch (RemoteException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/
}
