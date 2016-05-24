package gpig.c2.data.external;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import gpig.all.schema.GPIGData;
import gpig.c2.data.C2Data;
import gpig.common.data.Assignment.AssignmentStatus;
import gpig.common.data.Constants;
import gpig.common.data.Detection;
import gpig.common.data.external.ExternalDataTranslator;
import gpig.common.messages.DeliveryNotification;
import gpig.common.util.Log;

public class DataExporter extends Thread {
    private final C2Data data;
    private final File file;

    public DataExporter(C2Data data) {
        this.data = data;
        this.file = Constants.EXTERNAL_OUTPUT_LOCATION;
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                Log.error("Unable to export data to file " + file.getPath());
                e.printStackTrace();
            }
        }
        
        start();
    }

    @Override
    public void run() {
        super.run();

        while (true) {
            List<Detection> det = data.getDetections();
            List<DeliveryNotification> del = data.getAssignments().stream()
                    .filter(a -> a.status == AssignmentStatus.DELIVERED)
                    .map(d -> new DeliveryNotification(data.getDeliveryTimes().get(d), d)).collect(Collectors.toList());

            GPIGData data = ExternalDataTranslator.export(det, del);
            String dataString = ExternalDataTranslator.serialise(data);

            try {
                FileWriter fileWriter = new FileWriter(file, false);
                fileWriter.write(dataString);
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                Log.error("Unable to export data to file " + file.getPath());
                e.printStackTrace();
            }

            try {
                Thread.sleep(Constants.EXTERNAL_OUTPUT_RATE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
