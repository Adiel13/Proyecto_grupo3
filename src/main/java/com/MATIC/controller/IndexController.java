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
import com.MATIC.model.DatosUsuario;
import com.MATIC.model.Emociones;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;
import com.mongodb.util.JSON;
import java.io.File;
import static java.io.File.separator;
import java.io.FileOutputStream;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.primefaces.model.tagcloud.DefaultTagCloudModel;
import org.primefaces.model.tagcloud.TagCloudModel;


/**
 *
 * @author lrodriguez
 */
@Named
@ViewScoped
public class IndexController implements Serializable {
    
    Connection conexion = new Connection();
    private String accion;
    private String nombre;
    private String rutaFinal="";
    private UploadedFile uploadFile;
    private String finalUploadFileName;
    List<DatosUsuario> listUsuarios;
    private TagCloudModel model;
    
    DatosUsuario dialogoAnalisis = null;
    private  InputStream inputStream;
    private FileOutputStream output;
    private InputStream inputImage;

  
    
    

      
    public DatosUsuario getDialogoAnalisis(){
        return dialogoAnalisis;
    }
    
    public void setDialogoAnalasis(DatosUsuario dialogoAnalisis){
        this.dialogoAnalisis = dialogoAnalisis;
    }
    
 
    
    public List<DatosUsuario> getListUsuarios(){
        return listUsuarios;
    }
    
    public void setListUsuarios(List<DatosUsuario> listUsuarios){
        this.listUsuarios = listUsuarios;
    }
    public InputStream getInputStream() {
        return inputStream;
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
        dialogoAnalisis = null;
        cargarLista();
    }
    
    public void cargarLista() {
        listUsuarios = new ArrayList<>();
        try (DBCursor cursor = coleccion.find()) {             
            JSONObject json;
            GridFS gfsPhoto;
            while (cursor.hasNext()) {
                DBObject cur = cursor.next();                
                json = new JSONObject(JSON.serialize(cur));
                String stringArray = json.get("Resultado").toString();
                JSONArray jsonArray = new JSONArray(stringArray);
                String nombreBusqueda = cur.get("Nombre").toString();                
                gfsPhoto = new GridFS(BaseDatos, "foto");
                inputStream = gfsPhoto.findOne(nombreBusqueda).getInputStream();
                
                if(inputStream == null)
                {
                    inputStream = this.getClass().getResourceAsStream("/images/user.png");
                }
                               
                DatosUsuario usuario = new DatosUsuario(nombreBusqueda, inputStream, setEmociones(jsonArray));
                listUsuarios.add(usuario);
                if(dialogoAnalisis == null)
                    dialogoAnalisis = usuario;
            }            
        }  
    }
    
    public void mostrarUsuario(DatosUsuario usuarioParametro){
       
        
        
        BasicDBObject query = new BasicDBObject();
	query.put("Nombre", usuarioParametro.getNombre());
        
        DBObject datosDB = coleccion.findOne(query);
        JSONObject json = new JSONObject(JSON.serialize(datosDB));
        String stringArray = json.get("Resultado").toString();
        JSONArray jsonArray = new JSONArray(stringArray);              
        GridFS gfsPhoto = new GridFS(BaseDatos, "foto");
        inputStream = gfsPhoto.findOne(usuarioParametro.getNombre()).getInputStream();                       
        dialogoAnalisis = new DatosUsuario(usuarioParametro.getNombre(), inputStream, setEmociones(jsonArray));  
       
        
    }
    
    public Emociones setEmociones(JSONArray array){
        
        JSONObject faceAttributes = (JSONObject) array.getJSONObject(0).get("faceAttributes");        
        JSONObject emotions = (JSONObject) faceAttributes.get("emotion");    
        System.out.println("Emociones: "+ emotions);
        
        return new Emociones(getPorcentage(emotions.get("anger").toString()),
                getPorcentage(emotions.get("contempt").toString()),
                getPorcentage(emotions.get("disgust").toString()),
                getPorcentage(emotions.get("fear").toString()),
                getPorcentage(emotions.get("happiness").toString()),
                getPorcentage(emotions.get("neutral").toString()),
                getPorcentage(emotions.get("sadness").toString()),
                getPorcentage(emotions.get("surprise").toString()));
        
    }
    
    public String getPorcentage(String texto){
        
        float numero = Float.parseFloat(texto);
        float porcentaje = numero * 100;
        String textoPorcentaje = Float.toString(porcentaje);        
        return textoPorcentaje;
    }
  
    
    public boolean insertar(String inc) throws IOException {
        document = new BasicDBObject();
        document.put("Nombre", nombre);
        document.put("Resultado", inc);
        coleccion.insert(document);
        // Guardar imagen en mongo usando GridFS
       // File archivo = new File(rutaFinal.replace("Fotografias", "MATIC/Fotografias"));
        File archivo = new File(rutaFinal);
        System.out.println("rutaFinal: "+ rutaFinal);
	GridFS foto = new GridFS(BaseDatos, "foto");
	GridFSInputFile archivoGuardar = foto.createFile(archivo);
	archivoGuardar.setFilename(nombre);
	archivoGuardar.save();
        return true;
    }

