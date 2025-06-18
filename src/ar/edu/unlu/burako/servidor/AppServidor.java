package ar.edu.unlu.burako.servidor;

import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import ar.edu.unlu.burako.modelo.Burako;
import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.Util;
import ar.edu.unlu.rmimvc.servidor.Servidor;

public class AppServidor {
    public static void main(String[] args) {
        ArrayList<String> ips = Util.getIpDisponibles();
        String ip = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione la IP en la que escuchará peticiones el servidor", "IP del servidor",
                JOptionPane.QUESTION_MESSAGE,
                null,
                ips.toArray(),
                null
        );
        String port = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione el puerto en el que escuchará peticiones el servidor", "Puerto del servidor",
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                8888
        );

        try {
            Burako modelo = new Burako();
            Servidor servidor = new Servidor(ip, Integer.parseInt(port));
            servidor.iniciar(modelo);

            System.out.println("Servidor Burako iniciado en " + ip + ":" + port);
            System.out.println("Esperando conexiones de clientes...");

        } catch (RemoteException e) {
            System.err.println("Error de conexión en el servidor:");
            e.printStackTrace();
        } catch (RMIMVCException e) {
            System.err.println("Error de la librería RMI:");
            e.printStackTrace();
        }
    }
}
