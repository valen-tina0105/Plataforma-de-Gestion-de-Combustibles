package co.edu.unipiloto.pgc;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Register implements Serializable {
    private String tipo;
    private int cantidad;
    private Date fecha;

    private String fechaFormateada;

    public Register(String tipo, int cantidad) {
        this.tipo = tipo;
        this.cantidad = cantidad;
        fecha = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        fechaFormateada = formatter.format(fecha);
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
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
