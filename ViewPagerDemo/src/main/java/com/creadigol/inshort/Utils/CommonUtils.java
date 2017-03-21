package com.creadigol.inshort.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CommonUtils
{

	public static boolean isNetworkAvailable() {
		boolean isNetworkAvailable = false;
		ConnectivityManager connectivityManager =
				(ConnectivityManager) MyApplication.getInstance()
						.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

		if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
			isNetworkAvailable = true;
		}

		return isNetworkAvailable;
	}

	public static String getFormatedDate(long milliSeconds, String dateFormat)
	{
		// Create a DateFormatter object for displaying date in specified format.
		DateFormat formatter = new SimpleDateFormat(dateFormat);

		// Create a calendar object that will convert the date and time value in milliseconds to date.
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milliSeconds);
		//String date = formatter.format(calendar.getTime());
		return formatter.format(calendar.getTime());

//        try {
//            return formatter.parse(date);
//        } catch (ParseException e) {
//            return null;
//        }
	}

	public static boolean isSameDomain(String url, String url1) {
		return getRootDomainUrl(url.toLowerCase()).equals(getRootDomainUrl(url1.toLowerCase()));
	}
	public static String base64Decoding(String values) {
		byte[] data = Base64.decode(values, Base64.DEFAULT);
		try {
			return new String(data, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
	private static String getRootDomainUrl(String url) {
		String[] domainKeys = url.split("/")[2].split("\\.");
		int length = domainKeys.length;
		int dummy = domainKeys[0].equals("www") ? 1 : 0;
		if (length - dummy == 2)
			return domainKeys[length - 2] + "." + domainKeys[length - 1];
		else {
			if (domainKeys[length - 1].length() == 2) {
				return domainKeys[length - 3] + "." + domainKeys[length - 2] + "." + domainKeys[length - 1];
			} else {
				return domainKeys[length - 2] + "." + domainKeys[length - 1];
			}
		}
	}
}
