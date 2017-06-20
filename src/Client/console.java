package Client;

import ServerNode.RMI;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        String serverIP = "localhost";
        int port = 1101;
        try {
            Registry reg = LocateRegistry.getRegistry(serverIP, port);
            RMI serverSkeleton = (RMI)reg.lookup("server");
            serverSkeleton.printInServerSide("clientConnected");
            String response = "";
            while(!response.contentEquals("exit")){
                response = getConsole(serverSkeleton, "/");
            }
        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(console.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static String getConsole(RMI serverSkeleton, String directory) throws RemoteException {
        Scanner sc = new Scanner(System.in);
        System.out.print(directory + "$ ");
        String input = sc.nextLine();
        String[] tokens = input.split(" ");
        switch(tokens[0]){
            case "msg":
                if(tokens.length == 2){
                    serverSkeleton.printInServerSide(tokens[1]);
                    return "sent";
                }
                break;
            case "mkdir":
                if(tokens.length < 2){
                    return "not created";
                }
                if(serverSkeleton.addDirectory(null, tokens[1]))
                    System.out.println("created succesfully");
                else
                    System.out.println("not created");
                break;
        }
        return "command not found";
    }
}
