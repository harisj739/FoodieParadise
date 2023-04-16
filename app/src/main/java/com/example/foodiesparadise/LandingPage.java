package com.example.foodiesparadise;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.example.foodiesparadise.db.AppDatabase;
import com.example.foodiesparadise.db.UserDAO;

import java.util.List;

public class LandingPage extends AppCompatActivity {

    private static final String USER_ID_KEY = "com.example.foodiesparadise.userIdKey";
    private static final String PREFERENCES_KEY = "com.example.foodiesparadise.PREFERENCES_KEY";
    private TextView mWelcome;
    private UserDAO mUserDAO;
    private int mUserId = -1;

    private SharedPreferences mPreferences = null;
    private User mUser;

    private MenuItem mItem;

    private String[] mRestaurants = {"Restaurant 1", "Restaurant 2", "Restaurant 3"};

    private AutoCompleteTextView mAutoCompleteTextView;

    private ArrayAdapter<String> mAdapterItem;

    private Button mViewOrdersButton;
    private Button mOrderHistoryButton;
    private Button mTrackDataButton;
    private Button mEditInfoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        wireUpDisplay();

        mAutoCompleteTextView.setAdapter(mAdapterItem);
        mAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
            }
        });

        getDatabase();
        checkForUser();
        loginUser(mUserId);
        mWelcome.setText("Welcome " + mUser.getUsername() +"!");
        checkIfAdmin();
    }

    private void wireUpDisplay() {
        mWelcome = findViewById(R.id.welcomeUser);
        mAutoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        mAdapterItem = new ArrayAdapter<>(this, R.layout.list_items, mRestaurants);

        mViewOrdersButton = findViewById(R.id.viewOrders);
        mOrderHistoryButton = findViewById(R.id.orderHistory);
        mTrackDataButton = findViewById(R.id.trackRestaurantData);
        mEditInfoButton = findViewById(R.id.editRestaurantInfo);
    }

    private void checkIfAdmin() {
        if(mUser.isAdmin()) {
            mAutoCompleteTextView.setVisibility(View.GONE);
        }
        else if (!mUser.isAdmin()) {
            mViewOrdersButton.setVisibility(View.GONE);
            mOrderHistoryButton.setVisibility(View.GONE);
            mTrackDataButton.setVisibility(View.GONE);
            mEditInfoButton.setVisibility(View.GONE);
        } //if-elseif
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Similar to ViewBinding; finds the user
        //menu resource and turns it into a menu.
        MenuInflater inflater = getMenuInflater();
        //This is the tool to do that; creates a menu.
        inflater.inflate(R.menu.user_menu, menu);
        mItem = menu.findItem(R.id.userMenu);
        mItem.setTitle(mUser.getUsername());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //You get the item ID from the item that was clicked.
        switch (item.getItemId()) {
            //In the case of the onyl menu item we have,
            //we call logoutUser() and return true.
            case R.id.userMenu:
                logoutUser();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logoutUser() {
        //Displays an AlertDialog Builder at the current context.
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        alertBuilder.setMessage(R.string.logout);

        //The positive button is the button that reads yes
        alertBuilder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                clearUserFromIntent();
                clearUserFromPref();
                mUserId = -1;
                checkForUser();
            }
        });
        alertBuilder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //we don't really need to do anything here
            }
        });
        //
        alertBuilder.create().show();
    }

    private void clearUserFromPref() {
        addUserToPreference(-1);
    }

    private void clearUserFromIntent() {
        getIntent().putExtra(USER_ID_KEY, -1);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }
    private void loginUser(int userId) {
        //We try to pull our user out of the
        //database using the user ID.
        mUser = mUserDAO.getUserByUserId(userId);
        //Adds our user to the shared preference.
        addUserToPreference(userId);
        //Built-in method that is designed to reset
        //the menu with the username. Fires up the
        //overridden onPrepareOptionsMenu() method.
        invalidateOptionsMenu();
    }

    private void addUserToPreference(int userId) {
        //If our shareed preference is null, we
        //get the preference.
        if (mPreferences == null) {
            getPrefs();
        }
        //
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(USER_ID_KEY, userId);
        //We have to call editor.apply() in order
        //for this to work.
        editor.apply();
    }

    private void getPrefs() {
        mPreferences = this.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
    }


    private void getDatabase() {
        mUserDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getUserDAO();
    }

    private void checkForUser() {
        mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);

        if (mUserId != -1) {
            return;
        } //if

        if (mPreferences == null) {
            getPrefs();
        }

        mUserId = mPreferences.getInt(USER_ID_KEY, -1);

        if (mUserId != -1) {
            return;
        }

        List<User> users = mUserDAO.getAllUsers();
        if(users.size() <= 0) {
            User defaultUser = new User ("testuser1", "testuser1", false);
            User defaultUserTwo = new User ("admin2", "admin2", true);
            mUserDAO.insert(defaultUser, defaultUserTwo);
        }

        Intent intent = MainActivity.intentFactory(this);
        startActivity(intent);
    }

    public static Intent intentFactory(Context context, int userId) {
        Intent intent = new Intent(context, LandingPage.class);
        intent.putExtra(USER_ID_KEY, userId);

        return intent;
    }
}