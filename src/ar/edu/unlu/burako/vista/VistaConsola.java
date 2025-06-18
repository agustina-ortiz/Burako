package ar.edu.unlu.burako.vista;

import ar.edu.unlu.burako.controlador.ControladorBurako;
import ar.edu.unlu.burako.modelo.*;

import java.util.Scanner;
import java.util.ArrayList;

public class VistaConsola implements IVista {
    private ControladorBurako controlador;
    private Scanner scanner;
    private boolean juegoActivo;

    public VistaConsola(){
        this.scanner = new Scanner(System.in);
        this.juegoActivo = true;
    }

    @Override
    public void setControlador(ControladorBurako controlador){
        this.controlador = controlador;
    }

    @Override
    public void iniciar() {
        mostrarBienvenida();

        while (juegoActivo){
            mostrarMenu();
            procesarOpcion();
        }
    }

    private void mostrarBienvenida(){
        System.out.println("=================================");
        System.out.println("      BIENVENIDO AL BURAKO      ");
        System.out.println("=================================");
        System.out.println();
    }

    private void mostrarMenu(){
        Eventos estado = controlador.getEstadoJuego();

        System.out.println("\n--- ESTADO: " + estado + " ---");

        switch (estado){
            case ESPERANDO_JUGADORES:
            case LISTO_PARA_INICIAR:
                mostrarMenuConfiguracion();
                break;
            case TURNO_JUGADOR:
                mostrarMenuJuego();
                break;
            case PARTIDA_TERMINADA:
                mostrarResultados();
                break;
        }
    }

    private void mostrarMenuConfiguracion(){
        System.out.println("1. Agregar jugador");
        System.out.println("2. Iniciar partida");
        System.out.println("3. Ver jugadores");
        System.out.println("0. Salir");
        System.out.print("Opción: ");
    }

    private void mostrarMenuJuego(){
        IJugador jugadorActual = controlador.getJugadorActual();

        limpiarPantalla();

        System.out.println("=================================");
        System.out.println("         BURAKO - RONDA " + controlador.getRonda());
        System.out.println("=================================");
        System.out.println("TURNO DE: " + jugadorActual.getNombre());

        mostrarCartasJugador(jugadorActual);
        mostrarEstadoJuego();

        System.out.println("\nOpciones:");
        if (controlador.puedeTomarDelMazo()) {
            System.out.println("1. Tomar carta del mazo");
        }
        if (controlador.puedeTomarDelPozo()) {
            System.out.println("2. Tomar carta del pozo");
            System.out.println("3. Tomar todo el pozo");
        }
        System.out.println("4. Bajar juego");
        System.out.println("5. Apoyar carta");
        System.out.println("6. Descartar carta");
        System.out.println("7. Ver juegos bajados");
        System.out.println("0. Salir");
        System.out.print("\n Elegir opción: ");
    }

    private void limpiarPantalla(){
        for (int i = 0; i < 20; i++) { //imprimo lineas en blanco para simular la limpieza
            System.out.println();
        }
    }

    private void mostrarEstadoJuego(){
        System.out.println("\n--- ESTADO DEL JUEGO ---");
        System.out.println("Jugadores:");
        IJugador[] jugadores = controlador.getJugadores();
        for (IJugador jugador : jugadores) {
            String turnoActual = jugador.equals(controlador.getJugadorActual()) ? " ← TU TURNO" : "";
            System.out.println("  " + jugador.getNombre() + " (" + jugador.calcularPuntos() + " pts)" + turnoActual);
        }
    }

    private void mostrarCartasJugador(IJugador jugador){
        ICarta[] cartas = jugador.getCartasEnAtril();
        System.out.println("\n=== TUS CARTAS (" + cartas.length + ") ===");

        if (cartas.length == 0) {
            System.out.println("(No tienes cartas)");
            return;
        }

        for (int i = 0; i < cartas.length; i++) {
            System.out.print((i + 1) + "." + cartaToString(cartas[i]) + "  ");
        }
        System.out.println();
        System.out.println("=====================================");
    }

    private void mostrarTodosLosJugadores(){
        IJugador[] jugadores = controlador.getJugadores();
        System.out.println("\n--- TODOS LOS JUGADORES ---");
        for (int i = 0; i < jugadores.length; i++) {
            IJugador jugador = jugadores[i];
            System.out.println((i + 1) + ". " + jugador.getNombre() +
                    " - Puntos: " + jugador.calcularPuntos());
                    // TODO: Mostrar juegos bajados de cada jugador
        }
    }

