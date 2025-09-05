package main;

import java.awt.Image;

import javax.swing.ImageIcon;

public class Carta {
    private String palo;
    private String valor;
    private int valorNumerico;
    private String rutaImagen; // Ruta de la imagen de la carta

    public Carta(String palo, String valor, int valorNumerico, String rutaImagen) {
        this.palo = palo;
        this.valor = valor;
        this.valorNumerico = valorNumerico;
        this.rutaImagen = rutaImagen;
    }

    public int getValorNumerico() {
        return valorNumerico;
    }

    public String getValor() {
        return valor;
    }

    public String getPalo() {
        return palo;
    }

    public ImageIcon getImagen() {
        return new ImageIcon(getClass().getResource(rutaImagen)); 
        // Cargar imagen desde el recurso
    }
    
    
   


    @Override
    public String toString() {
        return valor + " de " + palo;
    }
}
