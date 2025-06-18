package ar.edu.unlu.burako.modelo;

import java.io.Serializable;
import java.util.ArrayList;

public class Jugador implements IJugador, Serializable {
    private static final long serialVersionUID = 1L;

    private static int ID = 0;
    private String nombre;
    private int id;
    private ArrayList<Carta> atril; //Cartas que tiene el jugador sin bajar
    private ArrayList<Carta> muerto;
    private ArrayList<ArrayList<Carta>> juegosBajados; //Cada juego que bajo es un Array de cartas
    private int puntuacion;
    private boolean haTomadoMuerto;

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.id = Jugador.ID++;
        this.atril = new ArrayList<>();
        this.muerto = new ArrayList<>();
        this.juegosBajados = new ArrayList<>();
        this.puntuacion = 0;
        this.haTomadoMuerto = false;
    }

    @Override
    public String getNombre(){
        return nombre;
    }

    @Override
    public ICarta[] getCartasEnAtril() {
        ICarta[] cartas = new ICarta[atril.size()];
        return atril.toArray(cartas);
    }

    public boolean tieneCartasEnAtril(){
        return !atril.isEmpty();
    }

    public int cantidadCartasEnAtril(){
        return atril.size();
    }

    public int cantidadJuegosBajados(){
        return juegosBajados.size();
    }

    @Override
    public ArrayList<Carta>[] getJuegosBajados() {
        ArrayList<Carta>[] array = new ArrayList[juegosBajados.size()];
        return juegosBajados.toArray(array);
    }

    @Override
    public int getPuntuacion(){
        return puntuacion;
    }

    @Override
    public boolean tieneCanasta(){
        for(ArrayList<Carta> juego : juegosBajados){
            if (juego.size() >= 7){
                return true;
            }
        }
        return false;
    }

    // metodos para manejar las cartas en mano del jugador

    public void agregarCarta(Carta carta){
        this.atril.add(carta);
    }

    public void agregarCartaAlMuerto(Carta carta){
        this.muerto.add(carta);
    }

    public void sacarCarta(Carta carta){
        this.atril.remove(carta);
         // si se quedó sin cartas y no tomo el muerto lo toma automaticamente
        if(atril.isEmpty() && !haTomadoMuerto){
            this.tomoMuerto();
        }
    }

    public void tomarMuerto(){
        this.atril.addAll(muerto);
        this.muerto.clear();
        this.haTomadoMuerto = true;
        this.sumarPuntos(100);
    }

    // metodos para los juegos bajados

    public void bajarJuego(ArrayList<Carta> juego){
        for(Carta carta : juego){
            this.atril.remove(carta);
        }
        this.juegosBajados.add(juego);
    }

    public void sumarCartaJuegoBajado(Carta carta, int indiceJuego){
        if (indiceJuego >= 0 && indiceJuego < juegosBajados.size()) {
            this.juegosBajados.get(indiceJuego).add(carta);
            this.atril.remove(carta);
        }
    }

    //metodos para contar la puntuacion

    public void sumarPuntos(int puntos){
        this.puntuacion += puntos;
    }

    public void restarPuntos(int puntos){
        this.puntuacion -= puntos;
    }

    public int calcularPuntos() {
        int puntos = 0;

        // Puntos por juegos bajados
        for (ArrayList<Carta> juego : juegosBajados) {
            if (juego != null && !juego.isEmpty()) {
                puntos += calcularPuntosJuego(juego);
            }
        }

        // Restar cartas en mano
        for (Carta carta : atril) {
            puntos -= calcularPuntosCarta(carta);
        }

        return puntos;
    }

    private int calcularPuntosJuego(ArrayList<Carta> juego) {
        int puntos = 0;
        boolean tieneComodin = false;

        // sumar puntos de cada carta
        for (Carta carta : juego) {
            puntos += calcularPuntosCarta(carta);
            if (carta.esComodin()) {
                tieneComodin = true;
            }
        }

        // puntos por canastas (7+ cartas)
        if (juego.size() >= 7) {
            if (tieneComodin) {
                puntos += 100; // Canasta impura
            } else {
                puntos += 200; // Canasta pura
            }
        }

        return puntos;
    }

    private int calcularPuntosCarta(Carta carta) {
        if (carta.esComodin()) {
            return 50;
        }

        int numero = carta.getNumero();
        if (numero == 1) {
            return 15;
        } else if (numero == 2) {
            return 20;
        } else if (numero >= 3 && numero <= 7) {
            return 5;
        } else { // 8-13
            return 10;
        }
    }

    //metodos para manejar el muerto

    public boolean tomoMuerto(){
        return haTomadoMuerto;
    }

    public void marcarMuertoTomado(){
        this.haTomadoMuerto = true;
    }

    public void reiniciarParaMano(){
        this.atril.clear();
        this.muerto.clear();
        this.juegosBajados.clear();
        this.haTomadoMuerto = false;
    }

    @Override
    public String toString(){
        return nombre + " (ID: " + id + ", Puntos: " + puntuacion + ")";
    }
}
