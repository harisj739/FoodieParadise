package com.example.foodiesparadise;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.foodiesparadise.db.AppDatabase;
import com.example.foodiesparadise.db.UserDAO;

public class SignUpActivity extends AppCompatActivity {

    private EditText mCreateUsername;
    private EditText mCreatePassword;
    private EditText mConfirmPassword;
    private Button mCreateAccButton;
    private UserDAO mUserDAO;

    private String mUsername;
    private String mPassword;
    private String mVerifyPassword;
    private User mUser;

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
                Intent intent = LoginActivity.intentFactory(getApplicationContext());
                startActivity(intent);
            }
        });
        alertBuilder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                User newUser = new User(mUsername, mPassword, false);
                mUserDAO.insert(newUser);
                Intent intent = LoginActivity.intentFactory(getApplicationContext());
                startActivity(intent);
            }
        });
        //
        alertBuilder.create().show();
    }
    public static Intent intentFactory(Context context) {
        Intent intent = new Intent(context, SignUpActivity.class);

        return intent;
    }
}