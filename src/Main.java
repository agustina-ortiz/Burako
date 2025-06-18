import ar.edu.unlu.burako.controlador.ControladorBurako;
import ar.edu.unlu.burako.vista.VistaConsola;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        System.out.println("Iniciando Burako...");

        try{
            ControladorBurako controlador = new ControladorBurako(); //crear el controlador que creará el modelo
            VistaConsola vista = new VistaConsola(); //crear la vista de la consola

            vista.setControlador(controlador);
            controlador.setVista(vista);//conectar vista con controlador
            vista.iniciar();
        } catch (Exception e){
            System.err.println("Error al iniciar el juego: " + e.getMessage());
            e.printStackTrace();
        } finally {
            System.out.println("Juego terminado.");
        }
    }
}