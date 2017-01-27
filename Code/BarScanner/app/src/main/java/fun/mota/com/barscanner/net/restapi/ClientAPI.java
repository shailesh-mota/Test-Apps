package fun.mota.com.barscanner.net.restapi;

import fun.mota.com.barscanner.pojo.EmptyPojo;
import fun.mota.com.barscanner.pojo.ScanResult;
import retrofit.http.Body;
import retrofit.http.POST;
import retrofit.http.Path;


/**
 * Created by shaileshmota on 27/01/2017.
 * All the client APIs used within the code.
 */

public interface ClientAPI {
    // TODO change this path to the correct path
    String PATH="/api/test-app/{productId}";

    String UPLOAD_SCAN_ENDPOINT="/upload/scan";
    @POST(PATH+UPLOAD_SCAN_ENDPOINT)
    EmptyPojo uploadScanResult(@Path("productId") String productID, @Body ScanResult result);

    //TODO add a new path : to upload user data
    /*
    String UPLOAD_SCAN_ENDPOINT="/upload/scan";
    @POST(PATH+UPLOAD_SCAN_ENDPOINT)
    EmptyPojo uploadScanResult(@Path("productId") String productID, @Body ScanResult result);
     */
}
