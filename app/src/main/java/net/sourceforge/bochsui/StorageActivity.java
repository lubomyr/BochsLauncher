package net.sourceforge.bochsui;
 
import android.app.Activity;
import android.os.Bundle;
import android.widget.*;
import java.util.*;
import android.view.*;
import java.io.*;
import android.util.*;
 
public class StorageActivity extends Activity implements TabService {
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
   
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.storage);
	
	applyTab();
	setupTab();
	setupSpinners();
  }
  
	@Override
	public void applyTab()
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
		tvFloppyA.setText(Config.floppyA_image);
		tvFloppyA.setEnabled(Config.floppyA);
		btBrowseFloppyA.setEnabled(Config.floppyA);
		cbFloppyB.setChecked(Config.floppyB);
		tvFloppyB.setText(Config.floppyB_image);
		tvFloppyB.setEnabled(Config.floppyB);
		btBrowseFloppyB.setEnabled(Config.floppyB);
		cbAta0m.setChecked(Config.ata0m);
		tvAta0m.setText(Config.ata0m_image);
		tvAta0m.setEnabled(Config.ata0m);
		cbVvfatAta0m.setEnabled(Config.ata0m);
		btBrowseAta0m.setEnabled(Config.ata0m);
		cbAta0s.setChecked(Config.ata0s);
		tvAta0s.setText(Config.ata0s_image);
		tvAta0s.setEnabled(Config.ata0s);
		cbVvfatAta0s.setEnabled(Config.ata0s);
		btBrowseAta0s.setEnabled(Config.ata0s);
		cbAta1m.setChecked(Config.ata1m);
		tvAta1m.setText(Config.ata1m_image);
		tvAta1m.setEnabled(Config.ata1m);
		cbVvfatAta1m.setEnabled(Config.ata1m);
		btBrowseAta1m.setEnabled(Config.ata1m);
		cbAta1s.setChecked(Config.ata1s);
		tvAta1s.setText(Config.ata1s_image);
		tvAta1s.setEnabled(Config.ata1s);
		cbVvfatAta1s.setEnabled(Config.ata1s);
		btBrowseAta1s.setEnabled(Config.ata1s);
	}

	@Override
	public void setupTab()
	{
		cbFloppyA.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
					Config.floppyA = cbFloppyA.isChecked();
					btBrowseFloppyA.setEnabled(Config.floppyA);
					tvFloppyA.setEnabled(Config.floppyA);
				}
			}
		);     
		cbFloppyB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
					Config.floppyB = cbFloppyB.isChecked();
					btBrowseFloppyB.setEnabled(Config.floppyB);
					tvFloppyB.setEnabled(Config.floppyB);
				}
			}
		);
		cbAta0m.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
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
				public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
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
				public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
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
				public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
					Config.ata1s = cbAta1s.isChecked();
					btBrowseAta1s.setEnabled(Config.ata1s);
					tvAta1s.setEnabled(Config.ata1s);
					spAta1sType.setEnabled(Config.ata1s);
					cbVvfatAta1s.setEnabled(Config.ata1s);
				}
			}
		);
	}
	
	public void setupSpinners() {
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
	
	public void browseFloppyA(View view) {
		FileChooser filechooser = new FileChooser(StorageActivity.this);
		filechooser.setFileListener(new FileChooser.FileSelectedListener() {
				@Override
				public void fileSelected(final File file)
				{
					// ....do something with the file
					String filename = file.getAbsolutePath();
					Log.d("File", filename);
					tvFloppyA.setText(filename);
					Config.floppyA_image = filename;
					// then actually do something in another module

				}
			});
        // Set up and filter my extension I am looking for
		//filechooser.setExtension("img");
		filechooser.showDialog();
	}
	
	public void browseFloppyB(View view) {
		FileChooser filechooser = new FileChooser(StorageActivity.this);
		filechooser.setFileListener(new FileChooser.FileSelectedListener() {
				@Override
				public void fileSelected(final File file)
				{
					// ....do something with the file
					String filename = file.getAbsolutePath();
					Log.d("File", filename);
					tvFloppyB.setText(filename);
					Config.floppyB_image = filename;
					// then actually do something in another module

				}
			});
        // Set up and filter my extension I am looking for
		//filechooser.setExtension("img");
		filechooser.showDialog();
	}
	
	public void browseAta0m(View view) {
		FileChooser filechooser = new FileChooser(StorageActivity.this);
		filechooser.setFileListener(new FileChooser.FileSelectedListener() {
				@Override
				public void fileSelected(final File file)
				{
					// ....do something with the file
					String filename = file.getAbsolutePath();
					Log.d("File", filename);
					tvAta0m.setText(filename);
					Config.ata0m_image = filename;
					// then actually do something in another module

				}
			});
        // Set up and filter my extension I am looking for
		//filechooser.setExtension("img");
		filechooser.showDialog();
	}
	
	public void browseAta0s(View view) {
		FileChooser filechooser = new FileChooser(StorageActivity.this);
		filechooser.setFileListener(new FileChooser.FileSelectedListener() {
				@Override
				public void fileSelected(final File file)
				{
					// ....do something with the file
					String filename = file.getAbsolutePath();
					Log.d("File", filename);
					tvAta0s.setText(filename);
					Config.ata0s_image = filename;
					// then actually do something in another module

				}
			});
        // Set up and filter my extension I am looking for
		//filechooser.setExtension("img");
		filechooser.showDialog();
	}
	
	public void browseAta1m(View view) {
		FileChooser filechooser = new FileChooser(StorageActivity.this);
		filechooser.setFileListener(new FileChooser.FileSelectedListener() {
				@Override
				public void fileSelected(final File file)
				{
					// ....do something with the file
					String filename = file.getAbsolutePath();
					Log.d("File", filename);
					tvAta1m.setText(filename);
					Config.ata1m_image = filename;
					// then actually do something in another module

				}
			});
        // Set up and filter my extension I am looking for
		//filechooser.setExtension("img");
		filechooser.showDialog();
	}
	
	public void browseAta1s(View view) {
		FileChooser filechooser = new FileChooser(StorageActivity.this);
		filechooser.setFileListener(new FileChooser.FileSelectedListener() {
				@Override
				public void fileSelected(final File file)
				{
					// ....do something with the file
					String filename = file.getAbsolutePath();
					Log.d("File", filename);
					tvAta1s.setText(filename);
					Config.ata1s_image = filename;
					// then actually do something in another module

				}
			});
        // Set up and filter my extension I am looking for
		//filechooser.setExtension("img");
		filechooser.showDialog();
	}
}
