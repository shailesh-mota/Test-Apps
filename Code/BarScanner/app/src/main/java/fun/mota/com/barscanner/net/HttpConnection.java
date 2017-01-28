package fun.mota.com.barscanner.net;

import android.content.Context;
import android.support.annotation.NonNull;

import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import fun.mota.com.barscanner.R;
import retrofit.client.OkClient;

/**
 * Created by shaileshmota on 28/01/2017.
 * Set up HTTP connection parameters for the client.
 */

public class HttpConnection extends OkClient{

    private OkHttpClient HttpConnection(@NonNull Context context) {
            OkHttpClient okHttpClient = new OkHttpClient();
            long timeout_conn = context.getResources().getInteger(R.integer.timeout_connection);
            long timeout_read = context.getResources().getInteger(R.integer.timeout_read);

            okHttpClient.setConnectTimeout(timeout_conn, TimeUnit.MILLISECONDS);
            okHttpClient.setReadTimeout(timeout_read, TimeUnit.MILLISECONDS);
            return okHttpClient;
    }
}
