package net.sourceforge.bochs;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import net.sourceforge.bochs.adapter.ViewPagerAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    static String appPath;
    private String configPath;
    private final int REQUEST_EXTERNAL_STORAGE = 1;

    // Tab titles
    private String[] tabs = {"Storage", "Hardware", "Misc"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        appPath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/Android/data/" + getPackageName() + "/files/";
        configPath = appPath + "bochsrc.txt";

        //initToolbar();
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        ImageView startBtn = (ImageView) findViewById(R.id.start);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

        setupViewPager(viewPager);

        verifyStoragePermissions();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        StorageTabFragment storageTabFragment =  new StorageTabFragment();
        adapter.addFragment(storageTabFragment, tabs[0]);
        HardwareTabFragment hardwareTabFragment =  new HardwareTabFragment();
        adapter.addFragment(hardwareTabFragment, tabs[1]);
        MiscTabFragment miscTabFragment =  new MiscTabFragment();
        adapter.addFragment(miscTabFragment, tabs[2]);

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
    }

    static String getFileName(String path) {
        String result;
        if (path.contains("/"))
            result = path.substring(path.lastIndexOf("/") + 1, path.length());
        else
            result = path;
        return result;
    }

    private void save() {
        try {
            Config.writeConfig(configPath);
        } catch (IOException e) {
            Toast.makeText(MainActivity.this, "Error, config not saved", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(MainActivity.this, "config saved", Toast.LENGTH_SHORT).show();

        // run bochs app
        //ComponentName cn = new ComponentName("net.sourceforge.bochs", "net.sourceforge.bochs.MainActivity");
        Intent intent = new Intent(this, SDLActivity.class);
        //intent.setComponent(cn);
        startActivity(intent);
    }

    private void checkConfig() {
        if (!Config.configLoaded) {
            try {
                Config.readConfig(configPath);
            } catch (FileNotFoundException e) {
                Toast.makeText(MainActivity.this, "config not found", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(MainActivity.this, "config loaded", Toast.LENGTH_SHORT).show();
            Config.configLoaded = true;
        }
    }

    private boolean createDirIfNotExists() {
        boolean ret = true;

        File file = new File(appPath);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                ret = false;
            }
        }
        return ret;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE:
                if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    createDirIfNotExists();
                    checkConfig();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }



    public void verifyStoragePermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

            String[] PERMISSIONS_STORAGE = new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE };

            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
            return;
        }
        createDirIfNotExists();
        checkConfig();
    }

}
