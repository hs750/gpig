package gpig.c2;

<<<<<<< Upstream, based on origin/master
import java.io.IOException;

import gpig.c2.config.C2Config;
import gpig.common.networking.CommunicationChannel;
import gpig.common.networking.MessageReceiver;
import gpig.common.networking.MessageSender;

public class C2 {
=======
import gpig.c2.gui.GUI;
>>>>>>> ca1dac6 Latlong to xy conversion

<<<<<<< Upstream, based on origin/master
    public static void main(String... args) throws IOException {
=======
public class C2 {
	private GUI gui;

	public static void main(String... args) {
>>>>>>> ca1dac6 Latlong to xy conversion
        System.out.println("Starting C2");
        C2 c2 = new C2();
        
<<<<<<< Upstream, based on origin/master
        C2Config conf = C2Config.getConfig(C2Config.class);
        
        MessageReceiver msgFromDCs = new MessageReceiver();
        CommunicationChannel c2dcChannel = new CommunicationChannel(conf.c2dcChannel, msgFromDCs);
        MessageSender msgToDCs = new MessageSender(c2dcChannel);
        
=======
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	c2.gui = new GUI();
                c2.gui.createAndShowGUI();
            }
        });
>>>>>>> a4b514d The most basic GUI.
    }
    
    

}
