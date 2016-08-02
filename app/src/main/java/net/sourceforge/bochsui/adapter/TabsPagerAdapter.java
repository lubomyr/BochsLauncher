package net.sourceforge.bochsui.adapter;
 
import net.sourceforge.bochsui.MiscTabFragment;
import net.sourceforge.bochsui.StorageTabFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
 
public class TabsPagerAdapter extends FragmentPagerAdapter {
 
    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }
 
    @Override
    public Fragment getItem(int index) {
 
        switch (index) {
        case 0:
            // Storage tab fragment activity
            return new StorageTabFragment();
        case 1:
            // Misc tab fragment activity
            return new MiscTabFragment();
        
        }
 
        return null;
    }
 
    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 2;
    }
 
}
