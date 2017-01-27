package fun.mota.com.barscanner.event;

/**
 * Created by shaileshmota on 26/01/2017.
 * Represents any internal command used within this app.
 */

public class CommandEvent extends Event {
    public enum Type {
        UPLOAD_SCAN_RESULT_SUCCESS,            // Upload collected scan results, success
        UPLOAD_SCAN_RESULT_FAILED,             // Upload collected scan results, failure
        SEND_USER_DATA,                        // Upload user data
        SENDING_USER_DATA_SUCCESS,             // Upload user data, success
        SENDING_USER_DATA_FAILURE              // Upload user data, failure
    }

    public CommandEvent(Type type) {
        super(type);
    }
}

