/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.MATIC.model;

import java.io.InputStream;

/**
 *
 * @author lizama
 */
public class DatosUsuario {
    private  String nombre;
    private  InputStream imagen;
    private  Emociones emociones;
    
    public DatosUsuario(String nombre, InputStream imagen, Emociones emociones){
        this.nombre = nombre;
        this.imagen = imagen;
        this.emociones = emociones;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setImagen(InputStream imagen) {
        this.imagen = imagen;
    }

    public void setEmociones(Emociones emociones) {
        this.emociones = emociones;
    }
    
    
    
    public String getNombre(){
        return nombre;
    }
    
    public InputStream getImagen() {
        return imagen;
    }
    
    public Emociones getDatos(){
        return emociones;
    }
    
    
}
