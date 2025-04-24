package org.example.modelo;

import org.bson.types.ObjectId;

public class Cliente {
    private ObjectId id;
    private String nombre;
    private String cedula;
    private String telefono;
    private String email;
    private String billete;
    private boolean verificado;
    private String encargado;

    public Cliente() {
    }

    public Cliente(ObjectId id, String cedula, String nombre, String telefono, String email, String billete, Boolean verificado, String encargado) {
        this.id = id;
        this.cedula = cedula;
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
        this.billete = billete;
        this.verificado = verificado;
        this.encargado = encargado;
    }




    // Getters y setters
    public String getNombre() {
        return nombre;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBillete() {
        return billete;
    }

    public void setBillete(String billete) {
        this.billete = billete;
    }

    public boolean isVerificado() {
        return verificado;
    }

    public void setVerificado(boolean verificado) {
        this.verificado = verificado;
    }

    public String getEncargado() {return encargado;}

    public void setEncargado(String encargado) {this.encargado = encargado;}
    @Override
    public String toString() {
        return "Cliente [nombre=" + nombre + ", cedula=" + cedula + ", telefono=" + telefono
                + ", email=" + email + ", billete=" + billete + "]";
    }

}
