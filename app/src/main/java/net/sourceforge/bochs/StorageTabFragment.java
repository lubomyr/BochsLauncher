package net.sourceforge.bochs;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class StorageTabFragment extends Fragment {
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
    private CheckBox cbFloppyA;
    private CheckBox cbFloppyB;
    private CheckBox cbAta0m;
    private CheckBox cbAta0s;
    private CheckBox cbAta1m;
    private CheckBox cbAta1s;
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

    private String m_chosenDir = "";
    private boolean m_newFolderEnabled = true;

    private enum Requestor {ATA0_MASTER, ATA0_SLAVE, ATA1_MASTER, ATA1_SLAVE, FLOPPY_A, FLOPPY_B}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_storage, container, false);
        setupView(rootView);

        return rootView;
    }

    private void setupView(View rootView) {
        final List<String> typeList = Arrays.asList("disk", "cdrom");
        final List<String> bootList = Arrays.asList("disk", "cdrom", "floppy");
        cbFloppyA = (CheckBox) rootView.findViewById(R.id.storageCheckBoxFloppyA);
        tvFloppyA = (TextView) rootView.findViewById(R.id.storageTextViewFloppyA);
        btBrowseFloppyA = (Button) rootView.findViewById(R.id.storageButtonFloppyA);
        cbFloppyB = (CheckBox) rootView.findViewById(R.id.storageCheckBoxFloppyB);
        tvFloppyB = (TextView) rootView.findViewById(R.id.storageTextViewFloppyB);
        btBrowseFloppyB = (Button) rootView.findViewById(R.id.storageButtonFloppyB);
        cbAta0m = (CheckBox) rootView.findViewById(R.id.storageCheckBoxAta0m);
        tvAta0m = (TextView) rootView.findViewById(R.id.storageTextViewAta0m);
        cbVvfatAta0m = (CheckBox) rootView.findViewById(R.id.storageCheckBoxAta0mVvfat);
        btBrowseAta0m = (Button) rootView.findViewById(R.id.storageButtonAta0m);
        cbAta0s = (CheckBox) rootView.findViewById(R.id.storageCheckBoxAta0s);
        tvAta0s = (TextView) rootView.findViewById(R.id.storageTextViewAta0s);
        cbVvfatAta0s = (CheckBox) rootView.findViewById(R.id.storageCheckBoxAta0sVvfat);
        btBrowseAta0s = (Button) rootView.findViewById(R.id.storageButtonAta0s);
        cbAta1m = (CheckBox) rootView.findViewById(R.id.storageCheckBoxAta1m);
        tvAta1m = (TextView) rootView.findViewById(R.id.storageTextViewAta1m);
        cbVvfatAta1m = (CheckBox) rootView.findViewById(R.id.storageCheckBoxAta1mVvfat);
        btBrowseAta1m = (Button) rootView.findViewById(R.id.storageButtonAta1m);
        cbAta1s = (CheckBox) rootView.findViewById(R.id.storageCheckBoxAta1s);
        tvAta1s = (TextView) rootView.findViewById(R.id.storageTextViewAta1s);
        cbVvfatAta1s = (CheckBox) rootView.findViewById(R.id.storageCheckBoxAta1sVvfat);
        btBrowseAta1s = (Button) rootView.findViewById(R.id.storageButtonAta1s);
        spAta0mType = (Spinner) rootView.findViewById(R.id.storageSpinnerAta0m);
        spAta0sType = (Spinner) rootView.findViewById(R.id.storageSpinnerAta0s);
        spAta1mType = (Spinner) rootView.findViewById(R.id.storageSpinnerAta1m);
        spAta1sType = (Spinner) rootView.findViewById(R.id.storageSpinnerAta1s);
        if (getResources().getDisplayMetrics().density <= 1.5) {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                cbFloppyA.setText("FloppyA");
                cbFloppyB.setText("FloppyB");
                btBrowseFloppyA.setText("sel.");
                btBrowseFloppyB.setText("sel.");
            } else {
                cbFloppyA.setText("Floppy A");
                cbFloppyB.setText("Floppy B");
                btBrowseFloppyA.setText("select");
                btBrowseFloppyB.setText("select");
            }
        }
        Spinner spBoot = (Spinner) rootView.findViewById(R.id.storageSpinnerBoot);
        cbFloppyA.setChecked(Config.floppyA);
        tvFloppyA.setText(MainActivity.getFileName(Config.floppyA_image));
        tvFloppyA.setEnabled(Config.floppyA);
        btBrowseFloppyA.setEnabled(Config.floppyA);
        cbFloppyB.setChecked(Config.floppyB);
        tvFloppyB.setText(MainActivity.getFileName(Config.floppyB_image));
        tvFloppyB.setEnabled(Config.floppyB);
        btBrowseFloppyB.setEnabled(Config.floppyB);
        cbAta0m.setChecked(Config.ata0m);
        tvAta0m.setText(MainActivity.getFileName(Config.ata0m_image));
        tvAta0m.setEnabled(Config.ata0m);
        cbVvfatAta0m.setEnabled(Config.ata0m);
        btBrowseAta0m.setEnabled(Config.ata0m);
        cbAta0s.setChecked(Config.ata0s);
        tvAta0s.setText(MainActivity.getFileName(Config.ata0s_image));
        tvAta0s.setEnabled(Config.ata0s);
        cbVvfatAta0s.setEnabled(Config.ata0s);
        btBrowseAta0s.setEnabled(Config.ata0s);
        cbAta1m.setChecked(Config.ata1m);
        tvAta1m.setText(MainActivity.getFileName(Config.ata1m_image));
        tvAta1m.setEnabled(Config.ata1m);
        cbVvfatAta1m.setEnabled(Config.ata1m);
        btBrowseAta1m.setEnabled(Config.ata1m);
        cbAta1s.setChecked(Config.ata1s);
        tvAta1s.setText(MainActivity.getFileName(Config.ata1s_image));
        tvAta1s.setEnabled(Config.ata1s);
        cbVvfatAta1s.setEnabled(Config.ata1s);
        btBrowseAta1s.setEnabled(Config.ata1s);
        spAta0mType.setEnabled(Config.ata0m);
        spAta0sType.setEnabled(Config.ata0s);
        spAta1mType.setEnabled(Config.ata1m);
        spAta1sType.setEnabled(Config.ata1s);
        SpinnerAdapter adapterType = new ArrayAdapter<String>(MainActivity.main, R.layout.spinner_row, typeList);
        SpinnerAdapter adapterBoot = new ArrayAdapter<String>(MainActivity.main, R.layout.spinner_row, bootList);
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

            }
        });

        spAta0sType.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4) {
                Config.ata0sType = typeList.get(p3);
            }

            @Override
            public void onNothingSelected(AdapterView<?> p1) {

            }
        });

        spAta1mType.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4) {
                Config.ata1mType = typeList.get(p3);
            }

            @Override
            public void onNothingSelected(AdapterView<?> p1) {

            }
        });

        spAta1sType.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4) {
                Config.ata1sType = typeList.get(p3);
            }

            @Override
            public void onNothingSelected(AdapterView<?> p1) {

            }
        });

        spBoot.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4) {
                Config.boot = bootList.get(p3);
            }

            @Override
            public void onNothingSelected(AdapterView<?> p1) {

            }
        });

        OnClickListener storageOnClick = new OnClickListener() {

            @Override
            public void onClick(View p1) {
                switch (p1.getId()) {
                    case R.id.storageButtonFloppyA:
                        fileSelection(Requestor.FLOPPY_A);
                        break;
                    case R.id.storageButtonFloppyB:
                        fileSelection(Requestor.FLOPPY_B);
                        break;
                    case R.id.storageButtonAta0m:
                        if (cbVvfatAta0m.isChecked())
                            dirSelection(Requestor.ATA0_MASTER);
                        else
                            fileSelection(Requestor.ATA0_MASTER);
                        break;
                    case R.id.storageButtonAta0s:
                        if (cbVvfatAta0s.isChecked())
                            dirSelection(Requestor.ATA0_SLAVE);
                        else
                            fileSelection(Requestor.ATA0_SLAVE);
                        break;
                    case R.id.storageButtonAta1m:
                        if (cbVvfatAta1m.isChecked())
                            dirSelection(Requestor.ATA1_MASTER);
                        else
                            fileSelection(Requestor.ATA1_MASTER);
                        break;
                    case R.id.storageButtonAta1s:
                        if (cbVvfatAta1s.isChecked())
                            dirSelection(Requestor.ATA1_SLAVE);
                        else
                            fileSelection(Requestor.ATA1_SLAVE);
                        break;
                }
            }
        };

        btBrowseFloppyA.setOnClickListener(storageOnClick);
        btBrowseFloppyB.setOnClickListener(storageOnClick);
        btBrowseAta0m.setOnClickListener(storageOnClick);
        btBrowseAta0s.setOnClickListener(storageOnClick);
        btBrowseAta1m.setOnClickListener(storageOnClick);
        btBrowseAta1s.setOnClickListener(storageOnClick);
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
        FileChooser filechooser = new FileChooser(MainActivity.main);
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

}
