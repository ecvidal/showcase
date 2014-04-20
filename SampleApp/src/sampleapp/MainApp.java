package sampleapp;

import sampleapp.mvp.Presenter;
import sampleapp.mvp.PresenterImpl;
import sampleapp.mvp.View;
import sampleapp.mvp.ViewImpl;


public class MainApp
{
    private static View view = new ViewImpl();
    private static Presenter presenter = new PresenterImpl(view);

    public static void main(String[] args) throws Exception
    {
    	presenter.init();   	
    }
}
