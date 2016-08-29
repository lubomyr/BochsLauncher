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
import android.widget.Toast;

import net.sourceforge.bochsui.adapter.TabsPagerAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {
    public static String path;
    public static MainActivity main;

    private ViewPager viewPager;
    private ActionBar actionBar;

    // Tab titles
    private String[] tabs = {"Storage", "Hardware", "Misc"};

    private String m_chosenDir = "";
    private boolean m_newFolderEnabled = true;

    private enum Requestor {ATA0_MASTER, ATA0_SLAVE, ATA1_MASTER, ATA1_SLAVE, FLOPPY_A, FLOPPY_B, ROM, VGAROM}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        main = this;
        path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/net.sourceforge.bochs/files/bochsrc.txt";

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
        TabsPagerAdapter mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

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

    static String getFileName(String path) {
        String result;
        if (path.contains("/"))
            result = path.substring(path.lastIndexOf("/") + 1, path.length());
        else
            result = path;
        return result;
    }

    public void browseFloppyA(View view) {
        fileSelection(Requestor.FLOPPY_A);
    }

    public void browseFloppyB(View view) {
        fileSelection(Requestor.FLOPPY_B);
    }

    public void browseAta0m(View view) {
        if (StorageTabFragment.cbVvfatAta0m.isChecked())
            dirSelection(Requestor.ATA0_MASTER);
        else
            fileSelection(Requestor.ATA0_MASTER);
    }

    public void browseAta0s(View view) {
        if (StorageTabFragment.cbVvfatAta0s.isChecked())
            dirSelection(Requestor.ATA0_SLAVE);
        else
            fileSelection(Requestor.ATA0_SLAVE);
    }

    public void browseAta1m(View view) {
        if (StorageTabFragment.cbVvfatAta1m.isChecked())
            dirSelection(Requestor.ATA1_MASTER);
        else
            fileSelection(Requestor.ATA1_MASTER);
    }

    public void browseAta1s(View view) {
        if (StorageTabFragment.cbVvfatAta1s.isChecked())
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

    private void dirSelection(final Requestor num) {
        // Create DirectoryChooserDialog and register a callback
        DirectoryChooserDialog directoryChooserDialog =
                new DirectoryChooserDialog(MainActivity.main,
                        new DirectoryChooserDialog.ChosenDirectoryListener() {
                            @Override
                            public void onChosenDir(String chosenDir) {
                                m_chosenDir = chosenDir;
                                switch (num) {
                                    case ATA0_MASTER:
                                        StorageTabFragment.tvAta0m.setText(chosenDir);
                                        Config.ata0m_image = chosenDir;
                                        Config.ata0mMode = "vvfat";
                                        break;
                                    case ATA0_SLAVE:
                                        StorageTabFragment.tvAta0s.setText(chosenDir);
                                        Config.ata0s_image = chosenDir;
                                        Config.ata0sMode = "vvfat";
                                        break;
                                    case ATA1_MASTER:
                                        StorageTabFragment.tvAta1m.setText(chosenDir);
                                        Config.ata1m_image = chosenDir;
                                        Config.ata1mMode = "vvfat";
                                        break;
                                    case ATA1_SLAVE:
                                        StorageTabFragment.tvAta1s.setText(chosenDir);
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
        FileChooser filechooser = new FileChooser(MainActivity.main);
        filechooser.setFileListener(new FileChooser.FileSelectedListener() {
            @Override
            public void fileSelected(final File file) {
                String filename = file.getAbsolutePath();
                Log.d("File", filename);
                switch (num) {
                    case ATA0_MASTER:
                        StorageTabFragment.tvAta0m.setText(file.getName());
                        Config.ata0m_image = filename;
                        Config.ata0mMode = getMode(file.getName());
                        break;
                    case ATA0_SLAVE:
                        StorageTabFragment.tvAta0s.setText(file.getName());
                        Config.ata0s_image = filename;
                        Config.ata0sMode = getMode(file.getName());
                        break;
                    case ATA1_MASTER:
                        StorageTabFragment.tvAta1m.setText(file.getName());
                        Config.ata1m_image = filename;
                        Config.ata1mMode = getMode(file.getName());
                        break;
                    case ATA1_SLAVE:
                        StorageTabFragment.tvAta1s.setText(file.getName());
                        Config.ata1s_image = filename;
                        Config.ata1sMode = getMode(file.getName());
                        break;
                    case FLOPPY_A:
                        StorageTabFragment.tvFloppyA.setText(file.getName());
                        Config.floppyA_image = filename;
                        break;
                    case FLOPPY_B:
                        StorageTabFragment.tvFloppyB.setText(file.getName());
                        Config.floppyB_image = filename;
                        break;
                    case ROM:
                        MiscTabFragment.tvRomImage.setText(file.getName());
                        Config.romImage = filename;
                        break;
                    case VGAROM:
                        MiscTabFragment.tvVgaRomImage.setText(file.getName());
                        Config.vgaRomImage = filename;
                        break;
                }

            }
        });
        // Set up and filter my extension I am looking for
        //filechooser.setExtension("img");
        filechooser.showDialog();
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

    public void onClickI430fx(View view) {
        Config.chipset = "i430fx";
    }

    public void onClickI440fx(View view) {
        Config.chipset = "i440fx";
    }
}
