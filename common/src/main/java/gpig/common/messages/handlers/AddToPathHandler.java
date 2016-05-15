package gpig.common.messages.handlers;

import gpig.common.messages.AddToPath;

@FunctionalInterface
public interface AddToPathHandler {
    public void handle(AddToPath message);
}
