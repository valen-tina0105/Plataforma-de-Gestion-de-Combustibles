package co.edu.unipiloto.pgc.model;

public class Inventory {
    private int id;
    private User estacion;
    private Fuel combustible;
    private double cantidadCombustible;
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

    public Fuel getCombustible() {
        return combustible;
    }

    public void setCombustible(Fuel combustible) {
        this.combustible = combustible;
    }

    public User getEstacion() {
        return estacion;
    }

    public void setEstacion(User estacion) {
        this.estacion = estacion;
    }

    public double getCantidadCombustible() {
        return cantidadCombustible;
    }

    public void setCantidadCombustible(double cantidadCombustible) {
        this.cantidadCombustible = cantidadCombustible;
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
