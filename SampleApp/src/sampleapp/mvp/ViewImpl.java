package sampleapp.mvp;


public class ViewImpl implements View
{		

	@Override
	public void launch(){		
		System.out.println("Database sample app");
		System.out.println("===================");
		System.out.println("Please run the 'login' command first to connect to mySQL. (Syntax: login connection_string username password)");
		System.out.println("Example for the login command: login jdbc:mysql://localhost/testDB myuser mypass");
		System.out.println("You can type 'help' anytime to display the available commands");		
	}


	
	public void showUsageHelp(){
        System.out.println("\nhelp: to display this message");
        System.out.println("login: connects to mySQL database. Syntax: login connection_string username password");
        System.out.println("countServers: to display the current number of servers present");
        System.out.println("loadServers: to populate the database. Syntax: loadServer pathToFile.xml");
        System.out.println("editServer: to change the name of a server identified by id. Syntax: editServer id newServerName");
        System.out.println("deleteServer: to delete a server. Syntax: deleteServer id");
        System.out.println("listServers: to display details of all servers servers");
        System.out.println("quit: to exit the program");
	}
	
	/**
	 * Prints a basic message to the user, with a type (ERROR, OK, MYSQLERROR) and a message 
	 */
	public void printMessage(String type, String message){
		System.out.println(type + message);
	}

	
	/**
	 * Prints the user prompt 
	 */	
	public void printPrompt(){
		System.out.print("\n> ");
	}

}
