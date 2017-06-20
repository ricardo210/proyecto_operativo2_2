
package exploradorarchivos;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.AccessException;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;


public class ServerNodeImpl extends UnicastRemoteObject implements ServerNode{
    private static HashMap<String, registro> registros = new HashMap();
    
    private static DataServer dataserver1;
    private static DataServer dataserver2;
    private static DataServer dataserver3;
    private static DataServer dataserver4;

    private static DefaultTreeModel directorio;
     
    public ServerNodeImpl() throws RemoteException {
        super();
        directorio = new DefaultTreeModel(new DefaultMutableTreeNode(new NodoArbol("root",-1,'d')));
        File archivo = null;
        try{
            archivo = new File ("directorio.bin");
            if (!archivo.exists()){
                FileOutputStream salida = new FileOutputStream(archivo);
                ObjectOutputStream objeto = new ObjectOutputStream(salida);
                objeto.writeObject(directorio);
                objeto.flush();
                objeto.close();
                salida.close();
            }else{
                FileInputStream entrada = new FileInputStream(archivo);
                ObjectInputStream objeto = new ObjectInputStream(entrada);
                try{
                    directorio = (DefaultTreeModel)objeto.readObject();
                }catch(ClassNotFoundException e){
                    //encontro el final del binario
                }catch (EOFException e){
                }
                finally{
                    objeto.close();
                    entrada.close();
                }
            }
        }catch(Exception e){
        
        }
    }
    
    
    @Override
    public void mensajeServer(String mensaje) throws RemoteException {
        System.out.println(mensaje);
    }
    
    private TreePath find(DefaultMutableTreeNode root, DefaultMutableTreeNode search) {
        @SuppressWarnings("unchecked")
        Enumeration<DefaultMutableTreeNode> e = root.depthFirstEnumeration();
        while (e.hasMoreElements()) {
            DefaultMutableTreeNode node = e.nextElement();
            if (((NodoArbol)node.getUserObject()).getNombre().equals(((NodoArbol)search.getUserObject()).getNombre())) {
                return new TreePath(node.getPath());
            }
        }        
        return null;
    }

    @Override
    public boolean crearDirectorio(String nombre, DefaultMutableTreeNode padre) throws RemoteException {
        //sincroniza el servidor con la vista en el cliente
        TreePath camino = find((DefaultMutableTreeNode)directorio.getRoot(),padre);
        DefaultMutableTreeNode selectedNode = ((DefaultMutableTreeNode)camino.getLastPathComponent());
        int servidor = 1;
        selectedNode.add(new DefaultMutableTreeNode(new NodoArbol(nombre,servidor,'d')));
        System.out.println(nombre);
        new File("directorio.bin").delete();
        File archivo = null;
        try{
            archivo = new File ("directorio.bin");            
            FileOutputStream salida = new FileOutputStream(archivo);
            ObjectOutputStream objeto = new ObjectOutputStream(salida);
            objeto.writeObject(directorio);
            objeto.flush();
            objeto.close();
            salida.close();
            
        }catch(Exception ex3){
        
        }
        //codigo para guardar en el data node
        return true;
    }

