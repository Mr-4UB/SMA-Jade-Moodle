package moodle;

import java.io.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;

import org.jdom2.*;
import org.jdom2.input.*;
import org.jdom2.filter.*;

import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

public class GetCourses {

    public static void main(String[] args) throws ProtocolException, IOException {

        /// NEED TO BE CHANGED
        String token = "03061ad63ec838d98117b858429372f0";
        String domainName = "http://localhost/";

        /// REST RETURNED VALUES FORMAT
        String restformat = "xml"; //Also possible in Moodle 2.2 and later: 'json'
        //Setting it to 'json' will fail all calls on earlier Moodle version
        if (restformat.equals("json")) {
            restformat = "&moodlewsrestformat=" + restformat;
        } else {
            restformat = "";
        }

        /// PARAMETERS - NEED TO BE CHANGED IF YOU CALL A DIFFERENT FUNCTION
        String functionName = "core_course_get_courses";
        String urlParameters = "users[0][username]=" + URLEncoder.encode("testusername1", "UTF-8");//depends on function called

        urlParameters = "";
        /// REST CALL

        // Send request
        String serverurl = domainName + "/webservice/rest/server.php" + "?wstoken=" + token + "&wsfunction=" + functionName;
        HttpURLConnection con = (HttpURLConnection) new URL(serverurl).openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");
        con.setRequestProperty("Content-Language", "en-US");
        con.setDoOutput(true);
        con.setUseCaches (false);
        con.setDoInput(true);
        DataOutputStream wr = new DataOutputStream (
                con.getOutputStream ());
        wr.writeBytes (urlParameters);
        wr.flush ();
        wr.close ();

        //Get Response
        InputStream is =con.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuilder response = new StringBuilder();
        while((line = rd.readLine()) != null) {
            response.append(line);
        }
        rd.close();
        System.out.println(response);

        Document document;
        Element racine;

        //On crée une instance de SAXBuilder
        SAXBuilder sxb = new SAXBuilder();
        try
        {
            //On crée un nouveau document JDOM avec en argument le fichier XML
            //Le parsing est terminé ;)
            document = sxb.build(new StringReader(response.toString()));
            //On initialise un nouvel élément racine avec l'élément racine du document.
            racine = document.getRootElement();
            System.out.println(document);
            List listCoures = racine.getChild("MULTIPLE").getChildren("SINGLE");

            Iterator i = listCoures.iterator();
            while(i.hasNext()){
                Element element = (Element) i.next();
                Cours cours = new Cours();
                cours.setName(getKeyValue(element, "fullname"));
                cours.setId(Integer.valueOf(getKeyValue(element, "id")));
            }
        }
        catch(Exception e){
            System.out.print(e.getMessage());
        }
    }

    protected static String getKeyValue(Element element, String attr){
        try {
            XPathFactory xFactory = XPathFactory.instance();
            XPathExpression<Element> expr = xFactory.compile(String.format("KEY[@name='%s']", attr), Filters.element());
            Element key = expr.evaluateFirst(element);
            return key.getChildText("VALUE");
        }catch (Exception e) {
            e.printStackTrace();
            return "ddd";
        }

    }
}
