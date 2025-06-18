package ar.edu.unlu.burako.modelo;

import ar.edu.unlu.rmimvc.observer.IObservableRemoto;
import java.rmi.RemoteException;

public interface IBurako extends IObservableRemoto {
    void iniciarPartida() throws RemoteException;
    void agregarJugador(String nombre) throws RemoteException;
    IJugador[] getJugadores() throws RemoteException;
    IJugador getJugadorActual() throws RemoteException;
    boolean puedeTomarDelMazo() throws RemoteException;
    boolean puedeTomarDelPozo() throws RemoteException;
    void tomarCartaDelMazo() throws RemoteException;
    void tomarCartaDelPozo() throws RemoteException;
    void tomarTodoElPozo() throws RemoteException;
    boolean puedeDescartarCarta(ICarta carta) throws RemoteException;
    void descartarCarta(ICarta carta) throws RemoteException;
    boolean puedeBajarJuego(ICarta[] cartas) throws RemoteException;
    void bajarJuego(ICarta[] cartas) throws RemoteException;
    boolean puedeApoyarCarta(ICarta carta, int jugadorId, int juegoId) throws RemoteException;
    void apoyarCarta(ICarta carta, int jugadorId, int juegoId) throws RemoteException;
    void pasarTurno() throws RemoteException;
    boolean hayGanador() throws RemoteException;
    IJugador getGanador() throws RemoteException;
    int getRonda() throws RemoteException;
    Eventos getEstadoJuego() throws RemoteException;
}
