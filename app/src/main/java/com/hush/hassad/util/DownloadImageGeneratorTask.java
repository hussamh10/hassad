package com.hush.hassad.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.hush.hassad.controller.player.User;

import java.io.InputStream;
import java.util.ArrayList;

public class DownloadImageGeneratorTask extends AsyncTask<User, Bitmap, Void> {
	private IDownloaded cback;
	public DownloadImageGeneratorTask(IDownloaded cback) {
		this.cback = cback;
	}
	
	protected Void doInBackground(User... users) {
		
		for (User u : users) {
			Bitmap bmp = null;
			try {
				bmp = BitmapFactory.decodeStream(new java.net.URL(u.getInfo().getPhotoUrl()).openStream());
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			} finally {
				publishProgress(bmp);
			}
		}
		return null;
	}
	
	@Override
	protected void onProgressUpdate(Bitmap... values) {
		cback.downloaded(values[0]);
	}
	
	public interface IDownloaded {
		void downloaded(Bitmap bmp);
	}
	
}