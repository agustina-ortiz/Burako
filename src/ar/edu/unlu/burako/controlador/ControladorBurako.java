package ar.edu.unlu.burako.controlador;

import ar.edu.unlu.burako.modelo.*;
import ar.edu.unlu.burako.vista.IVista;
import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

import java.rmi.RemoteException;

public class ControladorBurako implements IControladorRemoto {
    private IBurako modelo;
    private IVista vista;

    public ControladorBurako() {
        // Constructor vacio q uso para RMI
    }

    public <T extends IObservableRemoto> ControladorBurako(T modelo) {
        try {
            this.setModeloRemoto(modelo);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void setVista(IVista vista) {
        this.vista = vista;
    }

    // metodos del juego con RemoteException

    public void agregarJugador(String nombre) {
        try {
            this.modelo.agregarJugador(nombre);
        } catch (RemoteException e) {
            e.printStackTrace();
            this.vista.mostrarError("Error de conexión al agregar jugador");
        }
    }

    public void iniciarPartida() {
        try {
            this.modelo.iniciarPartida();
        } catch (RemoteException e) {
            e.printStackTrace();
            this.vista.mostrarError("Error de conexión al iniciar partida");
        }
    }

    public boolean puedeTomarDelMazo() {
        try {
            return this.modelo.puedeTomarDelMazo();
        } catch (RemoteException e) {
            e.printStackTrace();
            this.vista.mostrarError("Error de conexión al verificar mazo");
            return false;
        }
    }

    public boolean puedeTomarDelPozo() {
        try {
            return this.modelo.puedeTomarDelPozo();
        } catch (RemoteException e) {
            e.printStackTrace();
            this.vista.mostrarError("Error de conexión al verificar pozo");
            return false;
        }
    }

    public void tomarCartaDelMazo() {
        try {
            if (this.modelo.puedeTomarDelMazo()) {
                this.modelo.tomarCartaDelMazo();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            this.vista.mostrarError("Error de conexión al tomar carta del mazo");
        }
    }

    public void tomarCartaDelPozo() {
        try {
            if (this.modelo.puedeTomarDelPozo()) {
                this.modelo.tomarCartaDelPozo();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            this.vista.mostrarError("Error de conexión al tomar carta del pozo");
        }
    }

    public void tomarTodoElPozo() {
        try {
            if (this.modelo.puedeTomarDelPozo()) {
                this.modelo.tomarTodoElPozo();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            this.vista.mostrarError("Error de conexión al tomar todo el pozo");
        }
    }

    public boolean puedeDescartarCarta(ICarta carta) {
        try {
            return this.modelo.puedeDescartarCarta(carta);
        } catch (RemoteException e) {
            e.printStackTrace();
            this.vista.mostrarError("Error de conexión al verificar descarte");
            return false;
        }
    }

    public void descartarCarta(ICarta carta) {
        try {
            if (this.modelo.puedeDescartarCarta(carta)) {
                this.modelo.descartarCarta(carta);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            this.vista.mostrarError("Error de conexión al descartar carta");
        }
    }

    public boolean puedeBajarJuego(ICarta[] cartas) {
        try {
            return this.modelo.puedeBajarJuego(cartas);
        } catch (RemoteException e) {
            e.printStackTrace();
            this.vista.mostrarError("Error de conexión al verificar juego");
            return false;
        }
    }

    public void bajarJuego(ICarta[] cartas) {
        try {
            if (this.modelo.puedeBajarJuego(cartas)) {
                this.modelo.bajarJuego(cartas);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            this.vista.mostrarError("Error de conexión al bajar juego");
        }
    }

    public boolean puedeApoyarCarta(ICarta carta, int jugadorId, int juegoId) {
        try {
            return this.modelo.puedeApoyarCarta(carta, jugadorId, juegoId);
        } catch (RemoteException e) {
            e.printStackTrace();
            this.vista.mostrarError("Error de conexión al verificar apoyo");
            return false;
        }
    }

    public void apoyarCarta(ICarta carta, int jugadorId, int juegoId) {
        try {
            if (this.modelo.puedeApoyarCarta(carta, jugadorId, juegoId)) {
                this.modelo.apoyarCarta(carta, jugadorId, juegoId);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            this.vista.mostrarError("Error de conexión al apoyar carta");
        }
    }

    public void pasarTurno() {
        try {
            this.modelo.pasarTurno();
        } catch (RemoteException e) {
            e.printStackTrace();
            this.vista.mostrarError("Error de conexión al pasar turno");
        }
    }

    // todos de consulta con RemoteException

    public IJugador[] getJugadores() {
        try {
            return this.modelo.getJugadores();
        } catch (RemoteException e) {
            e.printStackTrace();
            this.vista.mostrarError("Error de conexión al obtener jugadores");
            return new IJugador[0];
        }
    }

    public IJugador getJugadorActual() {
        try {
            return this.modelo.getJugadorActual();
        } catch (RemoteException e) {
            e.printStackTrace();
            this.vista.mostrarError("Error de conexión al obtener jugador actual");
            return null;
        }
    }

    public int getRonda() {
        try {
            return this.modelo.getRonda();
        } catch (RemoteException e) {
            e.printStackTrace();
            this.vista.mostrarError("Error de conexión al obtener ronda");
            return 0;
        }
    }

    public Eventos getEstadoJuego() {
        try {
            return this.modelo.getEstadoJuego();
        } catch (RemoteException e) {
            e.printStackTrace();
            this.vista.mostrarError("Error de conexión al obtener estado");
            return Eventos.ESPERANDO_JUGADORES;
        }
    }

    public boolean hayGanador() {
        try {
            return this.modelo.hayGanador();
        } catch (RemoteException e) {
            e.printStackTrace();
            this.vista.mostrarError("Error de conexión al verificar ganador");
            return false;
        }
    }

    public IJugador getGanador() {
        try {
            return this.modelo.getGanador();
        } catch (RemoteException e) {
            e.printStackTrace();
            this.vista.mostrarError("Error de conexión al obtener ganador");
            return null;
        }
    }

    // metodos de IControladorRemoto

    @Override
    public <T extends IObservableRemoto> void setModeloRemoto(T modeloRemoto) throws RemoteException {
        this.modelo = (IBurako) modeloRemoto;
    }

    @Override
    public void actualizar(IObservableRemoto observable, Object evento) throws RemoteException {
        if (evento instanceof Eventos) {
            this.vista.actualizar((Eventos) evento);
        }
    }
}