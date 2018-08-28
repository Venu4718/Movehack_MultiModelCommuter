package hackathon.project1.commuter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class register extends AppCompatActivity {
    String S_Response;
    EditText user_name;
    EditText mail_id;
    EditText password;
    EditText c_password;
    String URL_POST = "http://10.1.75.53:8000/register/";
    Button create_account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        user_name = (EditText)findViewById(R.id.user);
        mail_id = findViewById(R.id.mailid);
        password = findViewById(R.id.password);
        c_password = findViewById(R.id.confirm_password);
        create_account = (Button)findViewById(R.id.acct_button);

       create_account.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               InsertSV();
           }
       });
    }

   private void InsertSV(){
       StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_POST, new Response.Listener<String>() {


           @Override
           public void onResponse(String response) {
               Toast.makeText(getApplication(), response ,Toast.LENGTH_SHORT).show();
               //S_Response = response;
               Intent reverse = new Intent(register.this, MainActivity.class);
               startActivity(reverse);
               //Toast.makeText(getApplication(),S_Response,Toast.LENGTH_SHORT).show();
           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {
               Toast.makeText(register.this, "Please try again later.",Toast.LENGTH_SHORT).show();
           }
       }){
           @Override
           protected Map<String, String>getParams() throws AuthFailureError{
               Map<String,String> params = new HashMap<String, String>();
               String usr = user_name.getText().toString();
               String pswd = password.getText().toString();
               String mailid = mail_id.getText().toString();
              String c_pass = c_password.getText().toString();
               params.put("confirm_passkey",c_pass);
               params.put("username",usr);
               params.put("password", pswd);
               params.put("mail_id",mailid);
               return params;
           }
       };

       RequestQueue requestQueue = Volley.newRequestQueue(this);
       requestQueue.add(stringRequest);

   }
}
