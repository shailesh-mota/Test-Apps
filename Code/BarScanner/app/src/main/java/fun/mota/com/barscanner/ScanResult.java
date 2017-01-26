package fun.mota.com.barscanner;

/**
 * Created by shaileshmota on 26/01/2017.
 * A Pojo to hold the result of the scanning.
 * Add/delete result contents from {@link com.google.zxing.integration.android.IntentResult}.
 */

public class ScanResult {

    private String format;
    private String content;

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
