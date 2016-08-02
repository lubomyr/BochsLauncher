package net.sourceforge.bochsui;

import net.sourceforge.bochsui.adapter.TabsPagerAdapter;
import net.sourceforge.bochsui.R;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import java.io.*;
import android.widget.*;
import android.view.*;
import android.util.*;
import android.content.*;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener
{
	private CheckBox cbFloppyA;
	private CheckBox cbFloppyB;
	private CheckBox cbAta0m;
	private CheckBox cbAta0s;
	private CheckBox cbAta1m;
	private CheckBox cbAta1s;
	private TextView tvFloppyA;
	private TextView tvFloppyB;
	private TextView tvAta0m;
	private TextView tvAta0s;
	private TextView tvAta1m;
	private TextView tvAta1s;
	private CheckBox cbVvfatAta0m;
	private CheckBox cbVvfatAta0s;
	private CheckBox cbVvfatAta1m;
	private CheckBox cbVvfatAta1s;
	private Button btBrowseFloppyA;
	private Button btBrowseFloppyB;
	private Button btBrowseAta0m;
	private Button btBrowseAta0s;
	private Button btBrowseAta1m;
	private Button btBrowseAta1s;
	private Spinner spAta0mType;
	private Spinner spAta0sType;
	private Spinner spAta1mType;
	private Spinner spAta1sType;
	private Spinner spBoot;

	private Button btRomImage;
	private Button btVgaRomImage;
	private TextView tvRomImage;
	private TextView tvVgaRomImage;
	private EditText editMegs;

    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    // Tab titles
    private String[] tabs = { "Storage", "Misc" };

    @Override
    protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

		if (!Config.configLoaded)
		{
			try
			{
				Config.readConfig();

			}
			catch (FileNotFoundException e)
			{
				Toast.makeText(MainActivity.this, "config not found", Toast.LENGTH_SHORT).show();
			}
			Toast.makeText(MainActivity.this, "config loaded", Toast.LENGTH_SHORT).show();
			Config.configLoaded = true;
		}


        // Initilization
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);        

        // Adding Tabs
        for (String tab_name : tabs)
		{
            actionBar.addTab(actionBar.newTab().setText(tab_name)
							 .setTabListener(this));
        }

        /**
         * on swiping the viewpager make respective tab selected
         * */
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

				@Override
				public void onPageSelected(int position)
				{
					// on changing the page
					// make respected tab selected
					actionBar.setSelectedNavigationItem(position);
				}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2)
				{
					if (arg0 == 0)
					{
						applyTabStorage();
						setupTabStorage();
					}
					else if (arg0 == 1)
					{
						applyTabMisc();
					}
				}

				@Override
				public void onPageScrollStateChanged(int arg0)
				{
				}
			});

    }
	
	public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
	
	public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.start) {
            save(null);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft)
	{
    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft)
	{
        // on tab selected
        // show respected fragment view
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft)
	{
    }

	public void save(View view)
	{
		try
		{
			Config.writeConfig();
		}
		catch (IOException e)
		{
			Toast.makeText(MainActivity.this, "Error, config not saved", Toast.LENGTH_SHORT).show();
		}
		Toast.makeText(MainActivity.this, "config saved", Toast.LENGTH_SHORT).show();
		
		// run bochs app
		/*ComponentName cn = new ComponentName("net.sourceforge.bochs", "net.sourceforge.bochs.MainActivity");
		Intent intent = new Intent();
		intent.setComponent(cn);
		startActivity(intent);*/
	}

	public void applyTabStorage()
	{
		cbFloppyA = (CheckBox) findViewById(R.id.storageCheckBoxFloppyA);
		tvFloppyA = (TextView) findViewById(R.id.storageTextViewFloppyA);
		btBrowseFloppyA = (Button) findViewById(R.id.storageButtonFloppyA);
		cbFloppyB = (CheckBox) findViewById(R.id.storageCheckBoxFloppyB);
		tvFloppyB = (TextView) findViewById(R.id.storageTextViewFloppyB);
		btBrowseFloppyB = (Button) findViewById(R.id.storageButtonFloppyB);
		cbAta0m = (CheckBox) findViewById(R.id.storageCheckBoxAta0m);
		tvAta0m = (TextView) findViewById(R.id.storageTextViewAta0m);
		cbVvfatAta0m = (CheckBox) findViewById(R.id.storageCheckBoxAta0mVvfat);
		btBrowseAta0m = (Button) findViewById(R.id.storageButtonAta0m);
		cbAta0s = (CheckBox) findViewById(R.id.storageCheckBoxAta0s);
		tvAta0s = (TextView) findViewById(R.id.storageTextViewAta0s);
		cbVvfatAta0s = (CheckBox) findViewById(R.id.storageCheckBoxAta0sVvfat);
		btBrowseAta0s = (Button) findViewById(R.id.storageButtonAta0s);
		cbAta1m = (CheckBox) findViewById(R.id.storageCheckBoxAta1m);
		tvAta1m = (TextView) findViewById(R.id.storageTextViewAta1m);
		cbVvfatAta1m = (CheckBox) findViewById(R.id.storageCheckBoxAta1mVvfat);
		btBrowseAta1m = (Button) findViewById(R.id.storageButtonAta1m);
		cbAta1s = (CheckBox) findViewById(R.id.storageCheckBoxAta1s);
		tvAta1s = (TextView) findViewById(R.id.storageTextViewAta1s);
		cbVvfatAta1s = (CheckBox) findViewById(R.id.storageCheckBoxAta1sVvfat);
		btBrowseAta1s = (Button) findViewById(R.id.storageButtonAta1s);	
		cbFloppyA.setChecked(Config.floppyA);
		tvFloppyA.setText(getFileName(Config.floppyA_image));
		tvFloppyA.setEnabled(Config.floppyA);
		btBrowseFloppyA.setEnabled(Config.floppyA);
		cbFloppyB.setChecked(Config.floppyB);
		tvFloppyB.setText(getFileName(Config.floppyB_image));
		tvFloppyB.setEnabled(Config.floppyB);
		btBrowseFloppyB.setEnabled(Config.floppyB);
		cbAta0m.setChecked(Config.ata0m);
		tvAta0m.setText(getFileName(Config.ata0m_image));
		tvAta0m.setEnabled(Config.ata0m);
		cbVvfatAta0m.setEnabled(Config.ata0m);
		btBrowseAta0m.setEnabled(Config.ata0m);
		cbAta0s.setChecked(Config.ata0s);
		tvAta0s.setText(getFileName(Config.ata0s_image));
		tvAta0s.setEnabled(Config.ata0s);
		cbVvfatAta0s.setEnabled(Config.ata0s);
		btBrowseAta0s.setEnabled(Config.ata0s);
		cbAta1m.setChecked(Config.ata1m);
		tvAta1m.setText(getFileName(Config.ata1m_image));
		tvAta1m.setEnabled(Config.ata1m);
		cbVvfatAta1m.setEnabled(Config.ata1m);
		btBrowseAta1m.setEnabled(Config.ata1m);
		cbAta1s.setChecked(Config.ata1s);
		tvAta1s.setText(getFileName(Config.ata1s_image));
		tvAta1s.setEnabled(Config.ata1s);
		cbVvfatAta1s.setEnabled(Config.ata1s);
		btBrowseAta1s.setEnabled(Config.ata1s);
	}

	public void setupTabStorage()
	{
		cbFloppyA.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
				{
					Config.floppyA = cbFloppyA.isChecked();
					btBrowseFloppyA.setEnabled(Config.floppyA);
					tvFloppyA.setEnabled(Config.floppyA);
				}
			}
		);     
		cbFloppyB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
				{
					Config.floppyB = cbFloppyB.isChecked();
					btBrowseFloppyB.setEnabled(Config.floppyB);
					tvFloppyB.setEnabled(Config.floppyB);
				}
			}
		);
		cbAta0m.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
				{
					Config.ata0m = cbAta0m.isChecked();
					btBrowseAta0m.setEnabled(Config.ata0m);
					tvAta0m.setEnabled(Config.ata0m);
					spAta0mType.setEnabled(Config.ata0m);
					cbVvfatAta0m.setEnabled(Config.ata0m);
				}
			}
		);
		cbAta0s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
				{
					Config.ata0s = cbAta0s.isChecked();
					btBrowseAta0s.setEnabled(Config.ata0s);
					tvAta0s.setEnabled(Config.ata0s);
					spAta0sType.setEnabled(Config.ata0s);
					cbVvfatAta0s.setEnabled(Config.ata0s);
				}
			}
		);
		cbAta1m.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
				{
					Config.ata1m = cbAta1m.isChecked();
					btBrowseAta1m.setEnabled(Config.ata1m);
					tvAta1m.setEnabled(Config.ata1m);
					spAta1mType.setEnabled(Config.ata1m);
					cbVvfatAta1m.setEnabled(Config.ata1m);
				}
			}
		);
		cbAta1s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
				{
					Config.ata1s = cbAta1s.isChecked();
					btBrowseAta1s.setEnabled(Config.ata1s);
					tvAta1s.setEnabled(Config.ata1s);
					spAta1sType.setEnabled(Config.ata1s);
					cbVvfatAta1s.setEnabled(Config.ata1s);
				}
			}
		);
		setupSpinnersStorage();
	}

	public void setupSpinnersStorage()
	{
		String[] typeList = {"disk", "cdrom"};
		String[] bootList = {"disk", "cdrom"};
		spAta0mType = (Spinner) findViewById(R.id.storageSpinnerAta0m);
		spAta0sType = (Spinner) findViewById(R.id.storageSpinnerAta0s);
		spAta1mType = (Spinner) findViewById(R.id.storageSpinnerAta1m);
		spAta1sType = (Spinner) findViewById(R.id.storageSpinnerAta1s);
		spBoot = (Spinner) findViewById(R.id.storageSpinnerBoot);
		spAta0mType.setEnabled(Config.ata0m);
		spAta0sType.setEnabled(Config.ata0s);
		spAta1mType.setEnabled(Config.ata1m);
		spAta1sType.setEnabled(Config.ata1s);
		SpinnerAdapter adapterType = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, typeList);
		SpinnerAdapter adapterBoot = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, bootList);
		spAta0mType.setAdapter(adapterType);
		spAta0sType.setAdapter(adapterType);
		spAta1mType.setAdapter(adapterType);
		spAta1sType.setAdapter(adapterType);
		spBoot.setAdapter(adapterBoot);
	}

	public void browseFloppyA(View view)
	{
		FileChooser filechooser = new FileChooser(MainActivity.this);
		filechooser.setFileListener(new FileChooser.FileSelectedListener() {
				@Override
				public void fileSelected(final File file)
				{
					// ....do something with the file
					String filename = file.getAbsolutePath();
					Log.d("File", filename);
					tvFloppyA.setText(file.getName());
					Config.floppyA_image = filename;
					// then actually do something in another module

				}
			});
        // Set up and filter my extension I am looking for
		//filechooser.setExtension("img");
		filechooser.showDialog();
	}

	public void browseFloppyB(View view)
	{
		FileChooser filechooser = new FileChooser(MainActivity.this);
		filechooser.setFileListener(new FileChooser.FileSelectedListener() {
				@Override
				public void fileSelected(final File file)
				{
					// ....do something with the file
					String filename = file.getAbsolutePath();
					Log.d("File", filename);
					tvFloppyB.setText(file.getName());
					Config.floppyB_image = filename;
					// then actually do something in another module

				}
			});
        // Set up and filter my extension I am looking for
		//filechooser.setExtension("img");
		filechooser.showDialog();
	}

	public void browseAta0m(View view)
	{
		FileChooser filechooser = new FileChooser(MainActivity.this);
		filechooser.setFileListener(new FileChooser.FileSelectedListener() {
				@Override
				public void fileSelected(final File file)
				{
					// ....do something with the file
					String filename = file.getAbsolutePath();
					Log.d("File", filename);
					tvAta0m.setText(file.getName());
					Config.ata0m_image = filename;
					// then actually do something in another module

				}
			});
        // Set up and filter my extension I am looking for
		//filechooser.setExtension("img");
		filechooser.showDialog();
	}

	public void browseAta0s(View view)
	{
		FileChooser filechooser = new FileChooser(MainActivity.this);
		filechooser.setFileListener(new FileChooser.FileSelectedListener() {
				@Override
				public void fileSelected(final File file)
				{
					// ....do something with the file
					String filename = file.getAbsolutePath();
					Log.d("File", filename);
					tvAta0s.setText(file.getName());
					Config.ata0s_image = filename;
					// then actually do something in another module

				}
			});
        // Set up and filter my extension I am looking for
		//filechooser.setExtension("img");
		filechooser.showDialog();
	}

	public void browseAta1m(View view)
	{
		FileChooser filechooser = new FileChooser(MainActivity.this);
		filechooser.setFileListener(new FileChooser.FileSelectedListener() {
				@Override
				public void fileSelected(final File file)
				{
					// ....do something with the file
					String filename = file.getAbsolutePath();
					Log.d("File", filename);
					tvAta1m.setText(file.getName());
					Config.ata1m_image = filename;
					// then actually do something in another module

				}
			});
        // Set up and filter my extension I am looking for
		//filechooser.setExtension("img");
		filechooser.showDialog();
	}

	public void browseAta1s(View view)
	{
		FileChooser filechooser = new FileChooser(MainActivity.this);
		filechooser.setFileListener(new FileChooser.FileSelectedListener() {
				@Override
				public void fileSelected(final File file)
				{
					// ....do something with the file
					String filename = file.getAbsolutePath();
					Log.d("File", filename);
					tvAta1s.setText(file.getName());
					Config.ata1s_image = filename;
					// then actually do something in another module

				}
			});
        // Set up and filter my extension I am looking for
		//filechooser.setExtension("img");
		filechooser.showDialog();
	}
	
	public void browseRomImage(View view)
	{
		FileChooser filechooser = new FileChooser(MainActivity.this);
		filechooser.setFileListener(new FileChooser.FileSelectedListener() {
				@Override
				public void fileSelected(final File file)
				{
					// ....do something with the file
					String filename = file.getAbsolutePath();
					Log.d("File", filename);
					tvRomImage.setText(file.getName());
					Config.romImage = filename;
					// then actually do something in another module

				}
			});
        // Set up and filter my extension I am looking for
		//filechooser.setExtension("img");
		filechooser.showDialog();
	}
	
	public void browseVgaRomImage(View view)
	{
		FileChooser filechooser = new FileChooser(MainActivity.this);
		filechooser.setFileListener(new FileChooser.FileSelectedListener() {
				@Override
				public void fileSelected(final File file)
				{
					// ....do something with the file
					String filename = file.getAbsolutePath();
					Log.d("File", filename);
					tvVgaRomImage.setText(file.getName());
					Config.vgaRomImage = filename;
					// then actually do something in another module

				}
			});
        // Set up and filter my extension I am looking for
		//filechooser.setExtension("img");
		filechooser.showDialog();
	}

	public void applyTabMisc()
	{
		btRomImage = (Button) findViewById(R.id.miscButtonRomImage);
		btVgaRomImage = (Button) findViewById(R.id.miscButtonVgaRomImage);
		tvRomImage = (TextView) findViewById(R.id.miscTextViewRomImage);
		tvVgaRomImage = (TextView) findViewById(R.id.miscTextViewVgaRomImage);
		editMegs = (EditText) findViewById(R.id.miscEditText1);
		tvRomImage.setText(getFileName(Config.romImage));
		tvVgaRomImage.setText(getFileName(Config.vgaRomImage));
		editMegs.setText(String.valueOf(Config.megs));
	}

	public void setupTabMisc()
	{
		Config.megs = Integer.parseInt(editMegs.getText().toString());
	}
	
	private String getFileName(String path) {
		String result;
		if (path.contains("/")) {
			result = path.substring(path.lastIndexOf("/")+1, path.length());
		} else {
			result = path;
		}
		return result;
	}

}
