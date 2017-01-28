package fun.mota.com.barscanner.net;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import java.util.concurrent.atomic.AtomicReference;
import fun.mota.com.barscanner.R;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by shaileshmota on 27/01/2017.
 * Handles asynchronous network requests in a handled thread.
 */

public class NetworkService extends Service{
    private Handler handler;
    private final AtomicReference<MyClient> currentClient = new AtomicReference<>();
    private static final RestAdapter.LogLevel REST_LOG_LEVEL = RestAdapter.LogLevel.FULL;

    public static <T> T generateAPI(HttpConnection conn, final ClientService cs, Class<T> apiClass) {
        RestAdapter rad = new RestAdapter.Builder().setEndpoint(new Endpoint(cs.getAppContext())).setClient(conn)
                .setErrorHandler(cs.getErrorHandler()).setLogLevel(NetworkService.REST_LOG_LEVEL).build();
        return rad.create(apiClass);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        HandlerThread thread = new HandlerThread("NetworkServiceThread");
        thread.start();

        Looper serviceLooper = thread.getLooper();
        this.handler = new Handler(serviceLooper);

        startClient();

    }

    private void startClient() {

        Runnable task = new Runnable() {
            public void run() {
                try {
                    ClientService cs = new CService();
                    Client client = new MyClient(cs);
                    currentClient.set((MyClient)client);
                    client.start();
                } catch (RuntimeException e) {
                    // quit at this point
                    System.exit(0);
                }
            }
        };
        handler.post(task);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Client client = currentClient.getAndSet(null);
        if (client != null) {
            client.finish();
        }
        stopSelf();
    }

    public interface Client {
        void start();

        void finish();

    }

    public interface ClientService {
        Context getAppContext();

        String getProductName();

        Handler getHandler();

        void setClient(MyClient client);

        retrofit.ErrorHandler getErrorHandler();
    }

    private class CService implements ClientService, retrofit.ErrorHandler {

        private final String productName;

        public CService() {
            this.productName = getString(R.string.product);
        }

        @Override
        public Context getAppContext() {
            return getApplicationContext();
        }

        @Override
        public String getProductName() {
            return productName;
        }

        @Override
        public Handler getHandler() {
            return handler;
        }

        @Override
        public void setClient(MyClient client) {
            MyClient oldClient = currentClient.getAndSet(client);
            if (oldClient != null) {
                oldClient.finish();
            }
            client.start();
        }

        @Override
        public retrofit.ErrorHandler getErrorHandler() {
            return this;
        }

        @Override
        public Throwable handleError(RetrofitError err) {
            Response resp = err.getResponse();
            // TODO inform UI based on error codes ?
            Log.v("Error :", "err: " + resp.toString());

            return err;
        }
    }

}
