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
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

		try
		{
			Config.readConfig();
		}
		catch (FileNotFoundException e)
		{
			Toast.makeText(MainActivity.this, "config not found", Toast.LENGTH_SHORT).show();
		}
		Toast.makeText(MainActivity.this, "config loaded", Toast.LENGTH_SHORT).show();
		
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
			Toast.makeText(MainActivity.this, "Error, config not saved", Toast.LENGTH_SHORT).show();
		}
		Toast.makeText(MainActivity.this, "config saved", Toast.LENGTH_SHORT).show();
	}
	
}
