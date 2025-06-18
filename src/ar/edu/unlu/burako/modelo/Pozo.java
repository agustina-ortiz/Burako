package ar.edu.unlu.burako.modelo;

import java.util.ArrayList;

public class Pozo {
    private ArrayList<Carta> cartas;

    public Pozo(){
        this.cartas = new ArrayList<>();
    }

    public void agregarCarta(Carta carta){
        this.cartas.add(carta);
    }

    public Carta verUltimaCarta(){
        if (cartas.isEmpty()){
            return null;
        }
        return cartas.get(cartas.size() - 1);
    }

    public Carta tomarUltimaCarta(){
        if (cartas.isEmpty()){
            return null;
        }
        return cartas.remove(cartas.size() - 1);
    }

    public ArrayList<Carta> tomarTodoElPozo(){
        ArrayList<Carta> todasLasCartas = new ArrayList<>(cartas);
        this.cartas.clear();
        return todasLasCartas;
    }

    public boolean estaVacio(){
        return cartas.isEmpty();
    }

    public int cantidadCartas(){
        return cartas.size();
    }

    public void limpiar(){
        this.cartas.clear();
    }

    @Override
    public String toString(){
        if (estaVacio()){
            return "Pozo vacío";
        }
        return "Pozo con " + cartas.size() + " cartas. Arriba: " + verUltimaCarta();
    }
}