   /* public void mostrarImagen() throws IOException {
        
        String newFileName = "prueba";
	
        GridFS foto = new GridFS(BaseDatos, "foto");
        BasicDBObject allImages = new BasicDBObject();
	List<GridFSDBFile> listImageForOutput = foto.find(allImages);
        
        for(int i=0; i<listImageForOutput.size();i++) {
            GridFSDBFile imageForOutput = listImageForOutput.get(i);
            imageForOutput.writeTo("c:\\prueba"+i+".png"); //output to new file
        }        
    }*/

    

    /*public void probarConexion() {
          /*
        pruebas para insertar, modificar, eliminar y mostrar en consola
        
        
       
        conexion.insertar("prueba insertar");
       // conexion.actualizar("prueba insertar", "prueba insertar111");
       // conexion.eliminar("prueba insertar");
        conexion.mostrar();
     }*/
    
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
        nombre = "";
        rutaFinal="";
        this.setAccion("R");
        RequestContext req = RequestContext.getCurrentInstance();
        req.execute("PF('wdialogoImagenes').show()");
    }
    public void mostrarDialogoAnalisis(DatosUsuario usuarioParametro) {
        System.out.println("usuarioParametro " + usuarioParametro.getNombre());
        mostrarUsuario(usuarioParametro);
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
            
            File target = getCurrentDir();
            String ruta = target.getAbsolutePath().replace("\\", "/");
            System.out.println("ruta"+ruta);
            inputImage = uploadFile.getInputstream();
            File outFile = new File( ruta +"/"+  finalUploadFileName);
                output = new FileOutputStream(outFile);
            
            try {
                int read = 0;
                byte[] bytes = new byte[1024];              
                while ((read = inputImage.read(bytes)) != -1) {
                    output.write(bytes, 0, read);
                }
                inputImage.close();
                output.flush();
                output.close();
                
                ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
                String rutaArchivoFinal =  outFile.getAbsolutePath();
                rutaArchivoFinal = rutaArchivoFinal.replace(servletContext.getContextPath(), "");
                    
                rutaFinal = rutaArchivoFinal;
                rutaFinal = rutaFinal.replace("Fotografias", "MATIC/Fotografias");
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
            request.setHeader("Content-Type", "application/octet-stream");
            request.setHeader("Ocp-Apim-Subscription-Key", "e5035ca930d84748852bd8c89f801e6b");

            // Request body. Replace the example URL below with the URL of the image you want to analyze.
            /*
            no borrar, son pruebas para leer la imagen
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            int nRead;
            int byteReads;
            ByteArrayOutputStream outputData = new ByteArrayOutputStream(1024);
            while ((byteReads = inputImage.read()) != -1) {
                output.write(byteReads);
            }

            byte[] data = outputData.toByteArray();

            
            StringBuilder sb = new StringBuilder();
            for (byte by : data)
                sb.append(Integer.toBinaryString(by & 0xFF));
            System.out.println("Imagen " + sb.toString());*/
            //ByteArrayEntity  reqEntity = new ByteArrayEntity (buffer.toByteArray());
            
            
            //StringEntity reqEntity = new StringEntity("{ \"url\": \"https://scontent-mia3-2.xx.fbcdn.net/v/t1.0-9/18118688_1446362395425071_7485885403947294730_n.jpg?_nc_cat=0&_nc_eui2=v1%3AAeHuX1J3FBKQSvnILlw9xLUYsOnboWgZDe3Xw0pnXMDZ1uDuBAj8ExT3lbHlcBG794UxyVqkyk0k12kJXmd4HdLFmS_0IPoRPCU31HDkmpuaPQ&oh=33ae072b31d876dd4080a1c1e8625c4f&oe=5B51A7E7\" }");
            //StringEntity reqEntity = new StringEntity("{ \"url\": \"http://1.bp.blogspot.com/-bbUDijSShSk/UvPOcFZpDTI/AAAAAAABgT8/USPimXZapcE/s1600/rostros-africanos-al-oleo+(2).jpg\" }");
            //StringEntity reqEntity = new StringEntity("{ \"url\": \"http://208.109.movil.png\" }");
            //StringEntity reqEntity = new StringEntity(sb.toString());
            
            System.out.println("AZURE RUTA:" + rutaFinal);
            File file = new File(rutaFinal);
            FileEntity reqEntity = new FileEntity(file, "application/octet-stream");
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
                        //System.out.println("Option 0" + jsonArray.toString(2));
                        break;
                    case '{':
                        JSONObject jsonObject = new JSONObject(jsonString);
                        //System.out.println("Option 1" + jsonObject.toString(2));
                        break;
                    default:
                        //System.out.println("Option 3" + jsonString);
                        break;
                }
                System.out.println("-"+jsonString);
                insertar(jsonString);
                cargarLista();
              
            }
        } catch (IOException | URISyntaxException | ParseException | JSONException e) {
            System.out.println(e.getMessage());
        }
        
    
    }

    

    
   
}
