package ffts.android.moefmdroid;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

public class MoeSettingsActivity extends PreferenceActivity {

	SharedPreferences sp;
	Preference mUser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.settings);
		sp = getSharedPreferences("MoeFM", MODE_PRIVATE);
		mUser = findPreference("user_name");
		mUser.setSummary(sp.getString("user_name", "none"));
	}
}
