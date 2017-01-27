package fun.mota.com.barscanner.net;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import java.util.concurrent.atomic.AtomicReference;
import fun.mota.com.barscanner.R;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by shaileshmota on 27/01/2017.
 * Handles asynchronous network requests in a handled thread.
 */

public class NetworkService extends Service{
    private Handler handler;
    private final AtomicReference<Client> currentClient = new AtomicReference<>();

    @Override
    public void onCreate() {
        super.onCreate();

        HandlerThread thread = new HandlerThread("NetworkServiceThread");
        thread.start();

        Looper serviceLooper = thread.getLooper();
        this.handler = new Handler(serviceLooper);

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

        void setClient(Client client);

        retrofit.ErrorHandler getErrorHandler();
    }

    private class Service implements ClientService, retrofit.ErrorHandler {

        private final String productName;

        public Service() {
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
        public void setClient(Client client) {
            Client oldClient = currentClient.getAndSet(client);
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
