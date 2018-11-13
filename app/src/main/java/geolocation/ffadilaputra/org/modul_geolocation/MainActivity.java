package geolocation.ffadilaputra.org.modul_geolocation;

import android.Manifest;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity implements AddressAsycTask.onTaskRampung{

    private Button btn;
    private Location lastLocation;
    private TextView txtLastLocation;
    private FusedLocationProviderClient mFusedLocationClient;
	private AnimatorSet rotateAnime;
	private ImageView imageView;

	private boolean mtrackingLocation;
	private LocationCallback mLocationCallback;

	private static final int REQUEST_LOCATION_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView)findViewById(R.id.image_view);
        txtLastLocation = (TextView)findViewById(R.id.last_location);
        btn = (Button) findViewById(R.id.btn_submit);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
		rotateAnime = (AnimatorSet) AnimatorInflater.loadAnimator(this,R.animator.rotate);
		rotateAnime.setTarget(imageView);

		mLocationCallback = new LocationCallback(){
			@Override
			public void onLocationResult(LocationResult locationResult) {
				if (mtrackingLocation){
					new AddressAsycTask(MainActivity.this,MainActivity.this).execute(locationResult.getLastLocation());
				}
			}
		};

        btn.setOnClickListener(new View.OnClickListener(){
			@Override
            public void onClick(View v) {

                if (!mtrackingLocation){
					getLocation();
				}else{
                	stopLocation();
				}
            }
        });

    }

	private void stopLocation() {
    	if (mtrackingLocation){
    		mtrackingLocation = false;
    		mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    		btn.setText("Mulai tracking gan");
    		txtLastLocation.setText("Tracking dihentikan ");
    		rotateAnime.end();
		}
	}

	private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				ActivityCompat.requestPermissions(this,new String[]{
						Manifest.permission.ACCESS_FINE_LOCATION
				},REQUEST_LOCATION_PERMISSION);
        }else{
            //Log.d("GetPermissions","getLocation: permission granted");
//			mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
//				@Override
//				public void onSuccess(Location location) {
//					if (location != null){
////						lastLocation = location;
////						txtLastLocation.setText(getString(R.string.location_text,lastLocation.getLatitude(),lastLocation.getLongitude(),lastLocation.getTime()));
//						new AddressAsycTask(MainActivity.this,MainActivity.this).execute(location);
//					}else{
//						txtLastLocation.setText("Lokasi tidak tersedia");
//					}
//				}
//			});
			mFusedLocationClient.requestLocationUpdates(getLocationRequest(),mLocationCallback,null);
			txtLastLocation.setText(getString(R.string.alamat_text,"Sedang mencari jodoh gan", System.currentTimeMillis()));
			mtrackingLocation = true;
			btn.setText("Berhenti dong gan");
			rotateAnime.start();
        }

    }

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		switch (requestCode){
			case REQUEST_LOCATION_PERMISSION:
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
					getLocation();
				}else{
					Toast.makeText(this,"Permission gak dapet bang",Toast.LENGTH_SHORT).show();
				}
		}
	}

	@Override
	public void onTaskCompleted(String result) {
    	if (mtrackingLocation){
			txtLastLocation.setText(getString(R.string.alamat_text, result, System.currentTimeMillis()));
		}

	}

	private LocationRequest getLocationRequest(){
    	LocationRequest locationRequest = new LocationRequest();
    	locationRequest.setInterval(10000);
    	locationRequest.setFastestInterval(5000);
    	locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    	return locationRequest;
	}

}