    private void procesarOpcion(){
        try{
            int opcion = scanner.nextInt();
            scanner.nextLine(); //limpiar el buffer

            Eventos estado = controlador.getEstadoJuego();

            switch (estado) {
                case ESPERANDO_JUGADORES:
                case LISTO_PARA_INICIAR:
                    procesarMenuConfiguracion(opcion);
                    break;
                case TURNO_JUGADOR:
                    procesarMenuJuego(opcion);
                    break;
                case PARTIDA_TERMINADA:
                    if (opcion == 0){
                        juegoActivo = false;
                    }
                    break;
            }
        } catch (Exception e){
            System.out.println("Opción inválida. Intenta de nuevo");
            scanner.nextLine(); //limpiar el buffer
        }
    }

    private void procesarMenuConfiguracion(int opcion){
        switch (opcion){
            case 1:
                System.out.print("Nombre del jugador: ");
                String nombre = scanner.nextLine();
                controlador.agregarJugador(nombre);
                break;
            case 2:
                controlador.iniciarPartida();
                break;
            case 3:
                mostrarTodosLosJugadores();
                break;
            case 0:
                juegoActivo = false;
                break;
            default:
                System.out.println("Opción inválida.");
        }
    }

    private void procesarMenuJuego(int opcion){
        switch (opcion){
            case 1:
                controlador.tomarCartaDelMazo();
                break;
            case 2:
                controlador.tomarCartaDelPozo();
                break;
            case 3:
                controlador.tomarTodoElPozo();
                break;
            case 4:
                procesarBajarJuego();
                break;
            case 5:
                procesarApoyarCarta();
                break;
            case 6:
                procesarDescartarCarta();
                break;
            case 7:
                mostrarTodosLosJugadores();
                break;
            case 0:
                juegoActivo = false;
                break;
            default:
                System.out.println("Opción inválida.");
        }
    }

    private void procesarDescartarCarta(){
        IJugador jugador = controlador.getJugadorActual();
        ICarta[] cartas = jugador.getCartasEnAtril();

        if (cartas.length == 0){
            System.out.println("No tienes cartas para descartar.");
            return;
        }

        System.out.print("¿Qué carta descartar? (1-" + cartas.length + "): ");
        try {
            int indice = scanner.nextInt() - 1;
            if (indice >= 0 && indice < cartas.length) {
                controlador.descartarCarta(cartas[indice]);
            } else {
                System.out.println("Índice inválido.");
            }
        } catch (Exception e) {
            System.out.println("Entrada inválida.");
            scanner.nextLine();
        }
    }

