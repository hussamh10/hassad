package com.hush.hassad.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Saad Mujeeb on 12/6/2018.
 */

public class GetByteArrayFromURL {

	public static byte[] getByteArrayFromURL(String src) {
		try {
			URL url = new URL(src);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);

			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
			byte[] byteArray = stream.toByteArray();

			return byteArray;
		} catch (IOException e) {
			// Log exception
			return null;
		}
	}
}
