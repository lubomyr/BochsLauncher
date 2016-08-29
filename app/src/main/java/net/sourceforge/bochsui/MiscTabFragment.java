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
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class MiscTabFragment extends Fragment {
    static TextView tvRomImage;
    static TextView tvVgaRomImage;
    private CheckBox cbFullscreen;
    private SeekBar sbVgaUpdateFreq;
    private TextView tvVgaUpdateFreq;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_misc, container, false);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        final List<String> syncList = Arrays.asList("none", "slowdown", "realtime", "both");
        final int minValueVgaUpdateFreq = 5;
        Spinner spClockSync = (Spinner) rootView.findViewById(R.id.miscSpinnerClockSync);
        Button btRomImage = (Button) rootView.findViewById(R.id.miscButtonRomImage);
        Button btVgaRomImage = (Button) rootView.findViewById(R.id.miscButtonVgaRomImage);
        tvRomImage = (TextView) rootView.findViewById(R.id.miscTextViewRomImage);
        tvVgaRomImage = (TextView) rootView.findViewById(R.id.miscTextViewVgaRomImage);
        cbFullscreen = (CheckBox) rootView.findViewById(R.id.miscCheckBoxFullscreen);
        sbVgaUpdateFreq = (SeekBar) rootView.findViewById(R.id.miscSeekBarVgaUpdateFreq);
        tvVgaUpdateFreq = (TextView) rootView.findViewById(R.id.miscTextViewVgaUpdateFreq);
        SpinnerAdapter syncAdapter = new ArrayAdapter<String>(MainActivity.main, R.layout.spinner_row, syncList);
        spClockSync.setAdapter(syncAdapter);
        tvRomImage.setText(MainActivity.getFileName(Config.romImage));
        tvVgaRomImage.setText(MainActivity.getFileName(Config.vgaRomImage));
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

}
