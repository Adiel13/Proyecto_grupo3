/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.MATIC.model;

/**
 *
 * @author lizama
 */
public class Emociones {
    private final String enfado; // anger
    private final String desprecio; //contempt
    private final String disgusto; // disgust
    private final String miedo; //fear
    private final String felicidad; //happiness
    private final String neutral;
    private final String tristeza; //sadness
    private final String sorpresa; //surprise
    
    public Emociones(String enfado, String desprecio,
            String disgusto, String miedo, String felicidad,
            String neutral, String tristeza, String sorpresa){
        this.enfado = enfado;
        this.desprecio = desprecio;
        this.disgusto = disgusto;
        this.miedo = miedo;
        this.felicidad = felicidad;
        this.neutral = neutral;
        this.tristeza = tristeza;
        this.sorpresa = sorpresa;        
    }
    
    public String getEnfado(){
        return enfado;
    }
    
    public String getDesprecio(){
        return desprecio;
    }
    
    public String getDisgusto(){
        return disgusto;
    }
    
    public String getMiedo(){
        return miedo;
    }
    
    public String getFelicidad(){
        return felicidad;
    }
    
    public String getNeutral(){
        return neutral;
    }
    
    public String getTristeza(){
        return tristeza;
    }
    
    public String getSorpresa(){
        return sorpresa;
    }    
}
