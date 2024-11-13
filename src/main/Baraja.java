package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Baraja {
    private ArrayList<Carta> cartas = new ArrayList<>();
    private Random random = new Random();

    public Baraja() {
        String[] palos = {"Corazones", "Tréboles", "Picas", "Diamantes"};
        String[] valores = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
        int[] valoresNumericos = {2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10, 11};
        
        for (String palo : palos) {
            for (int i = 0; i < valores.length; i++) {
                String rutaImagen = "/cards/" + valores[i] + "_" + palo + ".png"; // Ruta de imagen
                cartas.add(new Carta(palo, valores[i], valoresNumericos[i], rutaImagen));
            }
        }
    }
    

    public Carta darCartaAleatoria() {
        if (cartas.isEmpty()) {
            throw new IllegalStateException("No quedan cartas en la baraja");
        }
        int indice = random.nextInt(cartas.size());
        Carta cartaSeleccionada = cartas.get(indice);
        cartas.remove(indice); 
        return cartaSeleccionada;
    }

    // Método para mezclar la baraja (opcional)
    public void barajar() {
        Collections.shuffle(cartas);
    }
}



