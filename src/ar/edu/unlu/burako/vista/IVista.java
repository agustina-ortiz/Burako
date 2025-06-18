package ar.edu.unlu.burako.vista;

import ar.edu.unlu.burako.controlador.ControladorBurako;
import ar.edu.unlu.burako.modelo.Eventos;

public interface IVista {
    void setControlador(ControladorBurako controlador);
    void iniciar();
    void actualizar(Eventos evento);
    void mostrarMensaje(String mensaje);
    void mostrarError(String error);
    void cerrar();
}
