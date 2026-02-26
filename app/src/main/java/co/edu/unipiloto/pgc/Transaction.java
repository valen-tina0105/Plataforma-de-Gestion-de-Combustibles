package co.edu.unipiloto.pgc;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Transaction implements Serializable {
    private String tipo;
    private int total;
    private int volumen;
    private Date fecha;
    private SimpleDateFormat formatter;

    private String fechaFormateada;

    public Transaction(String tipo, int total, int volumen){
        this.tipo = tipo;
        this.total = total;
        this.volumen = volumen;
        fecha = new Date();
        formatter = new SimpleDateFormat("dd-MM-yyyy");
        fechaFormateada = formatter.format(fecha);
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getVolumen() {
        return volumen;
    }

    public void setVolumen(int volumen) {
        this.volumen = volumen;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public SimpleDateFormat getFormatter() {
        return formatter;
    }

    public void setFormatter(SimpleDateFormat formatter) {
        this.formatter = formatter;
    }

    public String getFechaFormateada() {
        return fechaFormateada;
    }

    public void setFechaFormateada(String fechaFormateada) {
        this.fechaFormateada = fechaFormateada;
    }
}
