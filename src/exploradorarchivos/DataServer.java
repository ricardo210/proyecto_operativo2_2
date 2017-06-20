
package exploradorarchivos;

import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;


public interface DataServer extends Remote {
    public void Mensaje(String mensaje)throws RemoteException;
    
    public File getFile(String name) throws RemoteException;

    public String getFileContent(String name) throws RemoteException;

    public boolean createFile(String content, String name) throws RemoteException;

    public boolean deleteFile(String name) throws RemoteException;

    
}
