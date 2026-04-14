package co.edu.unipiloto.pgc.model;

public class Subsidy {
    private int id;
    private int subsidio;
    private Double porcentaje;
    private User usuario;

    public Subsidy() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSubsidio() {
        return subsidio;
    }

    public void setSubsidio(int subsidio) {
        this.subsidio = subsidio;
    }

    public Double getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(Double porcentaje) {
        this.porcentaje = porcentaje;
    }

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }
}
