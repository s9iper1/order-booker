package com.byteshaft.order_booker.activites;


import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.byteshaft.order_booker.AppGlobals;
import com.byteshaft.order_booker.R;
import com.byteshaft.order_booker.utils.Helpers;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;

import org.json.JSONException;
import org.json.JSONObject;

public class OrderActivity extends AppCompatActivity {

    private EditText orderThingName;
    private EditText fromWhere;
    private EditText orderTimeDate;
    private Helpers mHelpers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_activity);
        mHelpers = new Helpers();
        orderThingName = (EditText) findViewById(R.id.order_et);
        fromWhere = (EditText) findViewById(R.id.from_where_et);
        orderTimeDate = (EditText) findViewById(R.id.time_date_et);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("Fill in the details");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_done) {
            String orderProduct = orderThingName.getText().toString();
            String from = fromWhere.getText().toString();
            String deliveryTime = orderTimeDate.getText().toString();
            if (orderProduct.isEmpty() || from.isEmpty() || deliveryTime.isEmpty()) {
                Toast.makeText(getApplicationContext(), "you must fill all the fields",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
            ParseQuery<ParseInstallation> parseQuery = ParseQuery.getQuery(ParseInstallation.class);
            parseQuery.whereEqualTo("admin", "admin_order_receiver");
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("name" , mHelpers.getDataFromSharedPreference(AppGlobals.KEY_Name));
                jsonObject.put("phone", mHelpers.getDataFromSharedPreference(AppGlobals.KEY_MOBILE_NUMBER));
                jsonObject.put("address", mHelpers.getDataFromSharedPreference(AppGlobals.KEY_address));
                jsonObject.put("product", orderProduct);
                jsonObject.put("from", from);
                jsonObject.put("delivery_time", deliveryTime);
                jsonObject.put("sender_id", AppGlobals.sAndroid_id);
                System.out.println(AppGlobals.sAndroid_id);
                System.out.println(mHelpers.getDataFromSharedPreference(AppGlobals.KEY_MOBILE_NUMBER));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            ParsePush.sendDataInBackground(jsonObject, parseQuery);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
