package gpig.c2.data.external;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import gpig.all.schema.GPIGData;
import gpig.c2.config.C2Config;
import gpig.common.data.Constants;
import gpig.common.data.external.ExternalDataTranslator;
import gpig.common.messages.handlers.DetectionNotificationHandler;

public class DataImporter extends Thread {
    private final List<DetectionNotificationHandler> detectionHandler;
    private final List<URL> files;

    public DataImporter(C2Config config) {
        this.detectionHandler = new ArrayList<>();
        this.files = config.interoperabilityInputs;

    }

    public void addHandler(DetectionNotificationHandler handler) {
        detectionHandler.add(handler);
    }

    @Override
    public void run() {
        super.run();
        while (true) {
            String input = "";

            for (URL f : files) {
                try {
                    Scanner s = new Scanner(f.openStream());

                    StringBuilder sb = new StringBuilder();

                    while (s.hasNextLine()) {
                        sb.append(s.nextLine());
                    }

                    s.close();

                    input = sb.toString();

                    GPIGData data = ExternalDataTranslator.deserialise(input);
                    ExternalDataTranslator.extractDetections(data)
                            .forEach(det -> detectionHandler.forEach(handler -> handler.handle(det)));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }

            try {
                Thread.sleep(Constants.EXTERNAL_INPUT_RATE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