    private void procesarBajarJuego(){
        IJugador jugador = controlador.getJugadorActual();
        ICarta[] cartas = jugador.getCartasEnAtril();

        if (cartas.length < 3) {
            System.out.println("Necesitas al menos 3 cartas para bajar un juego.");
            return;
        }

        System.out.println("\n=== BAJAR JUEGO ===");
        System.out.println("Selecciona las cartas para formar:");
        System.out.println("- ESCALERA: cartas consecutivas del mismo color (ej: 5 rojo, 6 rojo, 7 rojo)");
        System.out.println("- PIERNA: cartas del mismo número (ej: 5 negro, 5 amarillo, 5 amarillo)");

        mostrarCartasJugador(jugador);

        System.out.println("\nEscribe los números de las cartas separados por espacios");
        System.out.println("Ejemplo: 1 3 5  (para cartas 1, 3 y 5)");
        System.out.print("Cartas a bajar (o 'c' para cancelar): ");

        String input = scanner.nextLine().trim();

        if (input.equalsIgnoreCase("c")) {
            System.out.println("Operación cancelada.");
            return;
        }

        try {

            String[] numeros = input.split("\\s+");
            if (numeros.length < 3) {
                System.out.println("Necesitas seleccionar al menos 3 cartas.");
                return;
            }

            ICarta[] cartasSeleccionadas = new ICarta[numeros.length];
            for (int i = 0; i < numeros.length; i++) {
                int indice = Integer.parseInt(numeros[i]) - 1;  // -1 porque mostramos desde 1

                if (indice < 0 || indice >= cartas.length) {
                    System.out.println("Número de carta inválido: " + (indice + 1));
                    return;
                }

                cartasSeleccionadas[i] = cartas[indice];
            }

            if (controlador.puedeBajarJuego(cartasSeleccionadas)) {
                controlador.bajarJuego(cartasSeleccionadas);
                System.out.println("✅ Juego bajado exitosamente!");
                Thread.sleep(500);
            } else {
                System.out.println("❌ Las cartas seleccionadas no forman un juego válido.");
                System.out.println("Verifica que sea una escalera (consecutivas mismo color) o pierna (mismo número).");
            }

        } catch (NumberFormatException e) {
            System.out.println("❌ Formato inválido. Usa números separados por espacios.");
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    private void procesarApoyarCarta() {
        IJugador jugadorActual = controlador.getJugadorActual();
        ICarta[] cartasJugador = jugadorActual.getCartasEnAtril();

        if (cartasJugador.length == 0) {
            System.out.println("No tienes cartas para apoyar.");
            return;
        }

        IJugador[] jugadores = controlador.getJugadores();
        ArrayList<JuegoInfo> juegosBajados = new ArrayList<>();

        System.out.println("\n=== JUEGOS BAJADOS ===");
        int contadorJuegos = 0;

        for (int j = 0; j < jugadores.length; j++) {
            IJugador jugador = jugadores[j];
            if (jugador instanceof Jugador) {
                Jugador jug = (Jugador) jugador;
                for (int i = 0; i < jug.cantidadJuegosBajados(); i++) {
                    contadorJuegos++;
                    System.out.print(contadorJuegos + ". " + jugador.getNombre() + " - Juego " + (i+1) + ": ");
                    mostrarCartasDeJuego(jug, i);
                    juegosBajados.add(new JuegoInfo(j, i, jugador.getNombre()));
                }
            }
        }

        if (juegosBajados.isEmpty()) {
            System.out.println("No hay juegos bajados aún.");
            return;
        }

        System.out.println("\n=== TUS CARTAS ===");
        mostrarCartasJugador(jugadorActual);

        System.out.print("\n¿Qué carta quieres apoyar? (1-" + cartasJugador.length + "): ");
        try {
            int indiceCarta = scanner.nextInt() - 1;
            if (indiceCarta < 0 || indiceCarta >= cartasJugador.length) {
                System.out.println("Número de carta inválido.");
                return;
            }

            System.out.print("¿En qué juego la apoyas? (1-" + juegosBajados.size() + "): ");
            int indiceJuego = scanner.nextInt() - 1;
            if (indiceJuego < 0 || indiceJuego >= juegosBajados.size()) {
                System.out.println("Número de juego inválido.");
                return;
            }

            ICarta cartaSeleccionada = cartasJugador[indiceCarta];
            JuegoInfo juegoSeleccionado = juegosBajados.get(indiceJuego);

            controlador.apoyarCarta(cartaSeleccionada, juegoSeleccionado.jugadorId, juegoSeleccionado.juegoId);

        } catch (Exception e) {
            System.out.println("Entrada inválida.");
            scanner.nextLine();
        }
    }

    private static class JuegoInfo {
        int jugadorId;
        int juegoId;
        String nombreJugador;

        public JuegoInfo(int jugadorId, int juegoId, String nombreJugador) {
            this.jugadorId = jugadorId;
            this.juegoId = juegoId;
            this.nombreJugador = nombreJugador;
        }
    }

    private void mostrarCartasDeJuego(Jugador jugador, int juegoId) {
        ArrayList<Carta>[] juegos = jugador.getJuegosBajados();
        if (juegoId < juegos.length) {
            ArrayList<Carta> juego = juegos[juegoId];
            System.out.print("[");
            for (int i = 0; i < juego.size(); i++) {
                Carta carta = juego.get(i);
                if (carta.esComodin()) {
                    System.out.print("COMODIN");
                } else {
                    System.out.print(carta.getNumero() + " " + carta.getColor());
                }
                if (i < juego.size() - 1) System.out.print(", ");
            }
            System.out.println("]");
        }
    }

    private void mostrarResultados(){
        IJugador ganador = controlador.getGanador();
        if(ganador != null){
            System.out.println("\n🎉 ¡FELICITACIONES! 🎉");
            System.out.println("Ganador: " + ganador.getNombre());
            System.out.println("Puntos: " + ganador.calcularPuntos());
        }

        System.out.println("\n--- PUNTUACIÓN FINAL ---");
        mostrarTodosLosJugadores();

        System.out.println("\nPresiona 0 para salir.");
    }

    private String cartaToString(ICarta carta){
        if (carta.esComodin()){
            return "COMODIN";
        }
        return carta.getNumero() + " " + carta.getColor();
    }

    @Override
    public void actualizar(Eventos evento){
        switch (evento){
            case JUGADOR_UNIDO:
                System.out.println("\n✅ Jugador agregado exitosamente.");
                break;
            case CARTA_TOMADA:
                break;
            case CARTA_DESCARTADA:
                break;
            case JUEGO_BAJADO:
                break;
            case CARTA_APOYADA:
                System.out.println("\n✅ Carta apoyada exitosamente.");
                break;
            case GANADOR_ENCONTRADO:
                IJugador ganador = controlador.getGanador();
                System.out.println("\n🎉 ¡" + ganador.getNombre() + " ha ganado con " + ganador.calcularPuntos() + " puntos!");
                System.out.println("¡FELICITACIONES! 🏆");
                break;
            default:
                break;
        }
    }

    @Override
    public void mostrarMensaje(String mensaje){
        System.out.println("\n📢 " + mensaje);
    }

    @Override
    public void mostrarError(String error) {
        System.out.println("\n❌ Error: " + error);
    }

    @Override
    public void cerrar(){
        System.out.println("\n ¡Gracias por jugar Burako! Regrese pronto.");
        scanner.close();
        juegoActivo = false;
    }
}
