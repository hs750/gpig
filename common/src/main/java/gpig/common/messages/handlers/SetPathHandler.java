package gpig.common.messages.handlers;

import gpig.common.messages.SetPath;

@FunctionalInterface
public interface SetPathHandler {
    public void handle(SetPath message);
}