    @Override
    public boolean crearArchivo(String nombre, String Text, DefaultMutableTreeNode padre) throws RemoteException {
        //sincroniza el servidor con la vista en el cliente
        TreePath camino = find((DefaultMutableTreeNode)directorio.getRoot(),padre);
        DefaultMutableTreeNode selectedNode = ((DefaultMutableTreeNode)camino.getLastPathComponent());
         Random R = new Random();        
        int random = R.nextInt(2);
        NodoArbol neo = new NodoArbol(nombre,random,'a');
        System.out.println(random);
        selectedNode.add(new DefaultMutableTreeNode(neo));
        new File("directorio.bin").delete();
        File archivo = null;
        try{
            archivo = new File ("directorio.bin");            
            FileOutputStream salida = new FileOutputStream(archivo);
            ObjectOutputStream objeto = new ObjectOutputStream(salida);
            objeto.writeObject(directorio);
            objeto.flush();
            objeto.close();
            salida.close();
            
        }catch(Exception ex3){
        
        }
        
        try {
            Registry repositorio1;
            Registry repositorio2;
            if(random == 0){
                repositorio1 = LocateRegistry.getRegistry("", 1100);
                repositorio2 = LocateRegistry.getRegistry("", 1101);
            }else{
                repositorio1 = LocateRegistry.getRegistry("", 1102);
                repositorio2 = LocateRegistry.getRegistry("", 1103);
            }
            DataServer mensaje1;
            DataServer mensaje2;
            try {
                if(random == 0){
                    mensaje1 = (DataServer)repositorio1.lookup("DataServer1");
                    mensaje1.createFile(Text, nombre);

                }else{
                    mensaje1 = (DataServer)repositorio1.lookup("DataServer2");
                    mensaje1.createFile(Text, nombre);
                    
                }
            } catch (NotBoundException ex) {
                Logger.getLogger(ServerNodeImpl.class.getName()).log(Level.SEVERE, null, ex);
            } catch (AccessException ex) {
                Logger.getLogger(ServerNodeImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
                    
            
        } catch (RemoteException ex) {        
        }
       return true;
    }

    @Override
    public boolean borrarArchivo(String nombre, DefaultMutableTreeNode nodo) throws RemoteException {
        TreePath camino = find((DefaultMutableTreeNode)directorio.getRoot(),nodo);
        DefaultMutableTreeNode selectedNode = ((DefaultMutableTreeNode)camino.getLastPathComponent());
        ((DefaultMutableTreeNode)selectedNode.getParent()).remove(selectedNode);
        new File("directorio.bin").delete();
        File archivo = null;
        try{
            archivo = new File ("directorio.bin");            
            FileOutputStream salida = new FileOutputStream(archivo);
            ObjectOutputStream objeto = new ObjectOutputStream(salida);
            objeto.writeObject(directorio);
            objeto.flush();
            objeto.close();
            salida.close();
            
        }catch(Exception ex3){
        
        }
        //codigo para eliminar en el dataserver
        
        int random = ((NodoArbol)nodo.getUserObject()).getDataServer();
        System.out.println(random);
        try {
            Registry repositorio1;
            Registry repositorio2;
            if(random == 0){
                repositorio1 = LocateRegistry.getRegistry("", 1100);
                repositorio2 = LocateRegistry.getRegistry("", 1101);
            }else{
                repositorio1 = LocateRegistry.getRegistry("", 1102);
                repositorio2 = LocateRegistry.getRegistry("", 1103);
            }
            DataServer mensaje1;
            DataServer mensaje2;
            try {
                if(random == 0){
                    mensaje1 = (DataServer)repositorio1.lookup("DataServer1");
                    mensaje1.deleteFile(nombre);
                  
                }else{
                    System.out.println("entra");
                    mensaje1 = (DataServer)repositorio1.lookup("DataServer2");
                    mensaje1.deleteFile(nombre);
                
                }
            } catch (NotBoundException ex) {
                Logger.getLogger(ServerNodeImpl.class.getName()).log(Level.SEVERE, null, ex);
            } catch (AccessException ex) {
                Logger.getLogger(ServerNodeImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
                    
            
        } catch (RemoteException ex) {        
        }
       return true;
    }

    @Override
    public boolean borrarDirectorio(String nombre, DefaultMutableTreeNode nodo) throws RemoteException {
        TreePath camino = find((DefaultMutableTreeNode)directorio.getRoot(),nodo);
        DefaultMutableTreeNode selectedNode = ((DefaultMutableTreeNode)camino.getLastPathComponent());
        ((DefaultMutableTreeNode)selectedNode.getParent()).remove(selectedNode);
        new File("directorio.bin").delete();
        File archivo = null;
        try{
            archivo = new File ("directorio.bin");            
            FileOutputStream salida = new FileOutputStream(archivo);
            ObjectOutputStream objeto = new ObjectOutputStream(salida);
            objeto.writeObject(directorio);
            objeto.flush();
            objeto.close();
            salida.close();
            
        }catch(Exception ex3){
        
        }
        //codigo para eliminar en el dataserver
        
       return true;
    }

    @Override
    public String verArchivo(String nombre) throws RemoteException {
       return "";
    }
    
    @Override
    public DefaultTreeModel getEstructura() throws RemoteException {
        return directorio;
    }
     private static void iniciar(){
        
       String host = "";
        try {
            // create on port 1099
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("ServerNode", new ServerNodeImpl()); 
            System.out.println("ServerNode iniciado"); 
            
            Registry reg2 = LocateRegistry.getRegistry(host, 1100); // agregar host
            try{
                dataserver1 = (DataServer) reg2.lookup("DataServer1");
                System.out.println("Data server 1 conectado");
            }catch(ConnectException e){
                System.out.println("Data Server 1 refused connection!");
            }

            Registry reg4 = LocateRegistry.getRegistry(host,1102);
            try{
                dataserver2 = (DataServer) reg4.lookup("DataServer2");
                System.out.println("Data server 2 conectado");
            }catch(ConnectException e){
                System.out.println("Data Server 2 refused connection!");
            }
            
            Registry reg6 = LocateRegistry.getRegistry(host,1104);
            try{
                dataserver3 = (DataServer) reg6.lookup("DataServer3");
                System.out.println("Data server 3 conectado");
            }catch(ConnectException e){
                System.out.println("Data Server 3 refused connection!");
            }
            
            Registry reg8 = LocateRegistry.getRegistry(host,1106);
            try{
                dataserver4 = (DataServer) reg8.lookup("DataServer4");
                System.out.println("Data server 4 conectado");
            }catch(ConnectException e){
                System.out.println("Data Server 4 refused connection!");
            }
            
            dataserver1.Mensaje("Hola "+registros.get("DataServer1").getId());
            dataserver2.Mensaje("Hola "+registros.get("DataServer2").getId());
            dataserver3.Mensaje("Hola "+registros.get("DataServer3").getId());
            dataserver4.Mensaje("Hola "+registros.get("DataServer4").getId());
            
        } catch (Exception e) {
            e.printStackTrace();
        }              
    }
    
    public static void main(String args[]) {
        registros.put("ServerNode", new registro(1));
        registros.put("DataServer1", new registro(2));
        registros.put("DataServer2", new registro(4));
        registros.put("DataServer3", new registro(6));
        registros.put("DataServer4", new registro(8));
        
        iniciar();   
    }  
  
}
