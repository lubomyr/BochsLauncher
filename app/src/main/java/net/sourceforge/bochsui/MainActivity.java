package net.sourceforge.bochsui;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import net.sourceforge.bochsui.adapter.TabsPagerAdapter;
import net.sourceforge.bochsui.entity.CpuModel;
import net.sourceforge.bochsui.entity.EthernetCard;
import net.sourceforge.bochsui.entity.SoundCard;
import net.sourceforge.bochsui.entity.VgaCard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {
	public static String path;
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
    private CheckBox cbFullscreen;
    private Spinner spClockSync;
    private SeekBar sbVgaUpdateFreq;
    private TextView tvVgaUpdateFreq;

    private Spinner spCpuModel;
    private TextView tvCpuDescription;
    private RadioButton rbI430fx;
    private RadioButton rbI440fx;
    private Spinner spVga;
    private TextView tvVgaDescription;
    private Spinner spSound;
    private TextView tvSoundDescription;
    private Spinner spEthernet;
    private TextView tvEthernetDescription;
    private SeekBar sbMemory;
    private TextView tvMemory;
    private Spinner[] spSlot = new Spinner[5];

    private ArrayAdapter<String> slotAdapter;

    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    // Tab titles
    private String[] tabs = {"Storage", "Hardware", "Misc"};

    private String m_chosenDir = "";
    private boolean m_newFolderEnabled = true;

    private List<CpuModel> cpuModel = new ArrayList<CpuModel>();
    private List<VgaCard> vgaCard = new ArrayList<VgaCard>();
    private List<SoundCard> soundCard = new ArrayList<SoundCard>();
    private List<EthernetCard> ethernetCard = new ArrayList<EthernetCard>();

    private enum Requestor {ATA0_MASTER, ATA0_SLAVE, ATA1_MASTER, ATA1_SLAVE, FLOPPY_A, FLOPPY_B, ROM, VGAROM}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Android/data/net.sourceforge.bochs/files/bochsrc.txt";

        if (!Config.configLoaded) {
            try {
                Config.readConfig();

            } catch (FileNotFoundException e) {
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
        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }

        /**
         * on swiping the viewpager make respective tab selected
         * */
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                if (arg0 == 0)
                    setupTabStorage();
                else if (arg0 == 1)
                    setupTabHardware();
                else if (arg0 == 2)
                    setupTabMisc();
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
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
            save();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        // on tab selected
        // show respected fragment view
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    }

    public void save() {
        try {
            Config.writeConfig();
        } catch (IOException e) {
            Toast.makeText(MainActivity.this, "Error, config not saved", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(MainActivity.this, "config saved", Toast.LENGTH_SHORT).show();

        // run bochs app
        ComponentName cn = new ComponentName("net.sourceforge.bochs", "net.sourceforge.bochs.MainActivity");
         Intent intent = new Intent();
		 intent.setComponent(cn);
		 startActivity(intent);
    }

    public void setupTabStorage() {
        final List<String> typeList = Arrays.asList("disk", "cdrom");
        final List<String> bootList = Arrays.asList("disk", "cdrom", "floppy");
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
        SpinnerAdapter adapterType = new ArrayAdapter<String>(this, R.layout.spinner_row, typeList);
        SpinnerAdapter adapterBoot = new ArrayAdapter<String>(this, R.layout.spinner_row, bootList);
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
                                                 public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                     Config.floppyA = cbFloppyA.isChecked();
                                                     btBrowseFloppyA.setEnabled(Config.floppyA);
                                                     tvFloppyA.setEnabled(Config.floppyA);
                                                 }
                                             }
        );
        cbFloppyB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                 @Override
                                                 public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                     Config.floppyB = cbFloppyB.isChecked();
                                                     btBrowseFloppyB.setEnabled(Config.floppyB);
                                                     tvFloppyB.setEnabled(Config.floppyB);
                                                 }
                                             }
        );
        cbAta0m.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                               @Override
                                               public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
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
                                               public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
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
                                               public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
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
                                               public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
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
            public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4) {
                Config.ata0mType = typeList.get(p3);
            }

            @Override
            public void onNothingSelected(AdapterView<?> p1) {
                // TODO: Implement this method
            }
        });

        spAta0sType.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4) {
                Config.ata0sType = typeList.get(p3);
            }

            @Override
            public void onNothingSelected(AdapterView<?> p1) {
                // TODO: Implement this method
            }
        });

        spAta1mType.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4) {
                Config.ata1mType = typeList.get(p3);
            }

            @Override
            public void onNothingSelected(AdapterView<?> p1) {
                // TODO: Implement this method
            }
        });

        spAta1sType.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4) {
                Config.ata1sType = typeList.get(p3);
            }

            @Override
            public void onNothingSelected(AdapterView<?> p1) {
                // TODO: Implement this method
            }
        });

        spBoot.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4) {
                Config.boot = bootList.get(p3);
            }

            @Override
            public void onNothingSelected(AdapterView<?> p1) {
                // TODO: Implement this method
            }
        });
    }

    public void browseFloppyA(View view) {
        fileSelection(Requestor.FLOPPY_A);
    }

    public void browseFloppyB(View view) {
        fileSelection(Requestor.FLOPPY_B);
    }

    public void browseAta0m(View view) {
        if (cbVvfatAta0m.isChecked())
            dirSelection(Requestor.ATA0_MASTER);
        else
            fileSelection(Requestor.ATA0_MASTER);
    }

    public void browseAta0s(View view) {
        if (cbVvfatAta0s.isChecked())
            dirSelection(Requestor.ATA0_SLAVE);
        else
            fileSelection(Requestor.ATA0_SLAVE);
    }

    public void browseAta1m(View view) {
        if (cbVvfatAta1m.isChecked())
            dirSelection(Requestor.ATA1_MASTER);
        else
            fileSelection(Requestor.ATA1_MASTER);
    }

    public void browseAta1s(View view) {
        if (cbVvfatAta1s.isChecked())
            dirSelection(Requestor.ATA1_SLAVE);
        else
            fileSelection(Requestor.ATA1_SLAVE);
    }

    public void browseRomImage(View view) {
        fileSelection(Requestor.ROM);
    }

    public void browseVgaRomImage(View view) {
        fileSelection(Requestor.VGAROM);
    }

    public void setupTabMisc() {
        final List<String> syncList = Arrays.asList("none", "slowdown", "realtime", "both");
		final int minValueVgaUpdateFreq = 5;
        btRomImage = (Button) findViewById(R.id.miscButtonRomImage);
        btVgaRomImage = (Button) findViewById(R.id.miscButtonVgaRomImage);
        tvRomImage = (TextView) findViewById(R.id.miscTextViewRomImage);
        tvVgaRomImage = (TextView) findViewById(R.id.miscTextViewVgaRomImage);
        cbFullscreen = (CheckBox) findViewById(R.id.miscCheckBoxFullscreen);
        spClockSync = (Spinner) findViewById(R.id.miscSpinnerClockSync);
        sbVgaUpdateFreq = (SeekBar) findViewById(R.id.miscSeekBarVgaUpdateFreq);
        tvVgaUpdateFreq = (TextView) findViewById(R.id.miscTextViewVgaUpdateFreq);
        SpinnerAdapter syncAdapter = new ArrayAdapter<String>(this, R.layout.spinner_row, syncList);
        spClockSync.setAdapter(syncAdapter);
        tvRomImage.setText(getFileName(Config.romImage));
        tvVgaRomImage.setText(getFileName(Config.vgaRomImage));
        cbFullscreen.setChecked(Config.fullscreen);
        spClockSync.setSelection(syncList.indexOf(Config.clockSync));
        sbVgaUpdateFreq.setProgress(Config.vgaUpdateFreq - minValueVgaUpdateFreq);
        tvVgaUpdateFreq.setText(String.valueOf(Config.vgaUpdateFreq));

        cbFullscreen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                    @Override
                                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                        Config.fullscreen = cbFullscreen.isChecked();
                                                    }
                                                }
        );

        spClockSync.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4) {
                Config.clockSync = syncList.get(p3);
            }

            @Override
            public void onNothingSelected(AdapterView<?> p1) {
                // TODO: Implement this method
            }
        });

        sbVgaUpdateFreq.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar p1, int p2, boolean p3) {
                tvVgaUpdateFreq.setText(String.valueOf(minValueVgaUpdateFreq + sbVgaUpdateFreq.getProgress()));
                Config.vgaUpdateFreq = minValueVgaUpdateFreq + sbVgaUpdateFreq.getProgress();
            }

            @Override
            public void onStartTrackingTouch(SeekBar p1) {
                // TODO: Implement this method
            }

            @Override
            public void onStopTrackingTouch(SeekBar p1) {
                // TODO: Implement this method
            }
        });
    }

    private void dirSelection(final Requestor num) {
        // Create DirectoryChooserDialog and register a callback
        DirectoryChooserDialog directoryChooserDialog =
                new DirectoryChooserDialog(MainActivity.this,
                        new DirectoryChooserDialog.ChosenDirectoryListener() {
                            @Override
                            public void onChosenDir(String chosenDir) {
                                m_chosenDir = chosenDir;
                                switch (num) {
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
        m_newFolderEnabled = !m_newFolderEnabled;
    }

    private void fileSelection(final Requestor num) {
        FileChooser filechooser = new FileChooser(MainActivity.this);
        filechooser.setFileListener(new FileChooser.FileSelectedListener() {
            @Override
            public void fileSelected(final File file) {
                String filename = file.getAbsolutePath();
                Log.d("File", filename);
                switch (num) {
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

    private String getFileName(String path) {
        String result;
        if (path.contains("/"))
            result = path.substring(path.lastIndexOf("/") + 1, path.length());
        else
            result = path;
        return result;
    }

    private String getMode(String str) {
        String result = "";
        if (str.endsWith(".vmdk"))
            result = "vmware4";
        else if (str.endsWith(".vhd"))
            result = "vpc";
        else if (str.endsWith(".vdi"))
            result = "vbox";
        return result;
    }

    private void readCpuList() {
        Scanner sc = new Scanner(getResources().openRawResource(R.raw.data_json)).useDelimiter("[\n]");
        StringBuilder sb = new StringBuilder();
        while (sc.hasNext()) {
            sb.append(sc.next() + "\n");
        }
        sc.close();

        JSONObject dataJsonObj = null;
        try {
            dataJsonObj = new JSONObject(sb.toString());
            JSONArray cpulist = dataJsonObj.getJSONArray("cpulist");
            for (int i = 0; i < cpulist.length(); i++) {
                JSONObject model = cpulist.getJSONObject(i);
                String value = model.getString("value");
                String description = model.getString("description");
                String required = model.getString("required");
                cpuModel.add(new CpuModel(value, description, required));
            }
            JSONArray vgalist = dataJsonObj.getJSONArray("vgalist");
            for (int i = 0; i < vgalist.length(); i++) {
                JSONObject model = vgalist.getJSONObject(i);
                String value = model.getString("value");
                String description = model.getString("description");
                vgaCard.add(new VgaCard(value, description));
            }
            JSONArray soundlist = dataJsonObj.getJSONArray("soundlist");
            for (int i = 0; i < soundlist.length(); i++) {
                JSONObject model = soundlist.getJSONObject(i);
                String value = model.getString("value");
                String description = model.getString("description");
                soundCard.add(new SoundCard(value, description));
            }
            JSONArray ethernetlist = dataJsonObj.getJSONArray("ethernetlist");
            for (int i = 0; i < ethernetlist.length(); i++) {
                JSONObject model = ethernetlist.getJSONObject(i);
                String value = model.getString("value");
                String description = model.getString("description");
                ethernetCard.add(new EthernetCard(value, description));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private List<String> getCpuModelValues() {
        List<String> result = new ArrayList<String>();
        for (CpuModel cm : cpuModel) {
            result.add(cm.getValue());
        }
        return result;
    }

    private List<String> getVgaCardValues() {
        List<String> result = new ArrayList<String>();
        for (VgaCard vc : vgaCard) {
            result.add(vc.getValue());
        }
        return result;
    }

    private List<String> getSoundCardValues() {
        List<String> result = new ArrayList<String>();
        for (SoundCard sc : soundCard) {
            result.add(sc.getValue());
        }
        return result;
    }

    private List<String> getEthernetCardValues() {
        List<String> result = new ArrayList<String>();
        for (EthernetCard ec : ethernetCard) {
            result.add(ec.getValue());
        }
        return result;
    }

    private void setupTabHardware() {
        final List<String> slotList = Arrays.asList("none", "pcivga", "cirrus", "voodoo", "es1370", "ne2k", "e1000");
		final int minValueMemory = 4;

        if (cpuModel.size() == 0)
            readCpuList();

        spCpuModel = (Spinner) findViewById(R.id.hardwareSpinnerCpuModel);
        tvCpuDescription = (TextView) findViewById(R.id.hardwareTextViewCpuDesc);
        rbI430fx = (RadioButton) findViewById(R.id.hardwareRadioButtonI430fx);
        rbI440fx = (RadioButton) findViewById(R.id.hardwareRadioButtonI440fx);
        spVga = (Spinner) findViewById(R.id.hardwareSpinnerVga);
        tvVgaDescription = (TextView) findViewById(R.id.hardwareTextViewVgaDesc);
        spSound = (Spinner) findViewById(R.id.hardwareSpinnerSound);
        tvSoundDescription = (TextView) findViewById(R.id.hardwareTextViewSoundDesc);
        spEthernet = (Spinner) findViewById(R.id.hardwareSpinnerEthernet);
        tvEthernetDescription = (TextView) findViewById(R.id.hardwareTextViewEthernetDesc);
        sbMemory = (SeekBar) findViewById(R.id.hardwareSeekBarMemory);
        tvMemory = (TextView) findViewById(R.id.hardwareTextViewMemory);
        spSlot[0] = (Spinner) findViewById(R.id.hardwareSpinnerSlot1);
        spSlot[1] = (Spinner) findViewById(R.id.hardwareSpinnerSlot2);
        spSlot[2] = (Spinner) findViewById(R.id.hardwareSpinnerSlot3);
        spSlot[3] = (Spinner) findViewById(R.id.hardwareSpinnerSlot4);
        spSlot[4] = (Spinner) findViewById(R.id.hardwareSpinnerSlot5);
        SpinnerAdapter cpuModelAdapter = new ArrayAdapter<String>(this, R.layout.spinner_row, getCpuModelValues());
        slotAdapter = new ArrayAdapter<String>(this, R.layout.spinner_row, slotList);
        SpinnerAdapter vgaAdapter = new ArrayAdapter<String>(this, R.layout.spinner_row, getVgaCardValues());
        SpinnerAdapter soundAdapter = new ArrayAdapter<String>(this, R.layout.spinner_row, getSoundCardValues());
        SpinnerAdapter ethernetAdapter = new ArrayAdapter<String>(this, R.layout.spinner_row, getEthernetCardValues());
        spCpuModel.setAdapter(cpuModelAdapter);
        spVga.setAdapter(vgaAdapter);
        spSound.setAdapter(soundAdapter);
        spEthernet.setAdapter(ethernetAdapter);
        for (Spinner aSpSlot : spSlot) {
            aSpSlot.setAdapter(slotAdapter);
        }
        int selectedCpuModel = getCpuModelValues().indexOf(Config.cpuModel);
        spCpuModel.setSelection(selectedCpuModel);
        tvCpuDescription.setText(cpuModel.get(selectedCpuModel).getDescription());
        rbI430fx.setChecked(Config.chipset.equals("i430fx"));
        rbI440fx.setChecked(Config.chipset.equals("i440fx"));
        sbMemory.setProgress(Config.megs - minValueMemory);
        tvMemory.setText(Config.megs + " mb");
        Integer[] selectedSlot = new Integer[5];
        for (int i = 0; i < spSlot.length; i++) {
            selectedSlot[i] = slotList.indexOf(Config.slot[i]);
            spSlot[i].setSelection((selectedSlot[i] == -1) ? 0 : selectedSlot[i]);
        }
        checkVga();
        checkSound();
        checkEthernet();

        spCpuModel.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4) {
                Config.cpuModel = getCpuModelValues().get(p3);
                int num = getCpuModelValues().indexOf(Config.cpuModel);
                tvCpuDescription.setText(cpuModel.get(num).getDescription());
            }

            @Override
            public void onNothingSelected(AdapterView<?> p1) {
                // TODO: Implement this method
            }
        });

        spVga.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4) {
                tvVgaDescription.setText(vgaCard.get(p3).getDescription());
                switch (p3) {
                    case 0:
                        Config.vgaExtension = "vbe";
                        Config.vgaRomImage = "VGABIOS-lgpl-latest";
                        setFreePciSlot("pcivga");
                        setFreePciSlot("cirrus");
                        break;
                    case 1:
                        Config.vgaExtension = "vbe";
                        Config.vgaRomImage = "VGABIOS-lgpl-latest";
                        setFreePciSlot("cirrus");
                        spSlot[0].setSelection(slotList.indexOf("pcivga"));
                        slotAdapter.notifyDataSetChanged();
                        break;
                    case 2:
                        Config.vgaExtension = "cirrus";
                        Config.vgaRomImage = "VGABIOS-lgpl-latest-cirrus";
                        setFreePciSlot("pcivga");
                        setFreePciSlot("cirrus");
                        break;
                    case 3:
                        Config.vgaExtension = "cirrus";
                        Config.vgaRomImage = "VGABIOS-lgpl-latest-cirrus";
                        setFreePciSlot("pcivga");
                        spSlot[0].setSelection(slotList.indexOf("cirrus"));
                        slotAdapter.notifyDataSetChanged();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> p1) {
                // TODO: Implement this method
            }
        });

        spSound.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4) {
                tvSoundDescription.setText(soundCard.get(p3).getDescription());
                switch (p3) {
                    case 0:
                        Config.useSb16 = false;
                        Config.useEs1370 = false;
                        setFreePciSlot("es1370");
                        break;
                    case 1:
                        Config.useSb16 = true;
                        Config.useEs1370 = false;
                        setFreePciSlot("es1370");
                        break;
                    case 2:
                        Config.useSb16 = false;
                        Config.useEs1370 = true;
                        spSlot[2].setSelection(slotList.indexOf("es1370"));
                        slotAdapter.notifyDataSetChanged();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> p1) {
                // TODO: Implement this method
            }
        });

        spEthernet.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4) {
                tvEthernetDescription.setText(ethernetCard.get(p3).getDescription());
                switch (p3) {
                    case 0:
                        Config.useNe2000 = false;
                        Config.useRtl8029 = false;
                        Config.useE1000 = false;
                        setFreePciSlot("ne2k");
                        setFreePciSlot("e1000");
                        break;
                    case 1:
                        Config.useNe2000 = true;
                        Config.useRtl8029 = false;
                        Config.useE1000 = false;
                        setFreePciSlot("ne2k");
                        setFreePciSlot("e1000");
                        break;
                    case 2:
                        Config.useNe2000 = false;
                        Config.useRtl8029 = true;
                        Config.useE1000 = false;
                        setFreePciSlot("e1000");
                        spSlot[1].setSelection(slotList.indexOf("ne2k"));
                        slotAdapter.notifyDataSetChanged();
                        break;
                    case 3:
                        Config.useNe2000 = false;
                        Config.useRtl8029 = false;
                        Config.useE1000 = true;
                        setFreePciSlot("ne2k");
                        spSlot[1].setSelection(slotList.indexOf("e1000"));
                        slotAdapter.notifyDataSetChanged();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> p1) {
                // TODO: Implement this method
            }
        });

        spSlot[0].setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4) {
                String str = slotList.get(p3);
                Config.slot[0] = (p3 == 0) ? "" : str;
                setOnInConfig(str);
            }

            @Override
            public void onNothingSelected(AdapterView<?> p1) {
                // TODO: Implement this method
            }
        });

        spSlot[1].setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4) {
                String str = slotList.get(p3);
                Config.slot[1] = (p3 == 0) ? "" : str;
                setOnInConfig(str);
            }

            @Override
            public void onNothingSelected(AdapterView<?> p1) {
                // TODO: Implement this method
            }
        });

        spSlot[2].setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4) {
                String str = slotList.get(p3);
                Config.slot[2] = (p3 == 0) ? "" : str;
                setOnInConfig(str);
            }

            @Override
            public void onNothingSelected(AdapterView<?> p1) {
                // TODO: Implement this method
            }
        });

        spSlot[3].setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4) {
                String str = slotList.get(p3);
                Config.slot[3] = (p3 == 0) ? "" : str;
                setOnInConfig(str);
            }

            @Override
            public void onNothingSelected(AdapterView<?> p1) {
                // TODO: Implement this method
            }
        });

        spSlot[4].setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4) {
                String str = slotList.get(p3);
                Config.slot[4] = (p3 == 0) ? "" : str;
                setOnInConfig(str);
            }

            @Override
            public void onNothingSelected(AdapterView<?> p1) {
                // TODO: Implement this method
            }
        });

        sbMemory.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar p1, int p2, boolean p3) {
                tvMemory.setText(String.valueOf(minValueMemory + sbMemory.getProgress()) + " mb");
                Config.megs = minValueMemory + sbMemory.getProgress();
            }

            @Override
            public void onStartTrackingTouch(SeekBar p1) {
                // TODO: Implement this method
            }

            @Override
            public void onStopTrackingTouch(SeekBar p1) {
                // TODO: Implement this method
            }
        });
    }

    public void onClickI430fx(View view) {
        Config.chipset = "i430fx";
    }

    public void onClickI440fx(View view) {
        Config.chipset = "i440fx";
    }

    private int getfreePciSlot() {
        for (int i = 0; i < Config.slot.length; i++) {
            if (Config.slot[i].equals("")) {
                return i;
            }
        }
        return -1;
    }

    private boolean checkPciSlotFor(String str) {
        for (int i = 0; i < Config.slot.length; i++) {
            if (Config.slot[i].equals(str)) {
                return true;
            }
        }
        return false;
    }

    private void setFreePciSlot(String str) {
        for (int i = 0; i < Config.slot.length; i++) {
            if (Config.slot[i].equals(str)) {
                Config.slot[i] = "";
                spSlot[i].setSelection(0);
                slotAdapter.notifyDataSetChanged();
            }
        }
    }

    private void checkVga() {
        if (Config.vgaExtension.equals("vbe")) {
            spVga.setSelection(checkPciSlotFor("pcivga") ? 1 : 0);
        } else if (Config.vgaExtension.equals("cirrus")) {
            spVga.setSelection(checkPciSlotFor("cirrus") ? 3 : 2);
        }
    }

    private void checkSound() {
        if (Config.useEs1370 && checkPciSlotFor("es1370"))
            spSound.setSelection(2);
        else if (Config.useSb16)
            spSound.setSelection(1);
        else
            spSound.setSelection(0);
    }

    private void checkEthernet() {
        if (Config.useE1000 && checkPciSlotFor("e1000"))
            spEthernet.setSelection(3);
        else if (checkPciSlotFor("ne2k"))
            spEthernet.setSelection(2);
        else if (Config.useNe2000)
            spEthernet.setSelection(1);
        else
            spEthernet.setSelection(0);
    }

    private void setOnInConfig(String str) {
        switch (str) {
            case "voodoo":
                Config.useVoodoo = true;
                break;
            case "es1370":
                Config.useEs1370 = true;
                break;
            case "ne2k":
                Config.useNe2000 = true;
                Config.useRtl8029 = true;
                break;
            case "e1000":
                Config.useE1000 = true;
                break;
            case "pcivga":
                Config.vgaExtension = "vbe";
                Config.vgaRomImage = "VGABIOS-lgpl-latest";
                break;
            case "cirrus":
                Config.vgaExtension = "cirrus";
                Config.vgaRomImage = "VGABIOS-lgpl-latest-cirrus";
                break;
        }
    }

}
