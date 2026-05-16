package co.edu.unipiloto.pgc.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Transaction implements Serializable {
    private int id;
    private int estacionId;
    private String estacionUsername;
    private String tipoVehiculo;
    private double total;
    private double cantidad;
    private int userId;
    private String userUsername;
    private String fechaFormateada;
    private String estado;
    private Fuel combustible;

    public Transaction() {
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

    public String getTipoVehiculo() {
        return tipoVehiculo;
    }

    public void setTipoVehiculo(String tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public String getFechaFormateada() {
        return fechaFormateada;
    }

    public void setFechaFormateada(String fechaFormateada) {
        this.fechaFormateada = fechaFormateada;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserUsername() {
        return userUsername;
    }

    public void setUserUsername(String userUsername) {
        this.userUsername = userUsername;
    }

    public Fuel getCombustible() {
        return combustible;
    }

    public void setCombustible(Fuel combustible) {
        this.combustible = combustible;
    }
}
