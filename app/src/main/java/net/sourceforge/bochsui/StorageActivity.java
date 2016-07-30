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
	private CheckBox cbAta0;
	private CheckBox cbAta1;
	private TextView tvFloppyA;
	private TextView tvFloppyB;
	private TextView tvAta0;
	private TextView tvAta1;
	private Button btBrowseFloppyA;
	private Button btBrowseFloppyB;
	private Button btBrowseAta0;
	private Button btBrowseAta1;
	private Spinner spAta0Type;
	private Spinner spAta1Type;
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
		cbFloppyA = (CheckBox) findViewById(R.id.storageCheckBox1);
		tvFloppyA = (TextView) findViewById(R.id.storageTextView1);
		btBrowseFloppyA = (Button) findViewById(R.id.storageButton1);
		cbFloppyB = (CheckBox) findViewById(R.id.storageCheckBox2);
		tvFloppyB = (TextView) findViewById(R.id.storageTextView2);
		btBrowseFloppyB = (Button) findViewById(R.id.storageButton2);
		cbAta0 = (CheckBox) findViewById(R.id.storageCheckBox3);
		tvAta0 = (TextView) findViewById(R.id.storageTextView3);
		btBrowseAta0 = (Button) findViewById(R.id.storageButton3);
		cbAta1 = (CheckBox) findViewById(R.id.storageCheckBox4);
		tvAta1 = (TextView) findViewById(R.id.storageTextView4);
		btBrowseAta1 = (Button) findViewById(R.id.storageButton4);
		cbFloppyA.setChecked(Config.floppyA);
		tvFloppyA.setText(Config.floppyA_image);
		btBrowseFloppyA.setEnabled(Config.floppyA);
		cbFloppyB.setChecked(Config.floppyB);
		tvFloppyB.setText(Config.floppyB_image);
		btBrowseFloppyB.setEnabled(Config.floppyB);
		cbAta0.setChecked(Config.ata0);
		tvAta0.setText(Config.ata0_image);
		btBrowseAta0.setEnabled(Config.ata0);
		cbAta1.setChecked(Config.ata1);
		tvAta1.setText(Config.ata1_image);
		btBrowseAta1.setEnabled(Config.ata1);
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
		cbAta0.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
					Config.ata0 = cbAta0.isChecked();
					btBrowseAta0.setEnabled(Config.ata0);
					tvAta0.setEnabled(Config.ata0);
				}
			}
		);
		cbAta1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
					Config.ata1 = cbAta1.isChecked();
					btBrowseAta1.setEnabled(Config.ata1);
					tvAta1.setEnabled(Config.ata1);
				}
			}
		);
	}
	
	public void setupSpinners() {
		String[] typeList = {"disk", "cdrom"};
		String[] bootList = {"disk", "cdrom"};
		spAta0Type = (Spinner) findViewById(R.id.storageSpinner1);
		spAta1Type = (Spinner) findViewById(R.id.storageSpinner2);
		spBoot = (Spinner) findViewById(R.id.storageSpinner3);
		SpinnerAdapter adapterType = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, typeList);
		SpinnerAdapter adapterBoot = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, bootList);
		spAta0Type.setAdapter(adapterType);
		spAta1Type.setAdapter(adapterType);
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
	
	public void browseAta0(View view) {
		FileChooser filechooser = new FileChooser(StorageActivity.this);
		filechooser.setFileListener(new FileChooser.FileSelectedListener() {
				@Override
				public void fileSelected(final File file)
				{
					// ....do something with the file
					String filename = file.getAbsolutePath();
					Log.d("File", filename);
					tvAta0.setText(filename);
					Config.ata0_image = filename;
					// then actually do something in another module

				}
			});
        // Set up and filter my extension I am looking for
		//filechooser.setExtension("img");
		filechooser.showDialog();
	}
	
	public void browseAta1(View view) {
		FileChooser filechooser = new FileChooser(StorageActivity.this);
		filechooser.setFileListener(new FileChooser.FileSelectedListener() {
				@Override
				public void fileSelected(final File file)
				{
					// ....do something with the file
					String filename = file.getAbsolutePath();
					Log.d("File", filename);
					tvAta1.setText(filename);
					Config.ata1_image = filename;
					// then actually do something in another module

				}
			});
        // Set up and filter my extension I am looking for
		//filechooser.setExtension("img");
		filechooser.showDialog();
	}
}
