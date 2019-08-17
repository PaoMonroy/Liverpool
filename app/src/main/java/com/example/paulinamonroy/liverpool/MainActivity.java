package com.example.paulinamonroy.liverpool;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.example.paulinamonroy.liverpool.adapters.ProductAdapter;
import com.example.paulinamonroy.liverpool.model.Product;
import com.example.paulinamonroy.liverpool.services.ApiClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    static ArrayList<String> searches = new ArrayList<String>();
    AutoCompleteTextView editText;
    static List<Product> productList;
    private static RecyclerView recyclerView;
    private static ProductAdapter mAdapter;
    static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        editText = findViewById(R.id.autoCompleteTextView);
        Button button = findViewById(R.id.button);
        recyclerView = findViewById(R.id.rvProducts);


        searches = getArrayList("List");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, searches);
        editText.setAdapter(adapter);
        button.setOnClickListener(this);
    }


    public void saveArrayList(ArrayList<String> list, String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();
    }

    public ArrayList<String> getArrayList(String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(json, type);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button:

                //Guarda Criterio
                String search = editText.getText().toString();
                if(!searches.contains(search)){
                    searches.add(search);
                    saveArrayList(searches, "List");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, searches);
                    editText.setAdapter(adapter);
                }
                //Realiza búsqueda
                ApiClient apiClient = new ApiClient();
                productList = apiClient.getProducts(search);
                hideKeyboard(this);

                break;
        }
    }

    public static void setRecyclerView(){
        mAdapter = new ProductAdapter(productList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


}
