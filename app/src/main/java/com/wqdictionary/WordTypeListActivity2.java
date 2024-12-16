package com.wqdictionary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class WordTypeListActivity2 extends Fragment {
	// When requested, this adapter returns a DemoObjectFragment
	// representing an object in the collection.
	FragmenCollectionPagerAdapter demoCollectionPagerAdapter;
	ViewPager viewPager;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater,
							 @Nullable ViewGroup container,
							 @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.wordtype2, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		demoCollectionPagerAdapter = new FragmenCollectionPagerAdapter(getChildFragmentManager());
		viewPager = view.findViewById(R.id.pager);
		viewPager.setAdapter(demoCollectionPagerAdapter);
	}
}

// Since this is an object collection, use a FragmentStatePagerAdapter,
// NOT a FragmentPagerAdapter.
class FragmenCollectionPagerAdapter extends FragmentStatePagerAdapter {
	public FragmenCollectionPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int i) {
		Fragment fragment = new DemoObjectFragment();
		Bundle args = new Bundle();
		// Our object is just an integer :-P
		args.putInt(DemoObjectFragment.ARG_OBJECT, i + 1);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public int getCount() {
		return 100;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return "OBJECT " + (position + 1);
	}
}

// Instances of this class are fragments representing a single
// object in the collection.
class DemoObjectFragment extends Fragment {
	public static final String ARG_OBJECT = "object";

	@Override
	public View onCreateView(LayoutInflater inflater,
							 ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.wordtype_fragmen, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		Bundle args = getArguments();
		((TextView) view.findViewById(android.R.id.text1))
				.setText(Integer.toString(args.getInt(ARG_OBJECT)));
	}
}