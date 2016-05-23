package gpig.dc;

import gpig.common.messages.FailCommand;
import gpig.common.messages.handlers.FailCommandHandler;
import gpig.common.networking.MessageReceiver;
import gpig.common.networking.MessageSender;

/**
 * Forward fail commands to drones so that they may spoof the failures
 * 
 *
 */
public class FailCommandForwarder implements FailCommandHandler {
    private MessageSender deliverySender;
    private MessageSender detectionSender;

    public FailCommandForwarder(MessageReceiver c2Receiver, MessageSender deliverySender,
            MessageSender detectionSender) {
        c2Receiver.addHandler(this);
        this.deliverySender = deliverySender;
        this.detectionSender = detectionSender;
    }

    @Override
    public void handle(FailCommand message) {
        deliverySender.send(message);
        detectionSender.send(message);

    }
}
