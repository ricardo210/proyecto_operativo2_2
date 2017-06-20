
package exploradorarchivos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DataServer1 extends UnicastRemoteObject implements DataServer {
 
    public DataServer1(File directorio) throws RemoteException {
        super();
        this.directorio = directorio;
        
    }
    
    public File getDataDirectory() {
        return directorio;
    }

    public void setDataDirectory(File dataDirectory2) {
        directorio = dataDirectory2;
    }
     
    @Override
    public void Mensaje(String mensaje) throws RemoteException {
        System.out.println(mensaje);
    }
    private static File directorio;
    private static Registry reg;
    
    public static void main(String args[]){

        try {
            directorio = new File("./Data/DataServer1");
            if (!directorio.exists()) {
                try {
                    directorio.mkdirs();
                } catch (Exception e) {}
            }
            reg = LocateRegistry.createRegistry(1100);
            reg.rebind("DataServer1", new DataServer1(directorio));
            System.out.println("DataServer1 conectado");
        } catch (Exception e) {
            System.out.println(e);
        }
  
     
    }
    
    @Override
    public File getFile(String name) throws RemoteException {
        for (final File fileEntry : directorio.listFiles()) {
            if (fileEntry.getName().equals(name)) {
                return fileEntry;
            }
        }
        System.err.println("No se encontro el archivo");
        return null;
    }

    @Override
    public boolean createFile(String content, String name) throws RemoteException {
        PrintWriter writer = null;
        String[] path = name.split("/");
        name = directorio.getAbsolutePath() + "\\";
        for (int i = 1; i < path.length; i++) {
            name += path[i];
            if (name.endsWith(".txt")) {
                try {
                    writer = new PrintWriter(name, "UTF-8");
                } catch (FileNotFoundException | UnsupportedEncodingException ex) {
                    Logger.getLogger(DataServer1.class.getName()).log(Level.SEVERE, null, ex);
                    return false;
                }
                writer.println(content);
                writer.close();
            } else {
                File newFile = new File(name);
                name += "\\";
                if (!newFile.exists()) {
                    newFile.mkdir();
                }
            }
        }

        System.out.println("Nombre del Archivo: " + name);
      
        return true;
    }

    @Override
    public boolean deleteFile(String name) throws RemoteException {
        name = directorio.getAbsolutePath() + "\\" + name;
        File file = new File(name);
        return file.delete();
    }

    @Override
    public String getFileContent(String name) throws RemoteException {
        String collectedInfo = "";
        try {
            String line = "";
            name = directorio.getAbsolutePath() + "\\" + name;
            FileReader f = new FileReader(name);
            try (BufferedReader b = new BufferedReader(f)) {
                while ((line = b.readLine()) != null) {
                    collectedInfo += line;
                }
            }
        } catch (IOException ex) {
        }
        return collectedInfo;
    }

    private void delete(File f) throws IOException {
        if (f.isDirectory()) {
            for (File c : f.listFiles()) {
                delete(c);
            }
        }
        if (!f.delete()) {
            throw new FileNotFoundException("Failed to delete file: " + f);
        }
    }
}
