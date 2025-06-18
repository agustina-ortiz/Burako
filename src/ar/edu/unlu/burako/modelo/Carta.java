package ar.edu.unlu.burako.modelo;

import java.io.Serializable;

public class Carta implements ICarta, Serializable {
    private static final long serialVersionUID = 1L;

    private int numero;
    private Color color;
    private boolean esComodin;

    // Constructor de mis cartas normales
    public Carta(int numero, Color color){
        this.numero = numero;
        this.color = color;
        this.esComodin = false;
    }

    //constructor comodines
    public Carta(){
        this.numero = 0;
        this.color = null;
        this.esComodin = true;
    }

    @Override
    public int getNumero(){
        return numero;
    }

    @Override
    public Color getColor(){
        return color;
    }

    @Override
    public boolean esComodin(){
        return esComodin;
    }

    @Override
    public int getPuntos(){
        if(esComodin){
            return 50;
        }

        switch (numero){
            case 1: return 15;
            case 2: return 20;
            case 3: case 4: case 5: case 6: case 7: return 5;
            case 8: case 9: case 10: case 11: case 12: case 13: return 10;
            default: return 0;
        }
    }

    // muestro la carta
    @Override
    public String toString(){
        if (esComodin) {
            return  "COMODIN";
        }
        return numero + " " + color;
    }
}
