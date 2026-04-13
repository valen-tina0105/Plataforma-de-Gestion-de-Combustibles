package co.edu.unipiloto.pgc.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Register{
    private int id;
    private User estacion;
    private Fuel Combustible;
    private double cantidad;
    private Date fecha;
    private String fechaFormateada;

    public Register() {
        fecha = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        fechaFormateada = formatter.format(fecha);
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
        return Combustible;
    }

    public void setCombustible(Fuel combustible) {
        Combustible = combustible;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getFechaFormateada() {
        return fechaFormateada;
    }

    public void setFechaFormateada(String fechaFormateada) {
        this.fechaFormateada = fechaFormateada;
    }
}
