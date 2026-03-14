package co.edu.unipiloto.pgc.model;

import java.io.Serializable;

public class Rol implements Serializable{

    private int id;
    private String nombre;

    public Rol() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
