package sampleapp.mvp;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import sampleapp.domain.Database;
import sampleapp.domain.SampleAppMessages;
import sampleapp.domain.Server;

public class PresenterImpl implements Presenter
{
    private final View view;
    private Database database = null;

    public PresenterImpl(View v)
    {    	
        view = v;
    }
    
	/**
	 * Starts the application, displaying the welcome text and handling the user input 
	 */
    public void init(){
    	database = new Database();
    	view.launch();
   		this.handleUserInput();
    }

    /**
	 * Accepts commands from the user, redirecting the program flow accordingly              
	 */
	private void handleUserInput(){
		
	    Scanner sc = new Scanner(System.in);	    	      
	    view.printPrompt();
	    
	    while (sc.hasNextLine()) {
	    	String line = sc.nextLine();
	    	if (line.startsWith("help"))				view.showUsageHelp();
	    	else if (line.startsWith("login"))			processLogin(line);
	    	else if (line.startsWith("loadServers"))	loadServers(line);
	    	else if (line.startsWith("countServers"))	countServers();
	    	else if (line.startsWith("deleteServer"))	deleteServer(line);
	    	else if (line.startsWith("editServer"))		editServer(line);
	    	else if (line.startsWith("listServers"))	listServers();
	    	else if (line.startsWith("quit"))			{view.printMessage(SampleAppMessages.OK, SampleAppMessages.GOODBYE); System.exit(0);}
	    	else view.printMessage(SampleAppMessages.ERROR, SampleAppMessages.UNKNOWN_COMMAND);

	    	view.printPrompt();
	    }
	}
		
	/**
	 * Checks if a command has the necessary parameters 
	 * 
	 * @command String array with the command and its parameters
	 * @numberOfParams Number of parameters to check for
	 * @returns True if is valid, false if its not            
	 */
	private boolean commandParamsAreValid(String[] command, int numberOfParams){
		if (command.length != numberOfParams){
			view.printMessage(SampleAppMessages.ERROR, SampleAppMessages.NOT_ENOUGH_PARAMS);			
			return false;
		}		
		else return true;
	}
	
	
	/**
	 * Processes the 'login' command, setting the database object 
	 * 
	 * @line The 'login' command line with its parameters
	 * @returns The same object, passed as a parameter, with its contents filled in hashmap format
	 *             
	 */	
	private void processLogin(String line){
		String[] splittedLine = line.split(" ");
		
		if (commandParamsAreValid(splittedLine, 4)){		
			database.setConnectionString(splittedLine[1]);
			database.setUsername(splittedLine[2]);
			database.setPassword(splittedLine[3]);		
			view.printMessage(SampleAppMessages.OK, SampleAppMessages.PARAMS_SET);
		}
	}
		
	
	/**
	 * Lists the registered servers in the database 
	 * 
	 */		
	private void listServers(){
		
		try{
			List<Server> serverList = database.listServers();
			view.printMessage("", SampleAppMessages.SERVER_LIST_HEADING);

			for (int i=0; i < serverList.size(); i++){
				Server server = serverList.get(i);
				view.printMessage("", String.format(SampleAppMessages.SERVER_LIST_LINE, server.getId(), server.getName()))	;
			}
		}catch(Exception e){
			view.printMessage(SampleAppMessages.DATABASE_ERROR, e.getMessage());
			database.close(database.getConnection());
		}
		
	}

	/**
	 * Return the total count of servers registered in the database 
	 * 
	 */
	private void countServers(){		
		int nServers = 0;
		
		try{
			nServers = database.countServers();		
			view.printMessage(SampleAppMessages.OK, String.format(SampleAppMessages.NUMBER_REG_SERVERS, nServers));
		}catch(Exception e){
		 	view.printMessage(SampleAppMessages.DATABASE_ERROR, e.getMessage());			 	
		 	database.close(database.getConnection());			
		}
	}

	
	/**
	 * 
	 * Loads the servers from a XML file into the database 
	 * 
	 * @line Command line with the file to load passed as a parameter
	 *             
	 */
	private void loadServers(String line){
		String[] splittedLine = line.split(" ");
		int result = 0;

		try{
			if (commandParamsAreValid(splittedLine, 2)){
				String pathToFile = splittedLine[1];		
				List<Server> serverList = this.processXMLFile(pathToFile);
				result = database.loadServer(serverList);
				
				switch (result){
					case 1: {view.printMessage(SampleAppMessages.OK, SampleAppMessages.SERVER_LIST_SUCCESS); break;}
					case 0: {view.printMessage(SampleAppMessages.ERROR, SampleAppMessages.SERVER_LIST_ERROR); break;}
				}
			}			
		}catch(FileNotFoundException fe){
			view.printMessage(SampleAppMessages.ERROR, SampleAppMessages.FILE_NOT_FOUND);
		}catch(IOException ioe){
			view.printMessage(SampleAppMessages.ERROR, SampleAppMessages.FILE_READING_ERROR);
		}catch(SAXException se){
			view.printMessage(SampleAppMessages.ERROR, SampleAppMessages.FILE_FORMAT_ERROR + se.getMessage());
		}catch(ParserConfigurationException pce){
			view.printMessage(SampleAppMessages.ERROR, SampleAppMessages.FILE_FORMAT_ERROR + pce.getMessage());
		}catch(Exception e){
			// if an error occurs during the inserts, it rollbacks the transaction to keep the information coherent
			try {
				database.getConnection().rollback();  
			} catch (SQLException e1) {
				view.printMessage(SampleAppMessages.DATABASE_ERROR, e1.getMessage());
			}
			view.printMessage(SampleAppMessages.DATABASE_ERROR, e.getMessage());
		}
		
	}

