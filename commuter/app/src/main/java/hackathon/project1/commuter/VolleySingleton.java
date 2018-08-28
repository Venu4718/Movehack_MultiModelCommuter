package hackathon.project1.commuter;

import android.app.DownloadManager;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {

    private static VolleySingleton mInstance;
    private RequestQueue mrequestQueue;

    private VolleySingleton (Context context){
        mrequestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static synchronized VolleySingleton getInstance(Context context){
        if (mInstance == null){
            mInstance = new VolleySingleton(context);
        }

        return mInstance;
    }

    public RequestQueue getMrequestQueue() {
        return mrequestQueue;
    }
}
