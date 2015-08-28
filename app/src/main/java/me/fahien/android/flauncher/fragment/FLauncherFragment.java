package me.fahien.android.flauncher.fragment;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import me.fahien.android.flauncher.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class FLauncherFragment extends ListFragment {
	private static final String TAG = FLauncherFragment.class.getSimpleName();

	public FLauncherFragment() {}

	/**
	 * Uses an implicit intent to gather the desired activities and present them in a list
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Create an implicit intent.
		Intent startup = new Intent(Intent.ACTION_MAIN);
		startup.addCategory(Intent.CATEGORY_LAUNCHER);

		// Get a list of activities that match the intent from the Package Manager
		final PackageManager pm = getActivity().getPackageManager();
		List<ResolveInfo> activities = pm.queryIntentActivities(startup, 0);

		Log.i(TAG, "I've found " + activities.size() + " activities.");

		sortActivities(activities);

		createArrayAdapter(pm, activities);
	}

	/**
	 * Sort the ResolveInfo objects returned from the PackageManager alphabetically
	 */
	private void sortActivities(List<ResolveInfo> activities) {
		Collections.sort(activities, new Comparator<ResolveInfo>() {
			@Override
			public int compare(ResolveInfo a, ResolveInfo b) {
				PackageManager pm = getActivity().getPackageManager();
				return String.CASE_INSENSITIVE_ORDER.compare(
						a.loadLabel(pm).toString(),
						b.loadLabel(pm).toString());
			}
		});
	}

	/**
	 * Create an ArrayAdapter that will create simple list item views
	 */
	private void createArrayAdapter(final PackageManager pm, final List<ResolveInfo> activities) {
		ArrayAdapter<ResolveInfo> adapter = new ArrayAdapter<ResolveInfo>(
				getActivity(),
				R.layout.list_item_activity,
				activities) {
			public View getView(int position, View view, ViewGroup parent) {
				if (view == null) {
					view = getActivity().getLayoutInflater().inflate(R.layout.list_item_activity, null);
				}
				ResolveInfo info = getItem(position);
				ImageView imageView = (ImageView) view.findViewById(R.id.activity_list_item_iconImageView);
				imageView.setImageDrawable(info.loadIcon(pm));
				TextView textView = (TextView) view.findViewById(R.id.activity_list_item_nameTextView);
				textView.setText(info.loadLabel(pm));
				return view;
			}
		};
		setListAdapter(adapter);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list_fragment, container, false);
	}

	/**
	 * Starts the selected activity when the user presses its list item using an explicit intent
	 */
	@Override
	public void onListItemClick(ListView list, View view, int position, long id) {
		ResolveInfo resolveInfo = (ResolveInfo) list.getAdapter().getItem(position);
		ActivityInfo activityInfo = resolveInfo.activityInfo;

		if (activityInfo == null) return;

		Intent i = new Intent(Intent.ACTION_MAIN);
		// Use the package name and class name from the metadata
		i.setClassName(activityInfo.applicationInfo.packageName, activityInfo.name);
		// Start a new task when you start a new activity
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		startActivity(i);
	}
}
