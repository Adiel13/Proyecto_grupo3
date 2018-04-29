/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.MATIC.controller;

import java.io.InputStream;
import org.json.JSONObject;

/**
 *
 * @author lizama
 */
public class DatosUsuario {
    private String nombre;
    private InputStream imagen;
    private JSONObject datos;
    
    public DatosUsuario(String nombre, InputStream imagen, JSONObject datos){
        this.nombre = nombre;
        this.imagen = imagen;
        this.datos = datos;
    }
    
    public String getNombre(){
        return nombre;
    }
    
    public InputStream getImagen() {
        return imagen;
    }
    
    public JSONObject getDatos(){
        return datos;
    }
    
}
