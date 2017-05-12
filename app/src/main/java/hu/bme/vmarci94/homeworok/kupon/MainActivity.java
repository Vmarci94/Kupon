package hu.bme.vmarci94.homeworok.kupon;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import hu.bme.vmarci94.homeworok.kupon.adapter.LoginPagerAdapter;

public class MainActivity extends FragmentActivity {

    //A ViewPager működéséhez szükség van egy adapter ami szolgáltatja a fragmenteket
    private ViewPager pager; //ViewPager ... ami a felületleíróban van
    private PagerAdapter pagerAdapter; //adapter hozzá

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new LoginPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
    }
}
