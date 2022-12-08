package com.example.farmerverse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.farmerverse.notification.NotificationReceiver;
import com.example.farmerverse.databinding.ActivityMainBinding;
import com.example.farmerverse.viewmodel.FarmerverseViewModel;

import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;
    public static final int NEW_SEED_ACTIVITY_REQUEST_CODE = 1;
    private FarmerverseViewModel farmerverseViewModel;

    //Notification
    Calendar notificationCalendar = Calendar.getInstance();
//    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    public static final String CONTENT_NOTIFICATION = " Take a picture of your crop!";
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    private final static String default_notification_channel_id = "default";
//    public Button demoButton;
    public Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        farmerverseViewModel = new ViewModelProvider(this).get(FarmerverseViewModel.class);

        setSupportActionBar(binding.toolbar);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        context = binding.getRoot().getContext();

//        DEMO PURPOSES
//        demoButton = findViewById(R.id.btnDate);
//        demoButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                scheduleNotification(createNotification("Take a picture of your crop!"), 2000);
//            }
//        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.menu_home) {
            while (navController.popBackStack()) {
            }
            navController.navigate(R.id.homeFragment);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onSupportNavigateUp()
    {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

    private void scheduleNotification(Notification notification, long delay){
        Intent notificationIntent = new Intent(this, NotificationReceiver.class );
        notificationIntent.putExtra(NotificationReceiver.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationReceiver.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, delay, pendingIntent);
    }

    private Notification createNotification(String content){

        NotificationCompat.Builder builder = new NotificationCompat.Builder( this, default_notification_channel_id )
                .setContentTitle("Attention Farmer!")
                .setContentText(content)
                .setSmallIcon(androidx.transition.R.drawable.notification_template_icon_bg)
                .setAutoCancel(true)
                .setChannelId(NOTIFICATION_CHANNEL_ID);

        return builder.build();
    }

    public void setDate (View view) {
        new DatePickerDialog(MainActivity. this, date ,
                notificationCalendar.get(Calendar. YEAR ) ,
                notificationCalendar.get(Calendar. MONTH ) ,
                notificationCalendar.get(Calendar. DAY_OF_MONTH )
        ).show() ;
    }

    private void createInformationForNotifcation() {
        String myFormat = "dd/MM/yy" ; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat , Locale.getDefault ()) ;
        Date date = notificationCalendar.getTime() ;

        scheduleNotification(createNotification(CONTENT_NOTIFICATION), date.getTime()) ;

        // Toast let user know that the notification has been scheduled.
        Toast.makeText(this, "Your reminder to take a picture on the " + sdf.format(date) + " has been set.",
                Toast.LENGTH_LONG).show();
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet (DatePicker view , int year , int monthOfYear , int dayOfMonth) {
            notificationCalendar.set(Calendar. YEAR , year) ;
            notificationCalendar.set(Calendar. MONTH , monthOfYear) ;
            notificationCalendar.set(Calendar. DAY_OF_MONTH , dayOfMonth) ;
            createInformationForNotifcation() ;
        }
    };
}


////////// Resources Used
//https://www.tutorialspoint.com/how-to-create-everyday-notifications-at-certain-time-in-android#:
// ~:text=This%20example%20demonstrate%20about%20How%20to%20create%20everyday,2%20%E2%88%92%20Add
// %20the%20following%20code%20to%20res%2Flayout%2Factivity_main.xml.