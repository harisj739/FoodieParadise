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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodiesparadise.db.AppDatabase;
import com.example.foodiesparadise.db.UserDAO;

public class LoginActivity extends AppCompatActivity {

    private static final String USER_ID_KEY = "com.example.foodiesparadise.userIdKey";
    private UserDAO mUserDAO;
    private EditText mUsernameField;
    private EditText mPasswordField;
    private String mUsername;
    private String mPassword;
    private User mUser;
    private Button mButton;

    private TextView mLoginDisplay;

    private static final String PREFERENCES_KEY = "com.example.foodiesparadise.PREFERENCES_KEY";
    private TextView mWelcome;
    private int mUserId = -1;

    private SharedPreferences mPreferences = null;

    private MenuItem mItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        wireUpDisplay();

        getDatabase();
    }

    private void wireUpDisplay() {
        mUsernameField = findViewById(R.id.editTextLogInUserName);
        mPasswordField = findViewById(R.id.editTextLoginPassword);
        mLoginDisplay = findViewById(R.id.loginDisplay);

        mButton = findViewById(R.id.button);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getValuesFromDisplay();
                if (checkForUserInDatabase()) {
                    if (!validatePassword()) {
                        Toast.makeText(LoginActivity.this, "Invalid password", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = LandingPage.intentFactory(getApplicationContext(), mUser.getUserId());
                        startActivity(intent);
                    } //if-else
                } //if
            }
        });
    }

    private void getValuesFromDisplay() {
        mUsername = mUsernameField.getText().toString();
        mPassword = mPasswordField.getText().toString();
    }

    private boolean checkForUserInDatabase() {
        //We pull the username from the display.
        mUser = mUserDAO.getUserByUsername(mUsername);
        //If the username is null, we say that the username
        //isn't found and return false.
        if (mUser == null) {
            Toast.makeText(this, "no user " + mUsername + " found", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Checks to see if the password supplied (mPassword)
     * matches the password supplied in the database
     * (mUser.getPassword()). If it does, return true.
     * Otherwise, returns false.
     * @return
     */
    private boolean validatePassword() {
        return mUser.getPassword().equals(mPassword);
    }

    private void getDatabase() {
        mUserDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getUserDAO();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Similar to ViewBinding; finds the user
        //menu resource and turns it into a menu.
        MenuInflater inflater = getMenuInflater();
        //This is the tool to do that; creates a menu.
        inflater.inflate(R.menu.exit_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //You get the item ID from the item that was clicked.
        switch (item.getItemId()) {
            //In the case of the onyl menu item we have,
            //we call logoutUser() and return true.
            case R.id.returnToHomePage:
                backToMainActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void backToMainActivity() {
        //Displays an AlertDialog Builder at the current context.
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        alertBuilder.setMessage(R.string.returnToHomePage);

        //The positive button is the button that reads yes
        alertBuilder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = MainActivity.intentFactory(getApplicationContext());
                startActivity(intent);
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

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }
    public static Intent intentFactory(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);

        return intent;
    }

//    public static Intent intentFactory(Context context, int userId) {
//        Intent intent = new Intent(context, LoginActivity.class);
//        intent.putExtra(USER_ID_KEY, userId);
//
//        return intent;
//    }

}