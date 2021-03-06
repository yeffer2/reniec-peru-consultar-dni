/*
 * Autor : Cmop
 */
package conectarWeb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;

/**
 *
 * @author CMOP
 */
public class BusquedaCookie {

    public static final String USER_AGENT_FIREFOX = "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.0.10) Gecko/2009042316 Firefox/3.0.10 (.NET CLR 3.5.30729)";
    public static final String NOMBRE_COOKIE = "JSESSIONID";

    public List<Object> buscarCookieLogueo(String urlseguridad) throws Exception {
        List<Object> rpta = new ArrayList<Object>(1);
        List<HttpCookie> cookies;
        HttpsURLConnection connection = null;
        try {
            //Activar Oyente de Cookies
            CookieManager manager = new CookieManager();
            manager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
            CookieHandler.setDefault(manager);
            //-----------------------------------------------------------
            //Asignar User Agent para evitar el filtro de navegador
            System.setProperty("http.agent", USER_AGENT_FIREFOX);
            //Crear un objeto URL
            URL url = new URL(urlseguridad);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setSSLSocketFactory(SSLByPass.getInstancia().getSslSocketFactory());
            connection.setRequestMethod("POST");
            connection.setDoInput(true);  // Esto permite leer el contenido despues de la peticion
            connection.connect(); //Conectar
            connection.getContent(); //Obtener contenido          

            //Leyendo la Cookie
            CookieStore cookieJar = manager.getCookieStore();
            cookies = cookieJar.getCookies();
            // Leer la rpta
            rpta.add(cookies);
            StringBuilder answer = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                answer.append(line);
            }
            reader.close();
            connection.disconnect();
            rpta.add(answer);
        } catch (IOException e) {
            if (connection.getResponseCode() == 500) {
                System.out.println("No se puede obtener Cookies " + e);
            }
            throw e;
        } catch (Exception e) {
            System.out.println("No se puede obtener Cookies " + e);
            throw e;
        }
        return rpta;
    }
}
