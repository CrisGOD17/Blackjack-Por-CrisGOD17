package main;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PantallaJuego extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Puntuacion puntuacion;
    private Baraja baraja;
    private ArrayList<Carta> manoUsuario;
    private ArrayList<Carta> manoCasa;

    private JPanel panelCartasUsuario;
    private JPanel panelCartasCasa;
    private JTextArea areaInfo;
    private JLabel labelVictorias, labelDerrotas, labelEmpates, labelSaldo;

    public PantallaJuego() {
        puntuacion = new Puntuacion(1000);
        nuevoJuego();

        this.setPreferredSize(new Dimension(720, 480));
        setLayout(new BorderLayout());

        panelCartasUsuario = new JPanel();
        panelCartasCasa = new JPanel();
        
        areaInfo = new JTextArea(5, 30);
        areaInfo.setEditable(false);

        JPanel panelEstadisticas = new JPanel(new GridLayout(1, 4));
        labelVictorias = new JLabel("Victorias: 0");
        labelDerrotas = new JLabel("Derrotas: 0");
        labelEmpates = new JLabel("Empates: 0");
        labelSaldo = new JLabel("Saldo: " + puntuacion.getSaldo());

        panelEstadisticas.add(labelVictorias);
        panelEstadisticas.add(labelDerrotas);
        panelEstadisticas.add(labelEmpates);
        panelEstadisticas.add(labelSaldo);

        JPanel panelCentro = new JPanel(new BorderLayout());
        panelCentro.add(panelEstadisticas, BorderLayout.NORTH);
        panelCentro.add(areaInfo, BorderLayout.CENTER);

        // Botones para jugar
        JButton botonPedirCarta = new JButton("Pedir Carta");
        botonPedirCarta.addActionListener(e -> {
            manoUsuario.add(baraja.darCartaAleatoria());
            actualizarPantalla();
        });

        JButton botonNuevoJuego = new JButton("Nuevo Juego");
        botonNuevoJuego.addActionListener(e -> {
            nuevoJuego();
            mostrarManoInicial();
            actualizarEstadisticas();
        });

        JButton botonMostrarResultado = new JButton("Mostrar Resultado");
        botonMostrarResultado.addActionListener(e -> {
            mostrarResultado();
            actualizarEstadisticas();
        });

        JPanel panelBotones = new JPanel();
        panelBotones.add(botonPedirCarta);
        panelBotones.add(botonNuevoJuego);
        panelBotones.add(botonMostrarResultado);

        panelCentro.add(panelBotones, BorderLayout.SOUTH);

        add(panelCartasCasa, BorderLayout.NORTH);     // Panel de cartas de la casa
        add(panelCentro, BorderLayout.CENTER);        // Panel de botones y estadísticas
        add(panelCartasUsuario, BorderLayout.SOUTH);  // Panel de cartas del usuario

        mostrarManoInicial();
        actualizarEstadisticas();
    }

    private void nuevoJuego() {
        baraja = new Baraja();
        manoUsuario = new ArrayList<>();
        manoCasa = new ArrayList<>();
        manoUsuario.add(baraja.darCartaAleatoria());
        manoUsuario.add(baraja.darCartaAleatoria());
        manoCasa.add(baraja.darCartaAleatoria());
        manoCasa.add(baraja.darCartaAleatoria());
    }

    private void mostrarManoInicial() {
        panelCartasUsuario.removeAll();
        panelCartasCasa.removeAll();

        // Mostrar las cartas del usuario
        for (Carta carta : manoUsuario) {
            JLabel cartaLabel = new JLabel(carta.getImagen());
            panelCartasUsuario.add(cartaLabel);
        }

        // Mostrar una carta visible y una carta oculta para el crupier
        JLabel primeraCartaCrupier = new JLabel(manoCasa.get(0).getImagen());
        JLabel cartaOculta = new JLabel(new ImageIcon(getClass().getResource("/cards/carta_oculta.png")));

        panelCartasCasa.add(primeraCartaCrupier); // Primera carta visible
        panelCartasCasa.add(cartaOculta); // Segunda carta oculta

        actualizarPantallaTexto();
        revalidate();
        repaint();
    }


    private void actualizarPantalla() {
        panelCartasUsuario.removeAll();

        for (Carta carta : manoUsuario) {
            JLabel cartaLabel = new JLabel(carta.getImagen());
            panelCartasUsuario.add(cartaLabel);
        }

        actualizarPantallaTexto();
        revalidate();
        repaint();
    }

    private void mostrarResultado() {
        // El crupier juega su turno
        while (calcularValor(manoCasa) < 17) {
            manoCasa.add(baraja.darCartaAleatoria());
            actualizarPantalla();  // Actualizamos la pantalla después de cada carta del crupier
        }

        panelCartasCasa.removeAll();

        // Mostrar todas las cartas del crupier
        for (Carta carta : manoCasa) {
            JLabel cartaLabel = new JLabel(carta.getImagen());
            panelCartasCasa.add(cartaLabel);
        }

        // Calcular los valores finales
        int valorUsuario = calcularValor(manoUsuario);
        int valorCasa = calcularValor(manoCasa);

        // Mostrar el texto con los valores finales de las manos
        areaInfo.setText("Tu mano: " + manoUsuario + " (Valor: " + valorUsuario + ")\n" +
                         "Mano de la casa: " + manoCasa + " (Valor: " + valorCasa + ")\n");

        // Determinar el ganador y actualizar las estadísticas
        if (valorUsuario > 21) {
            areaInfo.append("Te has pasado de 21. Has perdido.\n");
            puntuacion.incrementarDerrota();
        } else if (valorCasa > 21) {
            areaInfo.append("El crupier se ha pasado de 21. Has ganado.\n");
            puntuacion.incrementarVictoria();
        } else if (valorUsuario > valorCasa) {
            areaInfo.append("Has ganado.\n");
            puntuacion.incrementarVictoria();
        } else if (valorUsuario < valorCasa) {
            areaInfo.append("El crupier ha ganado.\n");
            puntuacion.incrementarDerrota();
        } else {
            areaInfo.append("Empate.\n");
            puntuacion.incrementarEmpate();
        }

        // Actualizar las estadísticas
        actualizarEstadisticas();

        revalidate();
        repaint();
    }



    private int calcularValor(ArrayList<Carta> mano) {
        int total = 0;
        int numAses = 0;

        for (Carta carta : mano) {
            total += carta.getValorNumerico();
            if (carta.getValor().equals("A")) {
                numAses++;
            }
        }

        while (total > 21 && numAses > 0) {
            total -= 10;
            numAses--;
        }

        return total;
    }

    private void actualizarPantallaTexto() {
        int valorUsuario = calcularValor(manoUsuario);

        // Muestra el valor total del usuario
        String textoUsuario = "Tu mano: " + manoUsuario + " (Valor: " + valorUsuario + ")\n";
        
        // Calcula y muestra solo la primera carta del crupier mientras una está oculta
        if (manoCasa.size() > 1) {
            int valorPrimeraCartaCrupier = manoCasa.get(0).getValorNumerico();
            String textoCasa = "Mano de la casa: " + manoCasa.get(0) + " y otra carta oculta (Valor: " + valorPrimeraCartaCrupier + "+)";
            areaInfo.setText(textoUsuario + textoCasa);
        } else {
            areaInfo.setText(textoUsuario + "Mano de la casa: No hay cartas visibles aún.");
        }
    }


    private void actualizarEstadisticas() {
        labelVictorias.setText("Victorias: " + puntuacion.getVictorias());
        labelDerrotas.setText("Derrotas: " + puntuacion.getDerrotas());
        labelEmpates.setText("Empates: " + puntuacion.getEmpates());
        labelSaldo.setText("Saldo: " + puntuacion.getSaldo());
    }
}
