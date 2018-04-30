/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.MATIC.controller;

import java.io.InputStream;

/**
 *
 * @author lizama
 */
public class DatosUsuario {
    private final String nombre;
    private final InputStream imagen;
    private final Emociones emociones;
    
    public DatosUsuario(String nombre, InputStream imagen, Emociones emociones){
        this.nombre = nombre;
        this.imagen = imagen;
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
