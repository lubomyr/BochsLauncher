package net.sourceforge.bochsui;

import android.app.*;
import android.os.*;
import android.widget.*;
import android.view.*;
import java.io.*;
import java.util.*;
import android.content.*;

public class MainActivity extends TabActivity 
{
	TextView infoView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

		infoView = (TextView) findViewById(R.id.mainTextView1);
		
		try
		{
			Config.readConfig();
		}
		catch (FileNotFoundException e)
		{
			infoView.setText("config not found");
		}
		infoView.setText("config loaded");
		
        TabHost tabHost = getTabHost();

        TabHost.TabSpec tabSpec;

        tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setIndicator("Storage");
        tabSpec.setContent(new Intent(this, StorageActivity.class));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setIndicator("Misc");
        tabSpec.setContent(new Intent(this, MiscActivity.class));
        tabHost.addTab(tabSpec);
    }
	

	
	public void save(View view) {
		try
		{
			Config.writeConfig();
		}
		catch (IOException e)
		{
			infoView.setText("Error, config not saved");
		}
		infoView.setText("config saved");
	}
	
}
