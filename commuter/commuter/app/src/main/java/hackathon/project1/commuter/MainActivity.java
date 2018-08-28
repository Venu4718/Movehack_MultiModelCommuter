package hackathon.project1.commuter;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button btn;
    private Button login_btn;
    EditText username_id;
    private Handler mhandler = new Handler();

    EditText password_id;
    String URL_POST = "http://10.1.75.53:8000/login/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        login_btn = findViewById(R.id.loginbutton);
        username_id = (EditText) findViewById(R.id.username_login);
        password_id = (EditText) findViewById(R.id.password_login);

        btn = findViewById(R.id.register_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,register.class);
                startActivity(intent);
            }
        });
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_credentials();
     }
        });


    }

    public void send_credentials(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_POST, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                if (response.equals("YES"))
                {
                    Intent intent = new Intent(MainActivity.this,Second_Screen.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Wro" +
                                    "ng Credentials!",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Oops !! Something went Wrong",Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                String usr = username_id.getText().toString();
                String pswd = password_id.getText().toString();
                params.put("USERNAME",usr);
                params.put("PASSWORD",pswd);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


//    public void startrepeat() {
//        //mhandler.postDelayed(mtoast,2000);
//        mtoast.run();
//    }
//
//    private Runnable mtoast = new Runnable() {
//        @Override
//        public void run() {
//            Toast.makeText(MainActivity.this,"delayed msg",Toast.LENGTH_SHORT).show();
//            mhandler.postDelayed(this,5000);
//        }
//    };
}
