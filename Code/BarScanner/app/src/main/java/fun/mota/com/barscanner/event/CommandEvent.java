package fun.mota.com.barscanner.event;

/**
 * Created by shaileshmota on 26/01/2017.
 * Represents any internal command used within this app.
 */

public class CommandEvent extends Event {
    public enum Type {
        UPLOAD_SCAN_RESULT             // Quickly upload collected scan results
    }

    public CommandEvent(Type type) {
        super(type);
    }
}

