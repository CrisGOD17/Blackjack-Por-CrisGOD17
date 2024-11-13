package main;

class Puntuacion {

    private int victorias;
    private int derrotas;
    private int empates;
    private int saldo;

    public Puntuacion(int saldoInicial) {
        this.victorias = 0;
        this.derrotas = 0;
        this.empates = 0;
        this.saldo = saldoInicial;
    }

    public void incrementarVictoria() {
        victorias++;
        saldo += 100; 
    }

    public void incrementarDerrota() {
        derrotas++;
        saldo -= 100;
    }

    public void incrementarEmpate() {
        empates++;
    }

    public int getVictorias() {
        return victorias;
    }

    public int getDerrotas() {
        return derrotas;
    }

    public int getEmpates() {
        return empates;
    }

    public int getSaldo() {
        return saldo;
    }

    public void reiniciarPuntuacion() {
        victorias = 0;
        derrotas = 0;
        empates = 0;
        saldo = 0; 
    }
}