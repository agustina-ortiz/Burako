package ar.edu.unlu.burako.cliente;

import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import ar.edu.unlu.burako.controlador.ControladorBurako;
import ar.edu.unlu.burako.vista.IVista;
import ar.edu.unlu.burako.vista.VistaConsola;
import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.Util;
import ar.edu.unlu.rmimvc.cliente.Cliente;

public class AppCliente {
    public static void main(String[] args) {
        ArrayList<String> ips = Util.getIpDisponibles();
        String ip = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione la IP en la que escuchará peticiones el cliente", "IP del cliente",
                JOptionPane.QUESTION_MESSAGE,
                null,
                ips.toArray(),
                null
        );
        String port = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione el puerto en el que escuchará peticiones el cliente", "Puerto del cliente",
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                9999
        );
        String ipServidor = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione la IP en la que corre el servidor", "IP del servidor",
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                null
        );
        String portServidor = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione el puerto en el que corre el servidor", "Puerto del servidor",
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                8888
        );

        ControladorBurako controlador = new ControladorBurako();
        IVista vista = new VistaConsola();
        vista.setControlador(controlador);
        controlador.setVista(vista);

        Cliente cliente = new Cliente(ip, Integer.parseInt(port), ipServidor, Integer.parseInt(portServidor));


        try {
            cliente.iniciar(controlador); // conecto al servidor
            System.out.println("Cliente conectado al servidor " + ipServidor + ":" + portServidor);

            vista.iniciar();

        } catch (RemoteException e) {
            System.err.println("Error de conexión del cliente:");
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error de conexión con el servidor", "Error", JOptionPane.ERROR_MESSAGE);

        } catch (RMIMVCException e) {
            System.err.println("Error de la librería RMI en el cliente:");
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al inicializar el cliente RMI", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
