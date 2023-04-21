package com.example.foodiesparadise;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodiesparadise.db.AppDatabase;
import com.example.foodiesparadise.db.RestaurantDAO;
import com.example.foodiesparadise.db.UserDAO;

public class RegisterRestaurant extends AppCompatActivity {

    private static final String USER_ID_KEY = "com.example.foodiesparadise.userIdKey";
    private static final String RESTAURANT_ID_KEY = "com.example.foodiesparadise.restaurantIdKey";
    private static final String PREFERENCES_KEY = "com.example.foodiesparadise.PREFERENCES_KEY";

    private TextView mWelcomeAdmin;

    private EditText mRestaurantName;
    private EditText mRestaurantCuisine;
    private EditText mRestaurantLocation;
    private Button mRegisterButton;
    private String restaurantName;
    private String restaurantCuisine;
    private String restaurantLocation;
    private RestaurantDAO mRestaurantDAO;
    private Restaurant mRestaurant;
    private UserDAO mUserDAO;
    private int mUserId = -1;
    private User mUser;
    private SharedPreferences mPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_restaurant);

        wireUpDisplay();
    }

    private void wireUpDisplay() {
        mWelcomeAdmin = findViewById(R.id.welcomeAdmin);
        mRestaurantName = findViewById(R.id.editTextRestaurantName);
        mRestaurantCuisine = findViewById(R.id.editTextRestaurantCuisine);
        mRestaurantLocation = findViewById(R.id.editTextRestaurantLocation);
        mRegisterButton = findViewById(R.id.registerButton);

        getRestaurantDatabase();
        getUserDatabase();
        mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);
        mUser = mUserDAO.getUserByUserId(mUserId);
        mWelcomeAdmin.setText("Welcome " + mUser.getUsername() + "!");

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validRestaurant()) {
                    getValuesFromDisplay();
                    Restaurant newRestaurant = new Restaurant(restaurantName, restaurantLocation, restaurantCuisine, mUserId);
                    mRestaurantDAO.insert(newRestaurant);
                    mRestaurant = mRestaurantDAO.getRestaurantByName(restaurantName);
                    addRestaurantToPreference(mRestaurant.getRestaurantId());
                    setUpRestaurantPage();
                }
                else {
                    Toast.makeText(RegisterRestaurant.this, "The restaurant name " + restaurantName + " is already taken.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setUpRestaurantPage() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        alertBuilder.setMessage("Would you like to set up " + restaurantName + "'s home page?");

        //The positive button is the button that reads yes
        alertBuilder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = RestaurantPage.intentFactory(getApplicationContext(), mUserId);
                startActivity(intent);
            }
        });
        alertBuilder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = LoginActivity.intentFactory(getApplicationContext());
                startActivity(intent);
            }
        });
        //
        alertBuilder.create().show();
    }

    private void addRestaurantToPreference(int restaurantId) {
        if (mPreferences == null) {
            getPrefs();
        }
        //
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(RESTAURANT_ID_KEY, restaurantId);
        editor.apply();
    }

    private void getPrefs() {
        mPreferences = this.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
    }

    private void getValuesFromDisplay() {
        restaurantName = mRestaurantName.getText().toString();
        restaurantCuisine = mRestaurantCuisine.getText().toString();
        restaurantLocation = mRestaurantLocation.getText().toString();
    }

    private boolean validRestaurant() {
        mRestaurant = mRestaurantDAO.getRestaurantByName(restaurantName);
        if (mRestaurant != null) {
            return false;
        }
        return true;
    }

    private void getRestaurantDatabase() {
        mRestaurantDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getRestaurantDAO();
    }

    private void getUserDatabase() {
        mUserDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getUserDAO();
    }

    public static Intent intentFactory(Context context, int userId) {
        Intent intent = new Intent(context, RegisterRestaurant.class);
        intent.putExtra(USER_ID_KEY, userId);

        return intent;
    }
}