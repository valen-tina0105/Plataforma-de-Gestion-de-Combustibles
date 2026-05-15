package co.edu.unipiloto.pgc.model;

public class Inventory {
    private int id;
    private int estacionId;
    private String estacionUsername;
    private Fuel combustible;
    private double cantidadActual;
    private double capacidadMaxima;
    private double nivelMinimo;

    public Inventory() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEstacionId() {
        return estacionId;
    }

    public void setEstacionId(int estacionId) {
        this.estacionId = estacionId;
    }

    public String getEstacionUsername() {
        return estacionUsername;
    }

    public void setEstacionUsername(String estacionUsername) {
        this.estacionUsername = estacionUsername;
    }

    public Fuel getCombustible() {
        return combustible;
    }

    public void setCombustible(Fuel combustible) {
        this.combustible = combustible;
    }

    public double getCantidadActual() {
        return cantidadActual;
    }

    public void setCantidadActual(double cantidadActual) {
        this.cantidadActual = cantidadActual;
    }

    public double getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public void setCapacidadMaxima(double capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
    }

    public double getNivelMinimo() {
        return nivelMinimo;
    }

    public void setNivelMinimo(double nivelMinimo) {
        this.nivelMinimo = nivelMinimo;
    }
}
