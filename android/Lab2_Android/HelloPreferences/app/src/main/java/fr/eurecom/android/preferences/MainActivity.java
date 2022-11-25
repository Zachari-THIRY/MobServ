package fr.eurecom.android.preferences;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String CHANNEL_ID = "1010";
    private static final String CHANNEL_NAME = "Preference Lab";
    private static final String CHANNEL_DESCRIPTION = "Test Case for Preference Lab Android";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    // This method is called once the menu is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // We have only one menu option
            case R.id.action_settings: {
                // Launch Preference activity
                Intent i = new Intent(this, Preferences.class);
                startActivity(i);
                // A toast is a view containing a quick little message for the
                Toast.makeText(MainActivity.this, "Here you can store your user credentials.", Toast.LENGTH_LONG).show();
                Log.i("Main", "sent an intent to the Preference class!");
                break;
            }
        }
        return true;
    }
    android.content.SharedPreferences preferences;
    // Depends on SDK: you can also use SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialize preferences
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Button show_preferences = (Button) findViewById(R.id.show_preferences);
        show_preferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = preferences.getString("username",
                        "n/a");
                String password = preferences.getString("password",
                        "n/a");
// A toast is a view containing a quick little message for the user.
                showPrefs(username, password);
            }
        });
        // Exit Button
        Button exit_preferences = (Button) findViewById(R.id.exit_preferences);
        exit_preferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
            }
        });

    }
    private void showPrefs(String username, String password){
        Toast.makeText(MainActivity.this,
                "You kept user: " + username + " and password: " + password,
                Toast.LENGTH_LONG).show();
    }
    public void openDialog(View v) {
        // Create out AlterDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Reset username and password?");
        builder.setCancelable(true);
        builder.setPositiveButton("I agree", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"Resetting Credential",Toast.LENGTH_LONG).show();
                reset_preferences();
                createNotificationChannel();
                createNotification();
            }
        });
        builder.setNegativeButton("No, no", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"Keep Credential",Toast.LENGTH_LONG).show();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void reset_preferences(){
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString("username", null);
        edit.putString("password", null);
        edit.commit(); // Apply changes
// A toast is a view containing a quick little message for the
// user. We give a little feedback
        Toast.makeText(MainActivity.this,
                "Reset user name and password",
                Toast.LENGTH_LONG).show();
    }

    public void createNotification() {
        NotificationManager notificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        // prepare intent which is triggered if the notification is selected
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent activity = PendingIntent.getActivity(this, 0, intent, 0);
        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {
            notification = new Notification.Builder(this, CHANNEL_ID)
                    .setContentTitle("Hello Preferences")
                    .setContentText("Successfully reset user Credential")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(activity)
                    .setAutoCancel(true)
                    .build();
        }
        notificationManager.notify(1,notification);
    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME, importance);
            channel.setDescription(CHANNEL_DESCRIPTION);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }}

