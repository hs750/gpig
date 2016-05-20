package gpig.common.messages.handlers;

import gpig.common.messages.FailCommand;

@FunctionalInterface
public interface FailCommandHandler {
    public void handle(FailCommand message);
}
