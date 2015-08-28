package me.fahien.android.flauncher.activity;

import android.support.v4.app.Fragment;

import me.fahien.android.flauncher.fragment.FLauncherFragment;

public class FLauncherActivity extends SingleFragmentActivity {

	@Override
	public Fragment createFragment() {
		return new FLauncherFragment();
	}
}
