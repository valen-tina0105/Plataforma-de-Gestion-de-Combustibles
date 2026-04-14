package co.edu.unipiloto.pgc.model;

public class Station {
    private String nombre;
    private String direccion;
    private double latitud;
    private double longitud;
    private double distancia;

    private double precioCorriente;
    private double precioExtra;
    private double precioDiesel;
    private double precioGNV;

    public Station() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public double getLatitud() {
        return latitud;
    }
    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public double getPrecioCorriente() {
        return precioCorriente;
    }

    public void setPrecioCorriente(double precioCorriente) {
        this.precioCorriente = precioCorriente;
    }

    public double getPrecioExtra() {
        return precioExtra;
    }

    public void setPrecioExtra(double precioExtra) {
        this.precioExtra = precioExtra;
    }

    public double getPrecioDiesel() {
        return precioDiesel;
    }

    public void setPrecioDiesel(double precioDiesel) {
        this.precioDiesel = precioDiesel;
    }

    public double getPrecioGNV() {
        return precioGNV;
    }

    public void setPrecioGNV(double precioGNV) {
        this.precioGNV = precioGNV;
    }
}
