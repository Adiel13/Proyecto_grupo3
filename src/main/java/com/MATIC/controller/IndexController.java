/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.MATIC.controller;


import com.MATIC.controller.util.Connection;
import static com.MATIC.controller.util.Connection.BaseDatos;
import static com.MATIC.controller.util.Connection.coleccion;
import static com.MATIC.controller.util.Connection.document;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import java.io.File;
import static java.io.File.separator;
import java.io.FileOutputStream;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
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
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;


/**
 *
 * @author lrodriguez
 */
@Named
@ViewScoped
public class IndexController implements Serializable {
    
    Connection conexion = new Connection();
    private String accion;
    private String nombre="";
    private String rutaFinal="";
    private UploadedFile uploadFile;
    private String finalUploadFileName;
    List<DBObject> ListImg;

    public List<DBObject> getListImg() {
        return ListImg;
    }

    public void setListImg(List<DBObject> ListImg) {
        this.ListImg = ListImg;
    }
    
    
    public String getRutaFinal() {
        return rutaFinal;
    }

    public void setRutaFinal(String rutaFinal) {
        this.rutaFinal = rutaFinal;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public UploadedFile getUploadFile() {
        return uploadFile;
    }

    public void setUploadFile(UploadedFile uploadFile) {
        this.uploadFile = uploadFile;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }
    
    @PostConstruct
    public void init() {
       
       cargarLista();
      
       
    }
    
    public void cargarLista() {

        DBCursor lisCursor = coleccion.find();
        ListImg = lisCursor.toArray();

        Iterator iter = ListImg.iterator();
        int cont = 0;
        while (iter.hasNext()) {
            System.out.println(cont + ": " + iter.next().toString());
            cont++;
        }

    }

 
    
    public void ListarImagenes() {
      /*  
        try (DBCursor cursor = coleccion.find()) {
            while (cursor.hasNext()) {
                DBObject cur = cursor.next();
                System.out.println(cur);
            }
        }
        
    */
       
        GridFS gridFoto = new GridFS(BaseDatos, "foto");
	DBCursor cursor = gridFoto.getFileList();
	while (cursor.hasNext()) {
		System.out.println(cursor.next());
	}
     
    }
    
    public boolean insertar(String inc) throws IOException {
        document.put("Nombre", nombre);
        
        //document.put("Ruta", rutaFinal);
        
        // Guardar imagen en mongo usando GridFS
        File archivo = new File(rutaFinal);
	GridFS foto = new GridFS(BaseDatos, "foto");
	GridFSInputFile archivoGuardar = foto.createFile(archivo);
	archivoGuardar.setFilename(nombre);
	archivoGuardar.save();
                
        document.put("Resultado", inc);
        coleccion.insert(document);
        return true;
    }

    public void mostrarImagen() throws IOException {
        
        String newFileName = "prueba";
	
        GridFS foto = new GridFS(BaseDatos, "foto");
        BasicDBObject allImages = new BasicDBObject();
	List<GridFSDBFile> listImageForOutput = foto.find(allImages);
        
        for(int i=0; i<listImageForOutput.size();i++) {
            GridFSDBFile imageForOutput = listImageForOutput.get(i);
            imageForOutput.writeTo("c:\\prueba"+i+".png"); //output to new file
        }        
    }

    

    public void probarConexion() {
          /*
        pruebas para insertar, modificar, eliminar y mostrar en consola
        
        */
       
        conexion.insertar("prueba insertar");
       // conexion.actualizar("prueba insertar", "prueba insertar111");
       // conexion.eliminar("prueba insertar");
        conexion.mostrar();
     }
    
      public File getCurrentDir() {
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        //System.out.println(servletContext.getContextPath());        
        File target = new File(servletContext.getRealPath("") +separator + "Fotografias");
        if(!target.exists())
            target.mkdir();
        target = new File(servletContext.getRealPath("") +separator + "Fotografias" + separator +"imageComp");
        if(!target.exists())
            target.mkdir();
       return target;
    }
      
    public void mostrarDialogoImagenes() {
        this.setAccion("R");
        RequestContext req = RequestContext.getCurrentInstance();
        req.execute("PF('wdialogoImagenes').show()");
    }
    public void mostrarDialogoAnalisis() {
        this.setAccion("R");
        RequestContext req = RequestContext.getCurrentInstance();
        req.execute("PF('wdialogoAnalisis').show()");
    }

     public void handleFileUpload(FileUploadEvent event) {
        System.err.println("save");
        uploadFile = event.getFile();
        String ext = uploadFile.getFileName().substring(uploadFile.getFileName().lastIndexOf("."));
       try{
            finalUploadFileName =  uploadFile.getFileName().replace(" ","")+ "";// +Calendar.getInstance().getTimeInMillis() + ext;
            
            finalUploadFileName = finalUploadFileName.substring(0, finalUploadFileName.indexOf("."));
            finalUploadFileName = finalUploadFileName + Calendar.getInstance().getTimeInMillis() + ext;
            String nombreFile = finalUploadFileName;
            
            File target = getCurrentDir();
            String ruta = target.getAbsolutePath().replace("\\", "/");
            System.out.println("ruta"+ruta);
            InputStream input = uploadFile.getInputstream();
            File outFile = new File( ruta +"/"+  finalUploadFileName);
            OutputStream output = new FileOutputStream(outFile);
            
            try {
                int read = 0;
                byte[] bytes = new byte[1024];
              
                while ((read = input.read(bytes)) != -1) {
                    output.write(bytes, 0, read);
                }
              
                input.close();
                output.flush();
                output.close();
                
                
               
                
                ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
                String rutaArchivoFinal =  outFile.getAbsolutePath();
                rutaArchivoFinal = rutaArchivoFinal.replace(servletContext.getContextPath(), "");
                    
                rutaFinal = rutaArchivoFinal;
               
             } catch (IOException e) {
                e.printStackTrace();
                throw e;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
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
                insertar(jsonString);
                cargarLista();
                nombre="";
                rutaFinal="";
            }
        } catch (IOException | URISyntaxException | ParseException | JSONException e) {
            System.out.println(e.getMessage());
        }
        
    
    }

 
   
}
