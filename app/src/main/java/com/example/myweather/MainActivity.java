package com.example.myweather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    TextView CityName;
    EditText CityEditText;
    ImageButton searchBtn;
    TextView CurrTemp;
    ImageView ConditonImg;
    TextView ConditionTextView;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_main);

        CityName = findViewById(R.id.CityName);
        CityEditText = findViewById(R.id.CityEditText);
        searchBtn = findViewById(R.id.SearchButton);
        CurrTemp = findViewById(R.id.CurrentTemp);
        ConditonImg = findViewById(R.id.ConditionImg);
        ConditionTextView =  findViewById(R.id.ConditionText);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));

        getClimateInfo("Jalgaon");
        AddRecycleElements("Jalgaon");

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String citytext = CityEditText.getText().toString();
                if(!citytext.equals("")){
                    CityName.setText(citytext);
                    getClimateInfo(citytext);
                    AddRecycleElements(citytext);
                }
                else{
                    Toast.makeText(MainActivity.this, "Enter City Name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getClimateInfo(String city){

        String url = "https://api.weatherapi.com/v1/current.json?key=085018d6ab9e4f0ea8941655222509&q="+city+"&aqi=yes";

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Gaurav","Response : "+response);
                try {
                    JSONObject CurrentObject = response.getJSONObject("current");
                    String temp_c = CurrentObject.getString("temp_c");
                    CurrTemp.setText(temp_c+"°C");
                    JSONObject ConditionObject = CurrentObject.getJSONObject("condition");
                    String ConditionImgUrl = "https:"+ConditionObject.getString("icon");
                    Log.d("Gaurav","Condition Url : "+ ConditionImgUrl);
                    Picasso.get().load(ConditionImgUrl).into(ConditonImg);
                    String ConditionText = ConditionObject.getString("text");
                    ConditionTextView.setText(ConditionText);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Gaurav","Response Error !");
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void AddRecycleElements(String city){
        ArrayList<model> arr;
        arr = new ArrayList<>();
        CityName.setText(city);

        String url = "https://api.weatherapi.com/v1/forecast.json?key=085018d6ab9e4f0ea8941655222509&q="+city+"&days=1&aqi=yes&alerts=no";

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Gaurav","Response : "+response);
                try {
                    JSONObject ForeCastObject = response.getJSONObject("forecast");
                    JSONArray jsonArray = ForeCastObject.getJSONArray("forecastday");
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    JSONArray hourArray = jsonObject.getJSONArray("hour");
                    for(int i = 0;i<hourArray.length();i++){
                        JSONObject hourObject = hourArray.getJSONObject(i);
                        String timeC = hourObject.getString("time");
                        String tempC = hourObject.getString("temp_c");
                        JSONObject ConditionObject = hourObject.getJSONObject("condition");
                        String iconC = ConditionObject.getString("icon");
                        String wind_kph = hourObject.getString("wind_kph");
                        arr.add(new model(tempC,timeC,wind_kph,iconC));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RecycleViewAdapter adapter = new RecycleViewAdapter(getApplicationContext(),arr);
                recyclerView.setAdapter(adapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Gaurav","Response Error !");
            }
        });
        requestQueue.add(jsonObjectRequest);





    }
}