package fun.mota.com.barscanner.net;

import android.content.Context;
import android.support.annotation.NonNull;

import fun.mota.com.barscanner.R;

/**
 * Created by shaileshmota on 28/01/2017.
 */

public class Endpoint implements retrofit.Endpoint {
    private static final String SERVER_NAME = "demo_server";
    private String server;

    public Endpoint(@NonNull Context context) {
        server = context.getResources().getString(R.string.server);
    }

    @Override
    public String getUrl() {
        return server;
    }

    @Override
    public String getName() {
        return SERVER_NAME;
    }
}
