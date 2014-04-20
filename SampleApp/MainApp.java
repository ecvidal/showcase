import sampleApp.mvp.Presenter;
import sampleApp.mvp.PresenterImpl;
import sampleApp.mvp.View;
import sampleApp.mvp.ViewImpl;

import java.io.IOException;
import java.sql.SQLException;


public class MainApp
{
    private static View view = new ViewImpl();
    private static Presenter presenter = new PresenterImpl(view);

    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException
    {
        // TODO implement - launch the MVP based, command line application. E.g. presenter.init();
    	view.launch(args[]);
    }
}
