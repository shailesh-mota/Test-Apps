package fun.mota.com.barscanner.net.restapi;

import fun.mota.com.barscanner.pojo.EmptyPojo;
import fun.mota.com.barscanner.pojo.ScanResult;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by shaileshmota on 27/01/2017.
 */

public interface ClientAPI {
    // TODO change this path to the end point
    String PATH="/api/test-app/{productId}";

    String UPLOAD_SCAN_ENDPOINT="/upload/scan";
    @POST(PATH+UPLOAD_SCAN_ENDPOINT)
    EmptyPojo uploadScanResult(@Path("productId") String productID, @Body ScanResult result);

    //TODO add a new end point : to upload user data
    /*
    String UPLOAD_SCAN_ENDPOINT="/upload/scan";
    @POST(PATH+UPLOAD_SCAN_ENDPOINT)
    EmptyPojo uploadScanResult(@Path("productId") String productID, @Body ScanResult result);
     */
}
