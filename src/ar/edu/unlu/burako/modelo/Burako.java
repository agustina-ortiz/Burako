package ar.edu.unlu.burako.modelo;

import ar.edu.unlu.rmimvc.observer.ObservableRemoto;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class Burako extends ObservableRemoto implements IBurako {
    private ArrayList<Jugador> jugadores;
    private Mazo mazo;
    private Pozo pozo;
    private int jugadorActualIndex;
    private int ronda;
    private Eventos estadoJuego;
    private boolean partidaIniciada;
    private boolean cartaTomada;

    public Burako() throws RemoteException {
        this.jugadores = new ArrayList<>();
        this.mazo = new Mazo();
        this.pozo = new Pozo();
        this.jugadorActualIndex = 0;
        this.ronda = 1;
        this.estadoJuego = Eventos.ESPERANDO_JUGADORES;
        this.partidaIniciada = false;
        this.cartaTomada = false;
    }

    @Override
    public void agregarJugador(String nombre) throws RemoteException {
        if(jugadores.size() >= 4){
            return;
        }
        jugadores.add(new Jugador(nombre));

        if(jugadores.size() >= 2){
            this.estadoJuego = Eventos.LISTO_PARA_INICIAR;
        }
    }

    @Override
    public void iniciarPartida() throws RemoteException{
        if(jugadores.size() < 2){
            return;
        }

        this.repartirCartas();
        this.partidaIniciada = true;
        this.estadoJuego = Eventos.TURNO_JUGADOR;
    }

    private void repartirCartas(){
        for (Jugador jugador : jugadores){
            for (int i = 0; i < 11; i++){     // 11 cartas al atril x jugador
                jugador.agregarCarta(mazo.tomarCarta());
            }
            for (int i = 0; i < 11; i++){     // 11 cartas al muerto x jugador
                jugador.agregarCartaAlMuerto(mazo.tomarCarta());
            }
        }
    }

    @Override
    public IJugador[] getJugadores() throws RemoteException {
        IJugador[] array = new IJugador[jugadores.size()];
        return jugadores.toArray(array);
    }

    @Override
    public IJugador getJugadorActual() throws RemoteException {
        if (jugadores.isEmpty()){
            return null;
        }
        return jugadores.get(jugadorActualIndex);
    }

    @Override
    public boolean puedeTomarDelMazo() throws RemoteException {
        return partidaIniciada && !cartaTomada && !mazo.estaVacio();
    }

    @Override
    public boolean puedeTomarDelPozo() throws RemoteException {
        return partidaIniciada && !cartaTomada && !pozo.estaVacio();
    }

    @Override
    public void tomarCartaDelMazo() throws RemoteException {
        if (!puedeTomarDelMazo()){
            return;
        }

        Carta carta = mazo.tomarCarta();
        jugadores.get(jugadorActualIndex).agregarCarta(carta);
        this.cartaTomada = true;
    }

    @Override
    public void tomarCartaDelPozo() throws RemoteException {
        if(!puedeTomarDelPozo()){
            return;
        }

        Carta carta = pozo.tomarUltimaCarta();
        jugadores.get(jugadorActualIndex).agregarCarta(carta);
        this.cartaTomada = true;
    }

    @Override
    public void tomarTodoElPozo() throws RemoteException {
        if (!puedeTomarDelPozo()){
            return;
        }

        ArrayList<Carta> todasLasCartas = pozo.tomarTodoElPozo();
        for (Carta carta : todasLasCartas) {
            jugadores.get(jugadorActualIndex).agregarCarta(carta);
        }
        this.cartaTomada = true;
    }

    @Override
    public boolean puedeDescartarCarta(ICarta carta) throws RemoteException {
        return partidaIniciada && cartaTomada;
    }

    @Override
    public void descartarCarta(ICarta carta) throws RemoteException {
        if(!puedeDescartarCarta(carta)){
            return;
        }

        Jugador jugadorActual = jugadores.get(jugadorActualIndex);
        jugadorActual.sacarCarta((Carta)carta); //casteo de tipo ICarta a Carta
        pozo.agregarCarta((Carta)carta);

        if (verificarVictoria(jugadorActual)){
            this.estadoJuego = Eventos.PARTIDA_TERMINADA;
            return;
        }
        this.pasarTurno();
    }

    @Override
    public void pasarTurno() throws RemoteException {
        this.cartaTomada = false;
        this.jugadorActualIndex = (jugadorActualIndex + 1) % jugadores.size();
    }

    private boolean verificarVictoria(Jugador jugador){
        if (jugador.cantidadCartasEnAtril() == 0 && jugador.tomoMuerto() && jugador.tieneCanasta()){
            jugador.sumarPuntos(100); //100 puntos por cerrar
            calcularPuntosMano();
            for (Jugador j : jugadores){
                if(j.getPuntuacion() >= 3000){ //verificar si alguien llegó a 3000  puntos
                    return true;
                }
            }
            iniciarNuevaMano(); //si nadie llegó inicio otra mano
        }
        return false;
    }

    private void calcularPuntosMano(){
        for(Jugador jugador : jugadores){
            // TODO: Implementar cálculo de puntos por escaleras, piernas, canastas
            //por ahora solo restamos cartas del atril
            ICarta[] cartasEnAtril = jugador.getCartasEnAtril();
            for (ICarta carta : cartasEnAtril){
                jugador.restarPuntos(carta.getPuntos());
            }
        }
    }

    private void iniciarNuevaMano(){
        for (Jugador jugador : jugadores){
            jugador.reiniciarParaMano();
        }
        //crear nuevo mazo y nuevo pozo
        this.mazo = new Mazo();
        this.pozo = new Pozo();
        this.ronda++;

        this.repartirCartas();
    }

    @Override
    public boolean puedeBajarJuego(ICarta[] cartas) throws RemoteException {
        if (!partidaIniciada || !cartaTomada || cartas.length < 3){
            return false;
        }
        //verificar si tiene escalera o pierna y que sean validas
        return esEscaleraValida(cartas) || esPiernaValida(cartas);
    }


    @Override
    public void bajarJuego(ICarta[] cartas) throws RemoteException {
        if (!puedeBajarJuego(cartas)) {
            return;
        }
        Jugador jugadorActual = jugadores.get(jugadorActualIndex);
        ArrayList<Carta> juego = new ArrayList<>();

        //casteo ICarta a Carta y las saco del atril
        for (ICarta carta : cartas) {
            Carta cartaReal = (Carta) carta;
            juego.add(cartaReal);
            jugadorActual.sacarCarta(cartaReal);
        }

        //agregar el juego a los juegos bajados del jugador
        jugadorActual.bajarJuego(juego); //metodo bajarJuego del Jugador
        this.estadoJuego = Eventos.JUEGO_BAJADO;
    }

    private boolean esEscaleraValida(ICarta[] cartas){
        if(cartas.length < 3){
            return false;
        }
        ArrayList<ICarta> cartasNormales = new ArrayList<>();
        int comodines = 0;

        for (ICarta carta : cartas) {
            if (carta.esComodin()) {
                comodines++;
            } else {
                cartasNormales.add(carta);
            }
        }

        // Verificar mismo color en cartas normales
        Color color = cartasNormales.get(0).getColor();
        for (ICarta carta : cartasNormales) {
            if (!carta.getColor().equals(color)) {
                return false;
            }
        }

        // Obtener números y ordenarlos
        ArrayList<Integer> numeros = new ArrayList<>();
        for (ICarta carta : cartasNormales) {
            numeros.add(carta.getNumero());
        }
        numeros.sort(Integer::compareTo);

        // Verificar que no haya números repetidos
        for (int i = 1; i < numeros.size(); i++) {
            if (numeros.get(i).equals(numeros.get(i-1))) {
                return false; // Números repetidos no válidos en escalera
            }
        }

        // Calcular huecos en la secuencia
        int huecos = 0;
        for (int i = 1; i < numeros.size(); i++) {
            huecos += (numeros.get(i) - numeros.get(i-1) - 1);
        }

        // Verificar si los comodines pueden completar la secuencia
        int secuenciaActual = numeros.get(numeros.size()-1) - numeros.get(0) + 1;
        int cartasFaltantes = secuenciaActual - cartasNormales.size();

        return cartasFaltantes <= comodines;
    }

    private boolean esPiernaValida(ICarta[] cartas){
        if(cartas.length < 3){
            return false;
        }
        // Encontrar el número base (primera carta que no sea comodín)
        int numeroBase = -1;
        for (ICarta carta : cartas) {
            if (!carta.esComodin()) {
                numeroBase = carta.getNumero();
                break;
            }
        }

        // Verificar que todas las cartas normales tengan el mismo número
        for (ICarta carta : cartas) {
            if (!carta.esComodin() && carta.getNumero() != numeroBase) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean puedeApoyarCarta(ICarta carta, int jugadorId, int juegoId) throws RemoteException {
        if (!partidaIniciada || jugadorId < 0 || jugadorId >= jugadores.size()){
            return false;
        }

        Jugador jugadorDestino = jugadores.get(jugadorId);
        if (juegoId < 0 || juegoId >= jugadorDestino.cantidadJuegosBajados()){
            return false;
        }

        ArrayList<Carta> juego = jugadorDestino.getJuegosBajados()[juegoId];
        return puedeAgregarCartaAJuego(carta, juego);
    }

    private boolean puedeAgregarCartaAJuego(ICarta nuevaCarta, ArrayList<Carta> juego) {
        if (juego.isEmpty()) return false;

        // Si es comodín, siempre se puede agregar
        if (nuevaCarta.esComodin()) return true;

        // Verificar si es pierna (mismo número)
        if (esPiernaElJuego(juego)) {
            return esValidaParaPierna(nuevaCarta, juego);
        }

        // Verificar si es escalera (mismo color, consecutivos)
        if (esEscaleraElJuego(juego)) {
            return esValidaParaEscalera(nuevaCarta, juego);
        }

        return false;
    }

    private boolean esPiernaElJuego(ArrayList<Carta> juego) {
        int numeroPrimero = -1;
        for (Carta carta : juego) {
            if (!carta.esComodin()) {
                if (numeroPrimero == -1) {
                    numeroPrimero = carta.getNumero();
                } else if (carta.getNumero() != numeroPrimero) {
                    return false; // numeros diferentes = no es pierna
                }
            }
        }
        return true; // todos los números iguales = pierna
    }

    private boolean esValidaParaPierna(ICarta nuevaCarta, ArrayList<Carta> juego) {
        int numeroBase = -1;
        for (Carta carta : juego) {
            if (!carta.esComodin()) {
                numeroBase = carta.getNumero();
                break;
            }
        }
        return numeroBase != -1 && nuevaCarta.getNumero() == numeroBase;
    }

    private boolean esEscaleraElJuego(ArrayList<Carta> juego) {
        Color colorPrimero = null;
        for (Carta carta : juego) {
            if (!carta.esComodin()) {
                if (colorPrimero == null) {
                    colorPrimero = carta.getColor();
                } else if (!carta.getColor().equals(colorPrimero)) {
                    return false; // Colores diferentes = no es escalera
                }
            }
        }
        return true; // Todos del mismo color = escalera
    }

    private boolean esValidaParaEscalera(ICarta nuevaCarta, ArrayList<Carta> juego) {
        // Verifico mismo color
        Color colorBase = null;
        for (Carta carta : juego) {
            if (!carta.esComodin()) {
                colorBase = carta.getColor();
                break;
            }
        }

        if (colorBase == null || !nuevaCarta.getColor().equals(colorBase)) {
            return false;
        }

        // saco los nums de la escalera actual
        ArrayList<Integer> numeros = new ArrayList<>();
        for (Carta carta : juego) {
            if (!carta.esComodin()) {
                numeros.add(carta.getNumero());
            }
        }

        if (numeros.isEmpty()) return true; // Solo comodines

        // ordeno los nums
        numeros.sort(Integer::compareTo);
        int menor = numeros.get(0);
        int mayor = numeros.get(numeros.size() - 1);

        // la nueva carta puede ir antes (menor-1) o despues (mayor+1)
        int nuevoNumero = nuevaCarta.getNumero();
        return nuevoNumero == menor - 1 || nuevoNumero == mayor + 1;
    }

    @Override
    public void apoyarCarta(ICarta carta, int jugadorId, int juegoId) throws RemoteException {
        if (!puedeApoyarCarta(carta, jugadorId, juegoId)){
            return;
        }
        Jugador jugadorActual = jugadores.get(jugadorActualIndex);
        Jugador jugadorDestino = jugadores.get(jugadorId);

        //sacar carta del jugador actual y agregarla en el juego del jugador destino
        jugadorActual.sacarCarta((Carta)carta);
        jugadorDestino.sumarCartaJuegoBajado((Carta)carta, juegoId);
    }

    @Override
    public boolean hayGanador() throws RemoteException {
        if(!partidaIniciada){
            return false;
        }
        for (Jugador jugador : jugadores){
            if (jugador.calcularPuntos() >= 3000){
                return true;
            }
        }
        return false;
    }

    @Override
    public IJugador getGanador() throws RemoteException {
        if (!hayGanador()){
            return null;
        }

        Jugador ganador = jugadores.get(0);
        for (Jugador jugador : jugadores){
            if (jugador.calcularPuntos() > ganador.calcularPuntos()){
                ganador = jugador;
            }
        }
        return ganador;
    }

    @Override
    public int getRonda() throws RemoteException {
        return ronda;
    }

    @Override
    public Eventos getEstadoJuego() throws RemoteException {
        return estadoJuego;
    }

}







