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
import android.widget.Toast;

import com.example.foodiesparadise.db.AppDatabase;
import com.example.foodiesparadise.db.UserDAO;

public class SignUpActivity extends AppCompatActivity {

    private static final String USER_ID_KEY = "com.example.foodiesparadise.userIdKey";

    private static final String PREFERENCES_KEY = "com.example.foodiesparadise.PREFERENCES_KEY";
    private EditText mCreateUsername;
    private EditText mCreatePassword;
    private EditText mConfirmPassword;
    private Button mCreateAccButton;
    private UserDAO mUserDAO;

    private String mUsername;
    private String mPassword;
    private String mVerifyPassword;
    private User mUser;
    private SharedPreferences mPreferences;
    private User mNewUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        wireUpDisplay();
        mCreateAccButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDatabase();
                getValuesFromDisplay();
                if(!validUsername()) {
                    Toast.makeText(SignUpActivity.this, "The username " + mUsername + " is already taken.", Toast.LENGTH_SHORT).show();
                } else {
                    if (!validPassword()) {
                        Toast.makeText(SignUpActivity.this, "The passwords don't match.", Toast.LENGTH_SHORT).show();
                    } else {
                        userOrAdmin();
                    } //if-else
                } //if-else
            }
        });

    }

    private void getDatabase() {
        mUserDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getUserDAO();
    }
    private void wireUpDisplay() {
        mCreateUsername = findViewById(R.id.editTextCreateUserName);
        mCreatePassword = findViewById(R.id.editTextCreatePassword);
        mConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        mCreateAccButton = findViewById(R.id.createAccButton);
    }

    private void getValuesFromDisplay() {
        mUsername = mCreateUsername.getText().toString();
        mPassword = mCreatePassword.getText().toString();
        mVerifyPassword = mConfirmPassword.getText().toString();
    }

    private boolean validUsername() {
        mUser = mUserDAO.getUserByUsername(mUsername);
        if(mUser != null) {
            return false;
        }
        return true;
    }

    private boolean validPassword() {
        if (!mPassword.equals(mVerifyPassword)) {
            return false;
        }
        return true;
    }

    private void userOrAdmin() {
        boolean admin = true;
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        alertBuilder.setMessage("Would you like to create an admin account?");

        //The positive button is the button that reads yes
        alertBuilder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                User newUser = new User(mUsername, mPassword, true);
                mUserDAO.insert(newUser);
                mUser = mUserDAO.getUserByUsername(mUsername);
//                loginUser(mUser.getUserId());
                addUserToPreference(mUser.getUserId());
                Intent intent = RegisterRestaurant.intentFactory(getApplicationContext(), mUser.getUserId());
                startActivity(intent);
            }
        });
        alertBuilder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                User newUser = new User(mUsername, mPassword, false);
                mUserDAO.insert(newUser);
                mUser = mUserDAO.getUserByUsername(mUsername);
                addUserToPreference(mUser.getUserId());
                Intent intent = LoginActivity.intentFactory(getApplicationContext());
                startActivity(intent);
            }
        });
        //
        alertBuilder.create().show();
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
//        invalidateOptionsMenu();
    }

    private void addUserToPreference(int userId) {
        if (mPreferences == null) {
            getPrefs();
        }
        //
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(USER_ID_KEY, userId);
        editor.apply();
    }

    private void getPrefs() {
        mPreferences = this.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
    }
    public static Intent intentFactory(Context context) {
        Intent intent = new Intent(context, SignUpActivity.class);

        return intent;
    }
}