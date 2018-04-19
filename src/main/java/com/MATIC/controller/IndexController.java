/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.MATIC.controller;


import com.MATIC.controller.util.Connection;
import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import javax.naming.*;
import javax.naming.directory.*;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author lrodriguez
 */
@Named
@ViewScoped
public class IndexController implements Serializable {

  
    @PostConstruct
    public void init() {
       // probarConexion();
       
    }


    public void probarConexion() {
        /*
        pruebas para insertar, modificar, eliminar y mostrar en consola
        
        */
        Connection conexion = new Connection();
        conexion.insertar("prueba insertar");
       // conexion.actualizar("prueba insertar", "prueba insertar111");
       // conexion.eliminar("prueba insertar");
        conexion.mostrar();
        
        
     }

 
   
}
