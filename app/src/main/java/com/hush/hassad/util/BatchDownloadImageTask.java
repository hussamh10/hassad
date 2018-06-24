package com.hush.hassad.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.hush.hassad.controller.player.User;

import java.io.InputStream;
import java.util.ArrayList;

public class BatchDownloadImageTask extends AsyncTask<ArrayList<User>, Void, ArrayList<Bitmap>> {
	private IDownloaded cback;
	public BatchDownloadImageTask(IDownloaded cback) {
		this.cback = cback;
	}
	
	protected ArrayList<Bitmap> doInBackground(ArrayList<User>... users) {
		
		ArrayList<Bitmap> userImg = new ArrayList<>();
		for (User u : users[0]) {
			Bitmap bmp = null;
			try {
				bmp = BitmapFactory.decodeStream(new java.net.URL(u.getInfo().getPhotoUrl()).openStream());
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			} finally {
				userImg.add(bmp);
			}
		}
		return userImg;
	}
	
	protected void onPostExecute(ArrayList<Bitmap> result) {
		cback.downloaded(result);
	}
	
	public interface IDownloaded {
		void downloaded(ArrayList<Bitmap> result);
	}
	
}