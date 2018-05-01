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
    private  String enfado; // anger
    private  String desprecio; //contempt
    private  String disgusto; // disgust
    private  String miedo; //fear
    private  String felicidad; //happiness
    private  String neutral;
    private  String tristeza; //sadness
    private  String sorpresa; //surprise
    
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

    public void setEnfado(String enfado) {
        this.enfado = enfado;
    }

    public void setDesprecio(String desprecio) {
        this.desprecio = desprecio;
    }

    public void setDisgusto(String disgusto) {
        this.disgusto = disgusto;
    }

    public void setMiedo(String miedo) {
        this.miedo = miedo;
    }

    public void setFelicidad(String felicidad) {
        this.felicidad = felicidad;
    }

    public void setNeutral(String neutral) {
        this.neutral = neutral;
    }

    public void setTristeza(String tristeza) {
        this.tristeza = tristeza;
    }

    public void setSorpresa(String sorpresa) {
        this.sorpresa = sorpresa;
    }
    
    
    public String getTristeza(){
        return tristeza;
    }
    
    public String getSorpresa(){
        return sorpresa;
    }    
}
