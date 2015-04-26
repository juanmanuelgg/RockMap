package moviles.uniandes.com.rockmapv2;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.io.File;


public class MainActivity2 extends Activity
{
    ActionBar.Tab tabBuscar, tabRutas, tabAgregar, tabGPS;

    Fragment fragmentBuscar = new FragmentBuscar();
    Fragment fragmentRutas = new FragmentRutas();
    Fragment fragmentAgregar = new FragmentAgregar();
    Fragment fragmentGPS = new FragmentGPS();


    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getActionBar();

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM,ActionBar.DISPLAY_SHOW_CUSTOM);

        tabBuscar = actionBar.newTab().setIcon(getResources().getDrawable(R.drawable.buscar));
        tabRutas = actionBar.newTab().setIcon(getResources().getDrawable(R.drawable.formatlistbulleted));
        tabAgregar = actionBar.newTab().setIcon(getResources().getDrawable(R.drawable.camera));
        tabGPS = actionBar.newTab().setIcon(getResources().getDrawable(R.drawable.earth));

        tabBuscar.setTabListener(new TabListener(fragmentBuscar));
        tabRutas.setTabListener(new TabListener(fragmentRutas));
        tabAgregar.setTabListener(new TabListener(fragmentAgregar));
        tabGPS.setTabListener(new TabListener(fragmentGPS));

        actionBar.addTab(tabBuscar);
        actionBar.addTab(tabRutas);
        actionBar.addTab(tabAgregar);
        actionBar.addTab(tabGPS);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode,resultCode,data);

    }


}
