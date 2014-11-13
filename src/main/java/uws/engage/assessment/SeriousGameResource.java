package uws.engage.assessment;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import java.util.ArrayList;
import java.util.Properties;
 
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.eclipse.xtext.validation.Issue;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import uws.engage.controller.SeriousGameController;

/**
 * Root resource (exposed at "seriousgame" path)
 */
@Path("seriousgame")
/**
 * This class is the SeriousGame class of the web services, it allows
 * 1. To retrieve a serious game configuration (json format)
 * 2. create an SG configuration from a config file (DSL format)
 * 3. Check a DSL config file grammar
 * 4. update an SG configuration from a config file (JSON format)
 * @author Yaelle Chaudy - University of the West of Scotland - yaelle.chaudy@uws.ac.uk
 * @version 1.0
 *
 */
public class SeriousGameResource {
    /**
     * Method handling HTTP GET requests on path "seriousgame/{idSG}/version/{idVersion}"
     * 
     * @param idSG = an integer id of the SG to retrieve
     * @param idVersion = an integer, id of the version of the SG to retrieve
     * @return a json, representing the SG assessment configuration
     */
    @GET
    @Path("/{idSG}/version/{idVersion}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getSeriousGame(@PathParam("idSG") int idSeriousGame, 
                                        @PathParam("idVersion") int version) 
    {
        try
        {
            SeriousGameController sgController = new SeriousGameController();
            return sgController.getConfigFile(idSeriousGame, version).toString();
        }
        catch( Exception e )
        {
            return "{'error':'"+e+"'}";
        }
    }

    /**
     * Method handling HTTP GET requests on path "seriousgame/info/{idSG}/version/{idVersion}"
     * 
     * @param idSG = an integer id of the SG to retrieve
     * @param idVersion = an integer, id of the version of the SG to retrieve
     * @return a json with name, desc... of the SG
     */
    @GET
    @Path("info/{idSG}/version/{idVersion}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getSeriousGameInfo(@PathParam("idSG") int idSeriousGame, 
                                        @PathParam("idVersion") int version) 
    {
        try
        {
            SeriousGameController sgController = new SeriousGameController();
            return sgController.getConfigFile(idSeriousGame, version).get("seriousGame").toString();
        }
        catch( Exception e )
        {
            return "{'error':'"+e+"'}";
        }
    }

    /**
     * Method handling HTTP PUT requests on path "seriousgame/"
     * 
     * @param configFile = the configuration file (DSL format) of the SG to create
     * @return the id of SG created in the database if successful
     */
    @PUT
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String createSeriousGame(String configFile)
    {
    	try
        {
            uws.engage.dsl.generator.Parser engageParser = new uws.engage.dsl.generator.Parser();
            JSONObject configFileJSON = engageParser.getJSONfromDSL(configFile);

            SeriousGameController sgController = new SeriousGameController();
            return sgController.createSG(configFileJSON) + "";
        }
        catch( Exception e )
        {
            return "'error':'"+e+"'";
        }
    }

    /**
     * Method handling HTTP PUT requests on path "seriousgame/check"
     * 
     * @param configFile = the SG configuration file (DSL format)
     * @return an array of errors found in the grammar (JSON format)
     */
    @PUT
    @Path("/check")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String checkDSL(String configFile)
    {
        uws.engage.dsl.generator.Parser engageParser = new uws.engage.dsl.generator.Parser();
        try
        {
            ArrayList<JSONObject> errors = new ArrayList<JSONObject>();
            uws.engage.dsl.generator.ParseResult result = engageParser.parse(configFile);
            if (!result.issues.isEmpty()) {
               for (Issue issue : result.issues) {
                    JSONObject errorLog = new JSONObject(); 
                    errorLog.put("line", issue.getLineNumber());
                    errorLog.put("offset", issue.getOffset());
                    errorLog.put("message", issue.getMessage());
                    errors.add(errorLog);
                }
            }
            return errors.toString();
        }
        catch( Exception e )
        {
            return "'error':'"+e+"'";
        }
    }

    /**
     * Method handling HTTP POST requests on path "seriousgame/"
     * 
     * @param seriousgame = the configuration file (JSON format) of SG to update
     * @return 1 if successful
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String updateSeriousGame(String seriousGameCF)
    {
    	try
        {
            JSONObject configFileJSON=(JSONObject) JSONValue.parse(seriousGameCF);
            SeriousGameController sgController = new SeriousGameController();
            return sgController.createSG(configFileJSON) + "";
        }
        catch( Exception e )
        {
            return "'error':'"+e+"'";
        }
    }
}