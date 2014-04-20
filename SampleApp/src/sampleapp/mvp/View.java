package sampleapp.mvp;


public abstract interface View{
	
	public void launch();
	
	public void showUsageHelp();
	
	public void printMessage(String type, String message);
	
	public void printPrompt();
}
