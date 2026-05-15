package co.edu.unipiloto.pgc.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Delivery {
    private int id;
    private String placa;
    private Fuel combustible;
    private double cantidad;
    private String fechaFormateada;
    private String estado;
    private int estacionId;
    private String estacionUsername;

    private int distribuidorId;
    private String distribuidorUsername;

    public Delivery() {
        estado = "PENDIENTE";
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public Fuel getCombustible() { return combustible; }
    public void setCombustible(Fuel combustible) { this.combustible = combustible; }

    public double getCantidad() { return cantidad; }
    public void setCantidad(double cantidad) { this.cantidad = cantidad; }

    public String getFechaFormateada() { return fechaFormateada; }
    public void setFechaFormateada(String fechaFormateada) { this.fechaFormateada = fechaFormateada; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

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

    public int getDistribuidorId() {
        return distribuidorId;
    }

    public void setDistribuidorId(int distribuidorId) {
        this.distribuidorId = distribuidorId;
    }

    public String getDistribuidorUsername() {
        return distribuidorUsername;
    }

    public void setDistribuidorUsername(String distribuidorUsername) {
        this.distribuidorUsername = distribuidorUsername;
    }
}