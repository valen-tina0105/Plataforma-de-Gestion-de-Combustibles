package co.edu.unipiloto.pgc.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Delivery {
    private int id;
    private String placa;
    private Fuel combustible;
    private double cantidad;
    private Date fecha;
    private String fechaFormateada;

    private String estado; // 🔥 NUEVO
    private String fechaConfirmacion; // 🔥 NUEVO

    private User estacion;
    private User distribuidor;

    public Delivery() {
        fecha = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        fechaFormateada = formatter.format(fecha);
        estado = "PENDIENTE"; // 🔥 default
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public Fuel getCombustible() { return combustible; }
    public void setCombustible(Fuel combustible) { this.combustible = combustible; }

    public double getCantidad() { return cantidad; }
    public void setCantidad(double cantidad) { this.cantidad = cantidad; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public String getFechaFormateada() { return fechaFormateada; }
    public void setFechaFormateada(String fechaFormateada) { this.fechaFormateada = fechaFormateada; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getFechaConfirmacion() { return fechaConfirmacion; }
    public void setFechaConfirmacion(String fechaConfirmacion) { this.fechaConfirmacion = fechaConfirmacion; }

    public User getEstacion() { return estacion; }
    public void setEstacion(User estacion) { this.estacion = estacion; }

    public User getDistribuidor() { return distribuidor; }
    public void setDistribuidor(User distribuidor) { this.distribuidor = distribuidor; }
}