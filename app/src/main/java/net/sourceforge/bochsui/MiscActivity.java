package net.sourceforge.bochsui;
 
import android.app.Activity;
import android.os.Bundle;
import android.widget.*;
 
public class MiscActivity extends Activity implements TabService {
	EditText editMegs;
	
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.misc);
	
	applyTab();
	setupTab();
  }
  
  	@Override
	public void applyTab()
	{
		editMegs = (EditText) findViewById(R.id.miscEditText1);
		editMegs.setText(String.valueOf(Config.megs));
	}

	@Override
	public void setupTab()
	{
		Config.megs = Integer.parseInt(editMegs.getText().toString());
	}
	
}
