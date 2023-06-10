package com.example.bibaragi;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    public static myDBHelper myHelper;
    public static SQLiteDatabase sqlDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myHelper = new myDBHelper(this);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    public class myDBHelper extends SQLiteOpenHelper {
        public  myDBHelper(Context context) {super(context, "groupDB", null, 1);}

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE groupSubject (gSubject CHAR(20) PRIMARY KEY, gProfessor CHAR(20), gClass CHAR(20), gWeek CHAR(20), gStart CHAR(20), gEnd CHAR(20));");
            db.execSQL("CREATE TABLE groupReport (gWorkNumber CHAR(20) PRIMARY KEY, gSubject CHAR(20) , gWork CHAR(100) , gEndTime CHAR(20) , gCheck CHAR(20));");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS groupSubject");
            db.execSQL("DROP TABLE IF EXISTS groupReport");
            onCreate(db);
        }
    }
}