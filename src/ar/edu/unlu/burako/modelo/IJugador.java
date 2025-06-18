package ar.edu.unlu.burako.modelo;

import java.util.ArrayList;

public interface IJugador {
    String getNombre();
    int getPuntuacion();
    void sumarPuntos(int puntos);
    void restarPuntos(int puntos);
    int calcularPuntos();
    boolean tomoMuerto();
    boolean tieneCanasta();
    ICarta[] getCartasEnAtril();
    int cantidadJuegosBajados();
    ArrayList<Carta>[] getJuegosBajados();
}
