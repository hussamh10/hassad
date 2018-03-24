package com.hush.hassad.home;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hush.hassad.R;

public class DayFragment extends Fragment {


	public DayFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_home_sub, container, false);
	}
}
