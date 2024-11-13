package main;

import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        JFrame window = new PantallaJuego(); // Crear instancia de PantallaJuego
        window.setTitle("Juego BlackJack");  // Título de la ventana
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Acción al cerrar
        window.setResizable(false); // Evitar cambio de tamaño
        window.pack(); // Ajuste al tamaño del contenido
        window.setLocationRelativeTo(null); // Centrar ventana en pantalla
        window.setVisible(true); // Mostrar ventana
    }
}