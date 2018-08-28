package hackathon.project1.commuter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

public class Second_Screen extends AppCompatActivity {

    private BottomNavigationView mainnav;
    private FrameLayout mainframe;
    private Walk_Frag walkfrag;
    private Cab_Frag cabfrag;
    private Bus_Frag busfrag;
    private Metro_Frag metrofrag;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_screen);

        mainframe = (FrameLayout) findViewById(R.id.main_frame);
        mainnav = (BottomNavigationView) findViewById(R.id.main_nav);

        walkfrag = new Walk_Frag();
        cabfrag = new Cab_Frag();
        busfrag = new Bus_Frag();
        metrofrag = new Metro_Frag();

        setFragment(walkfrag);

        mainnav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.nav_walk :
                        setFragment(walkfrag);
                        return true;
                    case R.id.nav_cab :
                        setFragment(cabfrag);
                        return true;
                    case R.id.nav_bus :
                        setFragment(busfrag);
                        return true;
                    case R.id.nav_metro :
                        setFragment(metrofrag);
                        return true;
                    default:
                        return false;

                }
            }


        });
    }
    private void setFragment(Walk_Frag fragment) {

        FragmentTransaction fragmentTransaction =  getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }
}
