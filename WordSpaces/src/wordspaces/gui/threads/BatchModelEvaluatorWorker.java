package wordspaces.gui.threads;

import java.io.File;
import javax.swing.SwingWorker;

/**
 *
 * @author alexander
 */
public class BatchModelEvaluatorWorker extends SwingWorker<Object,Object>{

    File[] models;
    File xmlConfigFile;

    public BatchModelEvaluatorWorker(File[] models, File xmlFile){
        this.models = models;
        this.xmlConfigFile = xmlFile;
    }


    @Override
    protected Object doInBackground() throws Exception {

    }

}
