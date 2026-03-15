package co.edu.unipiloto.pgc.model;

public class Movement {

    private int id;
    private String tipo;
    private int cantidad;
    private Integer total;
    private String fecha;
    private int estacionId;
    private String tipoMovimiento;

    public Movement() {
    }

    public Movement(int id, String tipo, int cantidad, Integer total, String fecha, int estacionId, String tipoMovimiento) {
        this.id = id;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.total = total;
        this.fecha = fecha;
        this.estacionId = estacionId;
        this.tipoMovimiento = tipoMovimiento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getEstacionId() {
        return estacionId;
    }

    public void setEstacionId(int estacionId) {
        this.estacionId = estacionId;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }
}
