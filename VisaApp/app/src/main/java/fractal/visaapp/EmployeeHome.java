package fractal.visaapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class EmployeeHome extends AppCompatActivity {

    String empCode;
    Intent intent;
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_home);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Employee Dashboard");
        EmployeeHome.context = getApplicationContext();
        intent = getIntent();
        empCode = intent.getStringExtra("empCode");
    }
    public void evisa(View view){
        Intent iv=new Intent(this, VisaForm.class);
        iv.putExtra("empCode", empCode);
        startActivity(iv);
    }
    public void elogout(View view){
        Intent ilog=new Intent(this, LoginActivity.class);
        ilog.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(ilog);
    }

    public void onClickViewProfile(View v) {
        Intent iv = new Intent(this, EmployeeProfile.class);
        iv.putExtra("empCode", empCode);
        startActivity(iv);
    }

    public void upload(View v) {
        Intent up=new Intent(this,Upload.class);
        up.putExtra("empCode", empCode);
        startActivity(up);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);

//        MenuItem searchItem = menu.findItem(R.id.action_search);
//        SearchView searchView =
//                (SearchView) MenuItemCompat.getActionView(searchItem);

        // Configure the search info and add any event listeners...

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_logout:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Intent ilog=new Intent(this, LoginActivity.class);
                ilog.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(ilog);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public static Context getAppContext() {
        return EmployeeHome.context;
    }
}

