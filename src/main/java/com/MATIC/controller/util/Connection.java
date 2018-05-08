/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.MATIC.controller.util;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author lrodriguez
 */
public class Connection {

    public static DB BaseDatos;
    public static DBCollection coleccion;
    public static BasicDBObject document = new BasicDBObject();

    public Connection() {
       try {
            Mongo mongo = new Mongo("127.0.0.1", 27017);
            BaseDatos = mongo.getDB("MATIC_3");
            coleccion = BaseDatos.getCollection("MATIC_3");
            System.out.println("Se conecto correctamente");


        } catch (UnknownHostException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    public boolean insertar(String accion) {
        document.put("accion", accion);
        coleccion.insert(document);
        return true;
    }

    public void mostrar() {
        DBCursor cursor = coleccion.find();
        while (cursor.hasNext()) {
            System.out.println(cursor.next());
        }
    }
    
    public void listarRegistros() {
        DBCursor cursor = coleccion.find();
        while (cursor.hasNext()) {
            System.out.println(cursor.next());
        }
    }

    public boolean actualizar(String accionVieja, String accionNueva) {
        document.put("accion", accionVieja);
        BasicDBObject documentNuevo = new BasicDBObject();
        documentNuevo.put("accion", accionNueva);
        coleccion.findAndModify(document, documentNuevo);
        return true;
    }

    public boolean eliminar(String accion) {
        document.put("accion", accion);
        coleccion.remove(document);
        return true;
    }

}
