package in.brainwired.www.multipleforms;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextInputLayout cases_Input,broken_Input;
    private Button add_Button,submit_Button;
    private JSONArray entryList;
    String URL ="http://192.168.0.101/api/test/store";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cases_Input=findViewById(R.id.cases_input_layout);
        broken_Input=findViewById(R.id.broken_bottles_input_layout);
        add_Button=findViewById(R.id.add_button);
        submit_Button=findViewById(R.id.submit_button);
        entryList=new JSONArray();

        add_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToArray();
            }
        });

        submit_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToArray();
                SubmitAllData();
            }
        });
    }

    private void addToArray() {

        String deliverd_cases = cases_Input.getEditText().getText().toString();
        String broken_bottles = broken_Input.getEditText().getText().toString();

        if (deliverd_cases.isEmpty()|| broken_bottles.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"Please Fill Required Fields!",Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("deliverd_cases",deliverd_cases);
            jsonObject.put("broken_bottles",broken_bottles);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        entryList.put(jsonObject);
        cases_Input.getEditText().setText("");
        broken_Input.getEditText().setText("");
    }

    private void SubmitAllData()
    {
        for (int i = 0; i < entryList.length(); i++) {
            JSONObject item = null;
            try {
                item = entryList.getJSONObject(i);
                final String deliverd_cases = item.getString("deliverd_cases");
                final String broken_bottles = item.getString("broken_bottles");
                StringRequest stringRequest = new StringRequest(Request.Method.POST,URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject= new JSONObject(response);
                            if (jsonObject.getBoolean("error"))
                            {
                                Toast.makeText(MainActivity.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(MainActivity.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            Toast.makeText(MainActivity.this,"JSON Exception",Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<>();
                        params.put("deliverd_cases", deliverd_cases);
                        params.put("broken_bottles", broken_bottles);
                        return params;
                    }
                };
                RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        entryList = new JSONArray();
    }
}
