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
    
    private JButton botonPedirCarta;
    private JButton botonNuevoJuego;
    private JButton botonMostrarResultado;


    public PantallaJuego() {
        puntuacion = new Puntuacion(1000);

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
        botonPedirCarta = new JButton("Pedir Carta");
        botonPedirCarta.addActionListener(e -> {
            manoUsuario.add(baraja.darCartaAleatoria());
            actualizarPantalla();

            // Verificar si el usuario se pasó de 21
            if (calcularValor(manoUsuario) > 21) {
                areaInfo.append("\n Te has pasado de 21. Has perdido.\n");
                puntuacion.incrementarDerrota();
                botonPedirCarta.setEnabled(false);  // Deshabilitar "Pedir Carta"
                botonMostrarResultado.setEnabled(false);  // Deshabilitar "Mostrar Resultado"
                actualizarEstadisticas();
                revalidate();
                repaint();
            }
        });


        botonNuevoJuego = new JButton("Nuevo Juego");
        botonNuevoJuego.addActionListener(e -> {
            nuevoJuego();
            mostrarManoInicial();
            actualizarEstadisticas();
            botonPedirCarta.setEnabled(true);
            botonMostrarResultado.setEnabled(true);
        });

        botonMostrarResultado = new JButton("Mostrar Resultado");
        botonMostrarResultado.addActionListener(e -> {
            mostrarResultado();
            botonPedirCarta.setEnabled(false);  // Deshabilitar "Pedir Carta"
            botonMostrarResultado.setEnabled(false); // Deshabilitar "Mostrar Resultado"
        });

        JPanel panelBotones = new JPanel();
        panelBotones.add(botonPedirCarta);
        panelBotones.add(botonNuevoJuego);
        panelBotones.add(botonMostrarResultado);

        panelCentro.add(panelBotones, BorderLayout.SOUTH);

        add(panelCartasCasa, BorderLayout.NORTH);     // Panel de cartas de la casa
        add(panelCentro, BorderLayout.CENTER);        // Panel de botones y estadísticas
        add(panelCartasUsuario, BorderLayout.SOUTH);  // Panel de cartas del usuario

        // Inicializar el juego después de configurar todos los componentes
        nuevoJuego();
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

        botonPedirCarta.setEnabled(true); // Habilitar "Pedir Carta"
        botonMostrarResultado.setEnabled(true); // Habilitar "Mostrar Resultado"
    }


    private void mostrarManoInicial() {
        panelCartasUsuario.removeAll();
        panelCartasCasa.removeAll();

        for (Carta carta : manoUsuario) {
            ImageIcon cartaEscalada = escalarImagen(carta.getImagen(),  80, 100); // Escalar a 100x150 píxeles
            JLabel cartaLabel = new JLabel(cartaEscalada);
            panelCartasUsuario.add(cartaLabel);
        }

        JLabel cartaVisible = new JLabel(escalarImagen(manoCasa.get(0).getImagen(), 80, 100));
        JLabel cartaOculta = new JLabel(escalarImagen(
            new ImageIcon(getClass().getResource("/cards/carta_oculta.png")), 80, 100));

        panelCartasCasa.add(cartaVisible); // Carta visible del crupier
        panelCartasCasa.add(cartaOculta); // Carta oculta del crupier

        actualizarPantallaTexto();
        revalidate();
        repaint();
    }


    private void actualizarPantalla() {
        panelCartasUsuario.removeAll();

        for (Carta carta : manoUsuario) {
        	
            JLabel cartaLabel = new JLabel(escalarImagen(carta.getImagen(),  80, 100));
            panelCartasUsuario.add(cartaLabel);
        }

        actualizarPantallaTexto();
        revalidate();
        repaint();
    }

    private void mostrarResultado() {
        int valorUsuario = calcularValor(manoUsuario);

        if (valorUsuario > 21) {
            areaInfo.setText("Tu mano: " + manoUsuario + " (Valor: " + valorUsuario + ")\n");
            areaInfo.append("Te has pasado de 21. Has perdido.\n");
            puntuacion.incrementarDerrota();
            botonPedirCarta.setEnabled(false);  // Deshabilitar "Pedir Carta"
            botonMostrarResultado.setEnabled(false); 
            actualizarEstadisticas();
            revalidate();
            repaint();
            return; // Finalizar el método, ya no hay necesidad de jugar el turno del crupier
        }
        
        
        while (calcularValor(manoCasa) < 17) {
            manoCasa.add(baraja.darCartaAleatoria());
            actualizarPantalla();
        }

        panelCartasCasa.removeAll();
        
        for (Carta carta : manoCasa) {
            JLabel cartaLabel = new JLabel(escalarImagen(carta.getImagen(),  80, 100));  
            panelCartasCasa.add(cartaLabel);
        }

        int valorCasa = calcularValor(manoCasa);

        areaInfo.setText("Tu mano: " + manoUsuario + " (Valor: " + valorUsuario + ")\n" +
                         "Mano de la casa: " + manoCasa + " (Valor: " + valorCasa + ")\n");

        if (valorCasa > 21) {
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

        botonPedirCarta.setEnabled(false);  // Deshabilitar "Pedir Carta"
        botonMostrarResultado.setEnabled(false); // Deshabilitar "Mostrar Resultado"
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
    
    private ImageIcon escalarImagen(ImageIcon imagenOriginal, int ancho, int alto) {
        Image imagen = imagenOriginal.getImage(); // Obtener la imagen original
        Image imagenEscalada = imagen.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH); // Escalar la imagen
        return new ImageIcon(imagenEscalada); // Retornar como ImageIcon
    }
}
