package com.example.foodiesparadise;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodiesparadise.db.AppDatabase;
import com.example.foodiesparadise.db.ItemDAO;
import com.example.foodiesparadise.db.RestaurantDAO;
import com.example.foodiesparadise.db.UserDAO;

public class RestaurantPage extends AppCompatActivity {

    private static final String USER_ID_KEY = "com.example.foodiesparadise.userIdKey";
    private LinearLayout mLinearLayout;
    private TextView mRestaurantDisplay;
    private SearchView mRestaurantSearch;
    private ListView mRestaurantList;

    private Button mAddMenuButton;
    private EditText mItemName;
    private EditText mItemType;
    private EditText mItemCost;
    private RestaurantDAO mRestaurantDAO;
    private UserDAO mUserDAO;
    private Restaurant mRestaurant;
    private int mUserId;
    private String itemName;
    private String itemType;
    private Double itemCost;
    private ItemDAO mItemDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_page);

        wireUpDisplay();
        getDatabase();
        mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);
        mRestaurant = mRestaurantDAO.getRestaurantByUserId(mUserId);
        mRestaurantDisplay.setText("Welcome to " + mRestaurant.getRestaurantName());
        mAddMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem();
            }
        });

    }

    private void getDatabase() {
        mRestaurantDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getRestaurantDAO();
        mItemDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getItemDAO();
        mUserDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getUserDAO();
    }

    private void wireUpDisplay() {
        mRestaurantDisplay = findViewById(R.id.restaurantDisplay);
        mAddMenuButton = findViewById(R.id.addMenuButton);
    }

    private void getValuesFromEditText() {
        itemName = mItemName.getText().toString();
        itemType = mItemType.getText().toString();
        String cost = mItemCost.getText().toString();
        itemCost = Double.parseDouble(cost);

    }
    private void addItem() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        mItemName = new EditText(RestaurantPage.this);
        mItemName.setHint("Enter Item Name");
        mItemType = new EditText(RestaurantPage.this);
        mItemType.setHint("Enter Item Type (Entree, Dessert, etc)");
        mItemCost = new EditText(RestaurantPage.this);
        mItemCost.setHint("Enter Item Cost");

        alertBuilder.setMessage("Add Item Information");

        alertBuilder.setView(mItemName);
        alertBuilder.setView(mItemType);
        alertBuilder.setView(mItemCost);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(mItemName);
        layout.addView(mItemType);
        layout.addView(mItemCost);
        alertBuilder.setView(layout);

        alertBuilder.setPositiveButton(getString(R.string.add_item), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getValuesFromEditText();
                Item newItem = new Item(mRestaurant.getRestaurantId(), mUserId, itemName, itemType, itemCost);
                mItemDAO.insert(newItem);
                Toast.makeText(RestaurantPage.this, "The item " + itemName + " has been added to the menu.", Toast.LENGTH_SHORT).show();
            }
        });
        alertBuilder.setNegativeButton(getString(R.string.dismiss), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(RestaurantPage.this, "I will implement the dismiss feature.", Toast.LENGTH_SHORT).show();
            }
        });

        alertBuilder.create().show();
    }

    public static Intent intentFactory(Context context, int userId) {
        Intent intent = new Intent(context, RestaurantPage.class);
        intent.putExtra(USER_ID_KEY, userId);

        return intent;
    }
}