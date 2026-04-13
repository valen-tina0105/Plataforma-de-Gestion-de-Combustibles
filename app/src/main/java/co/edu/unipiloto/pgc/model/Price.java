package co.edu.unipiloto.pgc.model;

public class Price {

    private int id;
    private double precio;
    private User estacion;
    private Fuel combustible;

    public Price() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getEstacion() {
        return estacion;
    }

    public void setEstacion(User estacion) {
        this.estacion = estacion;
    }

    public Fuel getCombustible() {
        return combustible;
    }

    public void setCombustible(Fuel combustible) {
        this.combustible = combustible;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}
