package com.hush.hassad.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.hush.hassad.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Saad Mujeeb on 5/6/2018.
 */


public class SpinnerAdapter extends ArrayAdapter<String> {
	Context context;
	int resource;
	String[] countries;
	TypedArray flags;

	SpinnerItemHolder mSpinnerItemHolder;

	static class SpinnerItemHolder {
		TextView teamName;
		ImageView flag;
	}

	public SpinnerAdapter(Context context, int resource, String[] countries, TypedArray flags) {
		super(context, resource, countries);
		this.context = context;
		this.resource = resource;
		this.countries = countries;
		this.flags = flags;
	}

	@Override
	public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		// TODO: fix adapter
		if(convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView= inflater.inflate(R.layout.custom_spinner_items, null);

			mSpinnerItemHolder = new SpinnerItemHolder();

			mSpinnerItemHolder.teamName = convertView.findViewById(R.id.spinner_team_name);
			mSpinnerItemHolder.flag = convertView.findViewById(R.id.spinner_flag);
			convertView.setTag(mSpinnerItemHolder);
		}
		else {
			mSpinnerItemHolder = (SpinnerItemHolder) convertView.getTag();
		}


		mSpinnerItemHolder.teamName.setText(countries[position]);
		mSpinnerItemHolder.flag.setImageResource(flags.getResourceId(position, -1));

		return convertView;
	}

	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

		if(convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView= inflater.inflate(R.layout.custom_spinner_items, null);
		}

		mSpinnerItemHolder = new SpinnerItemHolder();

		mSpinnerItemHolder.teamName = convertView.findViewById(R.id.spinner_team_name);
		mSpinnerItemHolder.flag = convertView.findViewById(R.id.spinner_flag);

		mSpinnerItemHolder.teamName.setText(countries[position]);
		mSpinnerItemHolder.flag.setImageResource(flags.getResourceId(position, -1));

		return convertView;
	}
}