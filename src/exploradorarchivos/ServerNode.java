
package exploradorarchivos;

import java.rmi.Remote;
import java.rmi.RemoteException;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public interface ServerNode extends Remote{
    public void mensajeServer(String mensaje)throws RemoteException;
    public boolean crearDirectorio(String nombre, DefaultMutableTreeNode padre)throws RemoteException;
    public boolean crearArchivo(String nombre, String Text, DefaultMutableTreeNode padre)throws RemoteException;
    public boolean borrarArchivo(String nombre, DefaultMutableTreeNode nodo) throws RemoteException;
    public boolean borrarDirectorio(String nombre, DefaultMutableTreeNode nodo) throws RemoteException;
    public String verArchivo(String nombre)throws RemoteException; 
    public DefaultTreeModel getEstructura()throws RemoteException; 
    
}
