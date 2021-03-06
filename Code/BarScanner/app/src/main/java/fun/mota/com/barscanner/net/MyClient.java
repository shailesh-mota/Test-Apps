package fun.mota.com.barscanner.net;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import fun.mota.com.barscanner.event.CommandEvent;
import fun.mota.com.barscanner.net.restapi.ClientAPI;
import fun.mota.com.barscanner.pojo.EmptyPojo;
import fun.mota.com.barscanner.pojo.ScanResult;

/**
 * Created by shaileshmota on 27/01/2017.
 */

public class MyClient implements NetworkService.Client {
    private static final String TAG = "Client";
    private final NetworkService.ClientService cs;
    private final ClientAPI API;

    public MyClient(final NetworkService.ClientService cs) {
        this.cs = cs;
        this.API = NetworkService.generateAPI(new HttpConnection(), cs, ClientAPI.class);
    }

    @Override
    public void start() {
        EventBus.getDefault().register(this);

    }

    @Override
    public void finish() {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEvent(ScanResult result){
        if(result == null){
            // nothing to upload
            return;
        }

        Log.d(TAG,"Scanned : " + result.getContent() + " " + result.getFormat());
        uploadScanResult(result);

    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEvent(CommandEvent event) {
        switch((CommandEvent.Type)event.getType()){
            case SEND_USER_DATA:
                // TODO call the apropriate API
                break;
            default:
                // do nothing
                break;
        }
    }

    private void uploadScanResult(ScanResult result) {
        try{
            EmptyPojo empty = API.uploadScanResult(cs.getProductName(), result);
        } catch (Exception e) {
            Log.d(TAG, "Connection Error" + e.getMessage());
        }

        // TODO check the response from server and inform UI accordingly with UPLOAD_SCAN_RESULT_SUCCESS/UPLOAD_SCAN_RESULT_FAILURE

    }
}
