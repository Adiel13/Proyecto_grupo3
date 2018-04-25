/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.MATIC.controller;


import com.MATIC.controller.util.Connection;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


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
    
    public void azure(){
         // TODO code application logic here
         HttpClient httpClient = new DefaultHttpClient();
         try {
            // NOTE: You must use the same region in your REST call as you used to obtain your subscription keys.
            //   For example, if you obtained your subscription keys from westcentralus, replace "westus" in the 
            //   URL below with "westcentralus".
            URIBuilder uriBuilder = new URIBuilder("https://southcentralus.api.cognitive.microsoft.com/face/v1.0/detect");
            uriBuilder.setParameter("returnFaceId", "true");
            uriBuilder.setParameter("returnFaceLandmarks", "false");
            uriBuilder.setParameter("returnFaceAttributes", "age,gender,headPose,smile,facialHair,glasses,emotion,hair,makeup,occlusion,accessories,blur,exposure,noise");

            // Prepare the URI for the REST API call
            URI uri = uriBuilder.build();
            HttpPost request = new HttpPost(uri);

            // Request headers. Replace the example key below with your valid subscription key.
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key", "e5035ca930d84748852bd8c89f801e6b");

            // Request body. Replace the example URL below with the URL of the image you want to analyze.
           // StringEntity reqEntity = new StringEntity("{ \"url\": \"https://scontent-mia3-2.xx.fbcdn.net/v/t1.0-9/18118688_1446362395425071_7485885403947294730_n.jpg?_nc_cat=0&_nc_eui2=v1%3AAeHuX1J3FBKQSvnILlw9xLUYsOnboWgZDe3Xw0pnXMDZ1uDuBAj8ExT3lbHlcBG794UxyVqkyk0k12kJXmd4HdLFmS_0IPoRPCU31HDkmpuaPQ&oh=33ae072b31d876dd4080a1c1e8625c4f&oe=5B51A7E7\" }");
            StringEntity reqEntity = new StringEntity("{ \"url\": \"http://1.bp.blogspot.com/-bbUDijSShSk/UvPOcFZpDTI/AAAAAAABgT8/USPimXZapcE/s1600/rostros-africanos-al-oleo+(2).jpg\" }");
            request.setEntity(reqEntity);

            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                 // Format and display the JSON response.
                System.out.println("REST Response:\n");

                String jsonString = EntityUtils.toString(entity).trim();
                switch (jsonString.charAt(0)) {
                    case '[':
                        JSONArray jsonArray = new JSONArray(jsonString);
                        System.out.println(jsonArray.toString(2));
                        break;
                    case '{':
                        JSONObject jsonObject = new JSONObject(jsonString);
                        System.out.println(jsonObject.toString(2));
                        break;
                    default:
                        System.out.println(jsonString);
                        break;
                }
            }
        } catch (IOException | URISyntaxException | ParseException | JSONException e) {
            System.out.println(e.getMessage());
        }
        
    
    }

 
   
}
