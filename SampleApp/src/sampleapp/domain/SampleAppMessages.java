package sampleapp.domain;

/**
 * Stores all the application messages 
 */
public class SampleAppMessages
{
	// Error messages
	public static final String FILE_NOT_FOUND = "Server File not found.";
	public static final String FILE_FORMAT_ERROR = "File has incorrect XML format.";
	public static final String FILE_READING_ERROR = "Not possible to read from Server File.";
	public static final String NOT_ENOUGH_PARAMS = "Incorrect number of parameters.";
	public static final String UNKNOWN_COMMAND = "Unknown command.";
	public static final String INCORRECT_PARAM_FORMAT = "Incorrect parameter format.";
	public static final String GOODBYE = "Goodbye!";
	public static final String PARAMS_SET = "mySQL parameters set successfully.";	
	public static final String LOADING_SERVER_MSG = "Reading server from file with ID=%d and Name=%s ...";
	public static final String NUMBER_REG_SERVERS = "Number of registered servers: %d";
	public static final String SERVER_LIST_HEADING = "--------- Server List ---------";
	public static final String SERVER_LIST_LINE = "Server ID: %d - Name: %s";
	public static final String SERVER_LIST_SUCCESS = "Server list loaded successfully.";
	public static final String SERVER_LIST_ERROR = "Server list not loaded in database.";
	public static final String SERVER_NAME_CHANGED = "Server name was modified.";
	public static final String SERVER_NAME_CHANGE_ERROR = "Server name not changed. Server may not exist in the database.";
	public static final String SERVER_DELETE_SUCCESS = "Server was deleted.";
	public static final String SERVER_DELETE_ERROR = "Server was not deleted. Maybe it was already deleted or does not exist in the database.";
	public static final String ERROR_CLOSING_CONNECTION = "Unable to close connection.";
	public static final String UNKNOWN_ERROR = "Unknown error.";
	
	// Error types
	public static final String OK = "Ok. ";
	public static final String ERROR = "Error: ";
	public static final String DATABASE_ERROR = "mySQLError: ";
}