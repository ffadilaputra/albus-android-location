package geolocation.ffadilaputra.org.modul_geolocation;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AddressAsycTask extends AsyncTask<Location , Void, String> {

	interface onTaskRampung{
		void onTaskCompleted(String result);
	}

	private Context mContext;
	private onTaskRampung mListenere;

	public AddressAsycTask(Context context, onTaskRampung rampung) {
		mContext = context;
		mListenere = rampung;
	}

	@Override
	protected void onPostExecute(String s) {
		mListenere.onTaskCompleted(s);
		super.onPostExecute(s);
	}

	@Override
	protected String doInBackground(Location... locations) {
		Geocoder geocoder = new Geocoder(mContext,Locale.getDefault());
		Location location = locations[0];
		String resultMessage = "";
		List<Address> addresses = null;

		try {
			addresses = geocoder.getFromLocation(
					location.getLatitude(),
					location.getLongitude(),1
			);

			if (addresses == null || addresses.size() == 0){
				if (resultMessage.isEmpty()){
					resultMessage = "ALamat tida ditemukan";
					Log.e("Lokasi Error bto",resultMessage);
				}
			}else{
				Address address = addresses.get(0);
				ArrayList<String> listAddress = new ArrayList<>();

				for (int i=0; i<= address.getMaxAddressLineIndex(); i++){
					listAddress.add(address.getAddressLine(i));
				}

				resultMessage = TextUtils.join("\n", listAddress);

			}

		}catch (IOException e){
			resultMessage = "Layanan tida tersedia";
			Log.e("Lokasi eror brader",resultMessage,e);
		}catch (IllegalArgumentException ilegal){
			resultMessage = "Koordinat invalid";
			Log.e("Lokasi Error",resultMessage+"."+"Lattitide"+location.getLatitude()+"Logitude"+location.getLongitude(),ilegal);
		}

		return resultMessage;
	}
}
