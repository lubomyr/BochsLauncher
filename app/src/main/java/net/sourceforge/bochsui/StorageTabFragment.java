package net.sourceforge.bochsui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
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

import java.util.Arrays;
import java.util.List;
import android.content.res.*;

public class StorageTabFragment extends Fragment {
    static TextView tvFloppyA;
    static TextView tvFloppyB;
    static TextView tvAta0m;
    static TextView tvAta0s;
    static TextView tvAta1m;
    static TextView tvAta1s;
    static CheckBox cbVvfatAta0m;
    static CheckBox cbVvfatAta0s;
    static CheckBox cbVvfatAta1m;
    static CheckBox cbVvfatAta1s;
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

    //private MainActivity main = MainActivity.main;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_storage, container, false);

        return rootView;
    }

	@Override
	public void onStart()
	{
		super.onStart();
		
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
	}

    @Override
    public void onResume() {
        super.onResume();

        final List<String> typeList = Arrays.asList("disk", "cdrom");
        final List<String> bootList = Arrays.asList("disk", "cdrom", "floppy");
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

}
