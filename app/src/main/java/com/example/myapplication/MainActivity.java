package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    Button button;
    private mySQLiteDBHandler dbHandler;
    private EditText editText;
    private CalendarView calendarView;
    private String selectedDate;
    private SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener(){
        @Override
            public void onClick(View v){
            String message = "notifcations";
            NotificationCompat.Builder builder= new NotificationCompat.Builder(
                    MainActivity.this
            )
                    .setSmallIcon(R.drawable.ic_message)
                    .setContentTitle("New notifications")
                    .setContentText(message)
                    .setAutoCancel(true);
            Intent intent = new Intent(MainActivity.this,
                    NotificationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("message",message);

            PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this,
                    0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);
            NotificationManager notificationManager = (NotificationManager)getSystemService(
                    Context.NOTIFICATION_SERVICE
            );
             notificationManager.notify(0,builder.build());
        }

        });

        editText = findViewById(R.id.editText);
        calendarView = findViewById(R.id.calendarView2) ;


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                selectedDate = Integer.toString(year) + Integer.toString(month) + Integer.toString(dayOfMonth);
                ReadDatabase(view);
            }
        });
        try {
            dbHandler = new mySQLiteDBHandler(this, "CalendarDatabase", null, 1);
            sqLiteDatabase = dbHandler.getWritableDatabase();
            sqLiteDatabase.execSQL("CREATE TABLE EVENTCalendar(Date TEXT,Event TEXT)");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    public void InsertDatabase(View view) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("Date", selectedDate);
        contentValues.put("Event", editText.getText().toString());
        sqLiteDatabase.insert("EventCalendar", null, contentValues);

    }
    public void ReadDatabase(View view) {
    String query = "Select Event from EventCalendar where Date =" +selectedDate;
    try{
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        cursor.moveToFirst();
        editText.setText(cursor.getString(0));
    }
    catch(Exception e){
        e.printStackTrace();
        editText.setText("");
    }

    }

    }



