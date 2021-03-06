package com.example.geartrader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class UserListActivity extends AppCompatActivity {
    // Create class-member variables
    private FloatingActionButton openListingButton;
    private FloatingActionButton displayListingsButton;
    private Button returnButton;
    private ListView listingsList;
    private Session session;
    Animation animFadeIn;

    private static final String TAG = "User List";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        session = new Session(this);
        DbHelper dbHelper = new DbHelper(UserListActivity.this);

        // Create clickable ListView object for the listings
        listingsList = findViewById(R.id.listingListView);
        listingsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // For the item menu clicked, extract the ID
                String description = listingsList.getItemAtPosition(position).toString();
                int ID = Integer.parseInt(description.substring(0, description.indexOf(" ")));
                Log.d("UserListActivity", "Clicked Item: " + id + " at position:" + position);
                // If the listing exists, open the SingleListingActivity
                if(dbHelper.checkListingExists(ID)) {
                    Intent intent = new Intent(view.getContext(), PersonalListingActivity.class);
                    intent.putExtra("id", ID);
                    startActivity(intent);
                } else {
                    Log.e(TAG,"clicked null listing");
                    Toast.makeText(UserListActivity.this, "Listing no longer exists, please refresh", Toast.LENGTH_SHORT).show();
                }
            }
        });

        displayAllListings(dbHelper);

        // Create new button object for the displayListings function
        displayListingsButton = findViewById(R.id.displayListingsButton);
        displayListingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAllListings(dbHelper);
            }
        });

        // Create new button object for the displayListings function
        returnButton = findViewById(R.id.returnButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Create new button object for the openListing function
        openListingButton = findViewById(R.id.openListingButton);
        openListingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateListing();
            }
        });
    }

    // Create new intent to start CreateListing Activity
    public void openCreateListing() {
        Log.d(TAG,"open create listing");
        Intent intent = new Intent(this, CreateListingActivity.class);
        startActivity(intent);
    }

    // Function to set the ListView using ListingModel data from the database
    public void displayAllListings(DbHelper dbHelper) {
        Log.d(TAG,"display all listings");
        String user = session.getUsername();
        ArrayAdapter listingsArrayAdapter = new ArrayAdapter<ListingModel>(UserListActivity.this, android.R.layout.simple_list_item_1,
                dbHelper.getAllListingByAuthor(user));
        listingsList.setAdapter(listingsArrayAdapter);
        // Set fade in animation for the ListView
        animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        listingsList.setVisibility(View.VISIBLE);
        listingsList.startAnimation(animFadeIn);
    }
}
