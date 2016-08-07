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
import android.text.*;
import java.util.*;
import android.widget.AdapterView.*;
import org.json.*;
import net.sourceforge.bochsui.entity.*;

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

	private Spinner spCpuModel;
	private TextView tvCpuDescription;
	private RadioButton rbI430fx;
	private RadioButton rbI440fx;
	private RadioButton rbBochsVbe;
	private RadioButton rbCirrus;
	private Spinner spSound;
	private Spinner spEthernet;

	private Spinner spSlot1;
	private Spinner spSlot2;
	private Spinner spSlot3;
	private Spinner spSlot4;
	private Spinner spSlot5;

    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    // Tab titles
    private String[] tabs = { "Storage", "Hardware", "Misc" };

	private String m_chosenDir = "";
	private boolean m_newFolderEnabled = true;

	private List<CpuModel> cpuModel = new ArrayList<CpuModel>();

	private enum Requestor
	{ATA0_MASTER, ATA0_SLAVE, ATA1_MASTER, ATA1_SLAVE, FLOPPY_A, FLOPPY_B, ROM, VGAROM}
	private Requestor requestor;

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
						setupTabStorage();
					else if (arg0 == 1)
						setupTabHardware();
					else if (arg0 == 2)
						setupTabMisc();
				}

				@Override
				public void onPageScrollStateChanged(int arg0)
				{
				}
			});

    }

	public boolean onCreateOptionsMenu(Menu menu)
	{
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

	public boolean onOptionsItemSelected(MenuItem item)
	{
        int id = item.getItemId();
        if (id == R.id.start)
		{
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

	public void setupTabStorage()
	{
		final List typeList = Arrays.asList("disk", "cdrom");
		final List bootList = Arrays.asList("disk", "cdrom", "floppy");
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
		spAta0mType.setSelection(typeList.indexOf(Config.ata0mType));
		spAta0sType.setSelection(typeList.indexOf(Config.ata0sType));
		spAta1mType.setSelection(typeList.indexOf(Config.ata1mType));
		spAta1sType.setSelection(typeList.indexOf(Config.ata1sType));
		spBoot.setSelection(bootList.indexOf(Config.boot));


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

		spAta0mType.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4)
				{
					Config.ata0mType = (String) typeList.get(p3);
				}

				@Override
				public void onNothingSelected(AdapterView<?> p1)
				{
					// TODO: Implement this method
				}
			});

		spAta0sType.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4)
				{
					Config.ata0sType = (String) typeList.get(p3);
				}

				@Override
				public void onNothingSelected(AdapterView<?> p1)
				{
					// TODO: Implement this method
				}
			});

		spAta1mType.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4)
				{
					Config.ata1mType = (String) typeList.get(p3);
				}

				@Override
				public void onNothingSelected(AdapterView<?> p1)
				{
					// TODO: Implement this method
				}
			});

		spAta1sType.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4)
				{
					Config.ata1sType = (String) typeList.get(p3);
				}

				@Override
				public void onNothingSelected(AdapterView<?> p1)
				{
					// TODO: Implement this method
				}
			});

		spBoot.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4)
				{
					Config.boot = (String) bootList.get(p3);
				}

				@Override
				public void onNothingSelected(AdapterView<?> p1)
				{
					// TODO: Implement this method
				}
			});
	}

	public void browseFloppyA(View view)
	{
		fileSelection(requestor.FLOPPY_A);
	}

	public void browseFloppyB(View view)
	{
		fileSelection(requestor.FLOPPY_B);
	}

	public void browseAta0m(View view)
	{
		if (cbVvfatAta0m.isChecked())
			dirSelection(requestor.ATA0_MASTER);
		else
			fileSelection(requestor.ATA0_MASTER);
	}

	public void browseAta0s(View view)
	{
		if (cbVvfatAta0s.isChecked())
			dirSelection(requestor.ATA0_SLAVE);
		else
			fileSelection(requestor.ATA0_SLAVE);
	}

	public void browseAta1m(View view)
	{
		if (cbVvfatAta1m.isChecked())
			dirSelection(requestor.ATA1_MASTER);
		else
			fileSelection(requestor.ATA1_MASTER);
	}

	public void browseAta1s(View view)
	{
		if (cbVvfatAta1s.isChecked())
			dirSelection(requestor.ATA1_SLAVE);
		else
			fileSelection(requestor.ATA1_SLAVE);
	}

	public void browseRomImage(View view)
	{
		fileSelection(requestor.ROM);
	}

	public void browseVgaRomImage(View view)
	{
		fileSelection(requestor.VGAROM);
	}

	public void setupTabMisc()
	{
		btRomImage = (Button) findViewById(R.id.miscButtonRomImage);
		btVgaRomImage = (Button) findViewById(R.id.miscButtonVgaRomImage);
		tvRomImage = (TextView) findViewById(R.id.miscTextViewRomImage);
		tvVgaRomImage = (TextView) findViewById(R.id.miscTextViewVgaRomImage);
		editMegs = (EditText) findViewById(R.id.miscEditText1);
		tvRomImage.setText(getFileName(Config.romImage));
		tvVgaRomImage.setText(getFileName(Config.vgaRomImage));
		editMegs.setText(String.valueOf(Config.megs));

		editMegs.addTextChangedListener(new TextWatcher() {

				@Override
				public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4)
				{
					// TODO: Implement this method
				}

				@Override
				public void onTextChanged(CharSequence p1, int p2, int p3, int p4)
				{
					// TODO: Implement this method
					//Config.megs = Integer.parseInt(editMegs.getText().toString());
				}

				@Override
				public void afterTextChanged(Editable p1)
				{
					// TODO: Implement this method
					if (!p1.toString().equals(""))
					{
						Config.megs = Integer.parseInt(editMegs.getText().toString());
					}
				}

			});
	}

	private void dirSelection(final Requestor num)
	{
		// Create DirectoryChooserDialog and register a callback 
		DirectoryChooserDialog directoryChooserDialog = 
			new DirectoryChooserDialog(MainActivity.this, 
			new DirectoryChooserDialog.ChosenDirectoryListener() 
			{
				@Override
				public void onChosenDir(String chosenDir) 
				{
					m_chosenDir = chosenDir;
					switch (num)
					{
						case ATA0_MASTER:
							tvAta0m.setText(chosenDir);
							Config.ata0m_image = chosenDir;
							Config.ata0mMode = "vvfat";
							break;
						case ATA0_SLAVE:
							tvAta0s.setText(chosenDir);
							Config.ata0s_image = chosenDir;
							Config.ata0sMode = "vvfat";
							break;
						case ATA1_MASTER:
							tvAta1m.setText(chosenDir);
							Config.ata1m_image = chosenDir;
							Config.ata1mMode = "vvfat";
							break;
						case ATA1_SLAVE:
							tvAta1s.setText(chosenDir);
							Config.ata1s_image = chosenDir;
							Config.ata1sMode = "vvfat";
							break;
					}
				}
			}); 
		// Toggle new folder button enabling
		directoryChooserDialog.setNewFolderEnabled(m_newFolderEnabled);
		// Load directory chooser dialog for initial 'm_chosenDir' directory.
		// The registered callback will be called upon final directory selection.
		directoryChooserDialog.chooseDirectory(m_chosenDir);
		m_newFolderEnabled = ! m_newFolderEnabled;
	}

	private void fileSelection(final Requestor num)
	{
		FileChooser filechooser = new FileChooser(MainActivity.this);
		filechooser.setFileListener(new FileChooser.FileSelectedListener() {
				@Override
				public void fileSelected(final File file)
				{
					String filename = file.getAbsolutePath();
					Log.d("File", filename);
					switch (num)
					{
						case ATA0_MASTER:
							tvAta0m.setText(file.getName());
							Config.ata0m_image = filename;
							Config.ata0mMode = getMode(file.getName());
							break;
						case ATA0_SLAVE:
							tvAta0s.setText(file.getName());
							Config.ata0s_image = filename;
							Config.ata0sMode = getMode(file.getName());
							break;
						case ATA1_MASTER:
							tvAta1m.setText(file.getName());
							Config.ata1m_image = filename;
							Config.ata1mMode = getMode(file.getName());
							break;
						case ATA1_SLAVE:
							tvAta1s.setText(file.getName());
							Config.ata1s_image = filename;
							Config.ata1sMode = getMode(file.getName());
							break;
						case FLOPPY_A:
							tvFloppyA.setText(file.getName());
							Config.floppyA_image = filename;
							break;
						case FLOPPY_B:
							tvFloppyB.setText(file.getName());
							Config.floppyB_image = filename;
							break;
						case ROM:
							tvRomImage.setText(file.getName());
							Config.romImage = filename;
							break;
						case VGAROM:
							tvVgaRomImage.setText(file.getName());
					        Config.vgaRomImage = filename;
							break;
					}

				}
			});
        // Set up and filter my extension I am looking for
		//filechooser.setExtension("img");
		filechooser.showDialog();
	}

	private String getFileName(String path)
	{
		String result;
		if (path.contains("/"))
			result = path.substring(path.lastIndexOf("/") + 1, path.length());
		else
			result = path;
		return result;
	}

	private String getMode(String str)
	{
		String result="";
		if (str.endsWith(".vmdk"))
			result = "vmware4";
		else if (str.endsWith(".vhd"))
			result = "vpc";
		else if (str.endsWith(".vdi"))
			result = "vbox";
		return result;
	}

	private void readCpuList()
	{
		Scanner sc = new Scanner(getResources().openRawResource(R.raw.data_json)).useDelimiter("[\n]");
		StringBuilder sb = new StringBuilder();
		while (sc.hasNext())
		{
			sb.append(sc.next() + "\n");
		}
		sc.close();

		JSONObject dataJsonObj = null;
		try
		{
			dataJsonObj = new JSONObject(sb.toString());
			JSONArray cpulist = dataJsonObj.getJSONArray("cpulist");
			for (int i=0; i < cpulist.length() - 1; i++)
			{
				JSONObject model = cpulist.getJSONObject(i);
				String value = model.getString("value");
				String description = model.getString("description");
				String required = model.getString("required");
				cpuModel.add(new CpuModel(value, description, required));
			}
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
	}

	private List<String> getCpuModelValues()
	{
		List<String> result = new ArrayList<String>();
		for (CpuModel cm : cpuModel)
		{
			result.add(cm.getValue());
		}
		return result;
	}

	private void setupTabHardware()
	{
		final List slotList = Arrays.asList("none", "cirrus", "voodoo", "ne2k", "sb16", "es1370");
		final List soundList = Arrays.asList("none", "Creative SB16", "Ensoniq ES1370");
		final List ethernetList = Arrays.asList("none", "Novell NE2000", "Realtek RTL8029", "Intel 82540EM");
		
		if (cpuModel.size() == 0)
			readCpuList();

		spCpuModel = (Spinner) findViewById(R.id.hardwareSpinnerCpuModel);
		tvCpuDescription = (TextView) findViewById(R.id.hardwareTextViewCpuDesc);
		rbI430fx = (RadioButton) findViewById(R.id.hardwareRadioButtonI430fx);
		rbI440fx = (RadioButton) findViewById(R.id.hardwareRadioButtonI440fx);
		rbBochsVbe = (RadioButton) findViewById(R.id.hardwareRadioButtonBochsVbe);
		rbCirrus = (RadioButton) findViewById(R.id.hardwareRadioButtonCirrusLogic);
		spSound = (Spinner) findViewById(R.id.hardwareSpinnerSound);
		spEthernet = (Spinner) findViewById(R.id.hardwareSpinnerEthernet);
		spSlot1 = (Spinner) findViewById(R.id.hardwareSpinnerSlot1);
		spSlot2 = (Spinner) findViewById(R.id.hardwareSpinnerSlot2);
		spSlot3 = (Spinner) findViewById(R.id.hardwareSpinnerSlot3);
		spSlot4 = (Spinner) findViewById(R.id.hardwareSpinnerSlot4);
		spSlot5 = (Spinner) findViewById(R.id.hardwareSpinnerSlot5);
		SpinnerAdapter cpuModelAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, getCpuModelValues());
		SpinnerAdapter slotAdapter = new ArrayAdapter<String>(this, R.layout.spinner_row, slotList);
		SpinnerAdapter soundAdapter = new ArrayAdapter<String>(this, R.layout.spinner_row, soundList);
		SpinnerAdapter ethernetAdapter = new ArrayAdapter<String>(this, R.layout.spinner_row, ethernetList);
		spCpuModel.setAdapter(cpuModelAdapter);
		spSound.setAdapter(soundAdapter);
		spEthernet.setAdapter(ethernetAdapter);
		spSlot1.setAdapter(slotAdapter);
		spSlot2.setAdapter(slotAdapter);
		spSlot3.setAdapter(slotAdapter);
		spSlot4.setAdapter(slotAdapter);
		spSlot5.setAdapter(slotAdapter);
		int selectedCpuModel = getCpuModelValues().indexOf(Config.cpuModel);
		spCpuModel.setSelection(selectedCpuModel);
		tvCpuDescription.setText(cpuModel.get(selectedCpuModel).getDescription());
		rbI430fx.setChecked(Config.chipset.equals("i430fx"));
		rbI440fx.setChecked(Config.chipset.equals("i440fx"));
		rbBochsVbe.setChecked(Config.vgaExtension.equals("vbe"));
		rbCirrus.setChecked(Config.vgaExtension.equals("cirrus"));
		int selectedSlot1 = slotList.indexOf(Config.slot1);
		int selectedSlot2 = slotList.indexOf(Config.slot2);
		int selectedSlot3 = slotList.indexOf(Config.slot3);
		int selectedSlot4 = slotList.indexOf(Config.slot4);
		int selectedSlot5 = slotList.indexOf(Config.slot5);
		spSlot1.setSelection((selectedSlot1 == -1) ? 0 : selectedSlot1);
		spSlot2.setSelection((selectedSlot2 == -1) ? 0 : selectedSlot2);
		spSlot3.setSelection((selectedSlot3 == -1) ? 0 : selectedSlot3);
		spSlot4.setSelection((selectedSlot4 == -1) ? 0 : selectedSlot4);
		spSlot5.setSelection((selectedSlot5 == -1) ? 0 : selectedSlot5);

		spCpuModel.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4)
				{
					Config.cpuModel = getCpuModelValues().get(p3);
					int num = getCpuModelValues().indexOf(Config.cpuModel);
					tvCpuDescription.setText(cpuModel.get(num).getDescription());
				}

				@Override
				public void onNothingSelected(AdapterView<?> p1)
				{
					// TODO: Implement this method
				}
			});

		spSlot1.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4)
				{
					Config.slot1 = (p3 == 0) ? "" : (String) slotList.get(p3);
				}

				@Override
				public void onNothingSelected(AdapterView<?> p1)
				{
					// TODO: Implement this method
				}
			});

		spSlot2.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4)
				{
					Config.slot2 = (p3 == 0) ? "" : (String) slotList.get(p3);
				}

				@Override
				public void onNothingSelected(AdapterView<?> p1)
				{
					// TODO: Implement this method
				}
			});

		spSlot3.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4)
				{
					Config.slot3 = (p3 == 0) ? "" : (String) slotList.get(p3);
				}

				@Override
				public void onNothingSelected(AdapterView<?> p1)
				{
					// TODO: Implement this method
				}
			});

		spSlot4.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4)
				{
					Config.slot4 = (p3 == 0) ? "" : (String) slotList.get(p3);
				}

				@Override
				public void onNothingSelected(AdapterView<?> p1)
				{
					// TODO: Implement this method
				}
			});

		spSlot5.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4)
				{
					Config.slot5 = (p3 == 0) ? "" : (String) slotList.get(p3);
				}

				@Override
				public void onNothingSelected(AdapterView<?> p1)
				{
					// TODO: Implement this method
				}
			});
	}

	public void onClickI430fx(View view)
	{
		Config.chipset = "i430fx";
	}

	public void onClickI440fx(View view)
	{
		Config.chipset = "i440fx";
	}
	
	public void onClickBochsVbe(View view)
	{
	}
	
	public void onClickCirrusLogic(View view)
	{
	}

}
