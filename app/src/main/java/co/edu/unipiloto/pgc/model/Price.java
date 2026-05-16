package co.edu.unipiloto.pgc.model;

public class Price {

    private int id;
    private double precio;
    private int estacionId;
    private String estacionUsername;
    private Fuel combustible;

    public Price() {
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

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
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
}
