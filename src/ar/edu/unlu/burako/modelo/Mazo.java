package ar.edu.unlu.burako.modelo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Mazo {
    private ArrayList<Carta> cartas;

    public Mazo(){
        this.cartas = new ArrayList<>();
        this.crearMazo();
        this.mezclar();
    }

    private void crearMazo(){
        for (int mazo = 0; mazo < 2; mazo++){
            for (Color color : Color.values()){
                for (int numero = 1; numero <= 13; numero++) {
                    this.cartas.add(new Carta(numero, color));
                }
            }
        }

        for (int i = 0; i < 2; i++){
            this.cartas.add(new Carta()); //constructor de comodin
        }
    }

    public void mezclar(){
        Collections.shuffle(this.cartas);
    }

    public Carta tomarCarta(){
        if (cartas.isEmpty()){
            return null;
        }
        return cartas.remove(cartas.size() - 1);
    }

    public boolean estaVacio(){
        return cartas.isEmpty();
    }

    public int cantidadCartas(){
        return cartas.size();
    }

    @Override
    public String toString(){
        return "Mazo con " + cartas.size() + " cartas";
    }
}
