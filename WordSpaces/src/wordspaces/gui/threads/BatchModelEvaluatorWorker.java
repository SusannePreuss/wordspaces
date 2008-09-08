package wordspaces.gui.threads;

import java.io.File;
import javax.swing.SwingWorker;

/**
 *
 * @author alexander
 */
public class BatchModelEvaluatorWorker extends SwingWorker<Object[][],Object[]>{

    File[] models;
    File xmlGroupFile;

    public BatchModelEvaluatorWorker(File[] models, File xmlGroups){
        this.models = models;
        this.xmlGroupFile = xmlGroups;
    }


    @Override
    protected Object[][] doInBackground() throws Exception {

    }

}