	/**
	 * Edits a registered server, modifying its name in the database 
	 * 
	 * @line Command line with the id of the server to change and the new name for it
	 *             
	 */
	private void editServer(String line){
		String[] splittedLine = line.split(" ");
		int result = 0;
		
			try{
				if (commandParamsAreValid(splittedLine, 3)){
					int id = Integer.parseInt(splittedLine[1]);
					String newName = splittedLine[2];
					Server server = new Server(id, newName);
					result = database.editServer(server);
					
					switch (result){
						case 1: {view.printMessage(SampleAppMessages.OK, SampleAppMessages.SERVER_NAME_CHANGED); break;}
						case 0: {view.printMessage(SampleAppMessages.ERROR, SampleAppMessages.SERVER_NAME_CHANGE_ERROR); break;}
					}
				}
			} catch(NumberFormatException ne){
				view.printMessage(SampleAppMessages.ERROR, SampleAppMessages.INCORRECT_PARAM_FORMAT);
			} catch(Exception e){
				view.printMessage(SampleAppMessages.DATABASE_ERROR, e.getMessage());
			}
		
	}

	/**
	 * Deletes a registered server 
	 * 
	 * @line Command line with the id of the server to delete
	 *             
	 */	
	private void deleteServer(String line){
		String[] splittedLine = line.split(" ");
		int result = 0;

			try{				
				if (commandParamsAreValid(splittedLine, 2)){
					int id = Integer.parseInt(splittedLine[1]);
					Server server = new Server(id, "");		
					result = database.deleteServer(server);
					
					switch (result){
						case 1: {view.printMessage(SampleAppMessages.OK, SampleAppMessages.SERVER_DELETE_SUCCESS); break;}
						case 0: {view.printMessage(SampleAppMessages.ERROR, SampleAppMessages.SERVER_DELETE_ERROR); break;}
					}

				}
			} catch(NumberFormatException ne){
				view.printMessage(SampleAppMessages.ERROR, SampleAppMessages.INCORRECT_PARAM_FORMAT);
			} catch(Exception e){
				view.printMessage(SampleAppMessages.DATABASE_ERROR, e.getMessage());
			}
	}
	
	
	/**
	 * Processes an XML file, using a basic SAX Parser 
	 * 
	 * @pathToFile Path to the XML file to be processed
	 * @return List of server objects from the XML file
	 * @throws FileNotFoundException If the file does not exist
	 * @throws IOException If there's an error reading from the file
	 * @throws SAXException If there's an error processing the XML
	 * @throws ParserConfigurationException If there's an error in the parser configuration
	 *             
	 */
	private List<Server> processXMLFile(String pathToFile) throws FileNotFoundException, IOException, SAXException, ParserConfigurationException{
		final List<Server> serverList = new ArrayList<Server>();
			  
				SAXParserFactory factory = SAXParserFactory.newInstance();
				SAXParser saxParser = factory.newSAXParser();
			 
				DefaultHandler handler = new DefaultHandler() {
							
					boolean id = false;
					boolean name = false;
					Server server = new Server();
								 
					public void startElement(String uri, String localName,String qName, Attributes attributes) throws SAXException {
						
						if (qName.equalsIgnoreCase("xs2:id")) {
							id = true;
						}			 
						if (qName.equalsIgnoreCase("xs2:name")) {
							name = true;
						}			 			
					}
				 
					public void endElement(String uri, String localName, String qName) throws SAXException { 
					}
				 
					public void characters(char ch[], int start, int length) throws SAXException {
											
						if (id) {
							String idStr = new String(ch, start, length);
							server.setId(Integer.parseInt(idStr));
							id = false;
						}
				 
						if (name) {
							String nameStr = new String(ch, start, length);
							server.setName(nameStr);
							view.printMessage("", String.format(SampleAppMessages.LOADING_SERVER_MSG, server.getId(), server.getName()));
							serverList.add(server);
							name = false;							
						}					
					}			 
			     };
			 
			     saxParser.parse(pathToFile, handler);			     			    			    
			     return serverList;
	}

		
}
