package com.example.foodiesparadise;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.foodiesparadise.db.AppDatabase;
import com.example.foodiesparadise.db.UserDAO;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String USER_ID_KEY = "com.example.foodiesparadise.userIdKey";
    private static final String PREFERENCES_KEY = "com.example.foodiesparadise.PREFERENCES_KEY";
    private Button mLoginButton;
    private Button mCreateAccButton;
    private int mUserId = -1;

    private SharedPreferences mPreferences = null;
    private UserDAO mUserDAO;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoginButton = findViewById(R.id.mainLoginButton);
        mCreateAccButton = findViewById(R.id.mainCreateAccButton);

        getDatabase();
        checkForUser();
////                loginUser(mUserId);
//        addUserToPreference(mUserId);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = LoginActivity.intentFactory(getApplicationContext());
                startActivity(intent);
            }
        });

        mCreateAccButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = SignUpActivity.intentFactory(getApplicationContext());
                startActivity(intent);
            }
        });
    }

    private void checkForUser() {
        List<User> users = mUserDAO.getAllUsers();
        if(users.size() <= 0) {
            User defaultUser = new User ("testuser1", "testuser1", false);
            User defaultUserTwo = new User ("admin2", "admin2", true);
            mUserDAO.insert(defaultUser, defaultUserTwo);
        } //if
    }

    private void getDatabase() {
        mUserDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getUserDAO();
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
        Intent intent = new Intent(context, MainActivity.class);

        return intent;
    }
}