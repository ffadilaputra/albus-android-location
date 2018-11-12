package geolocation.ffadilaputra.org.modul_geolocation;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity {

    private Button btn;
    private Location lastLocation;
    private TextView txtLastLocation;
    private FusedLocationProviderClient mFusedLocationClient;


	private static final int REQUEST_LOCATION_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtLastLocation = (TextView)findViewById(R.id.last_location);
        btn = (Button) findViewById(R.id.btn_submit);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        btn.setOnClickListener(new View.OnClickListener(){
			@Override
            public void onClick(View v) {
                getLocation();
            }
        });

    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				ActivityCompat.requestPermissions(this,new String[]{
						Manifest.permission.ACCESS_FINE_LOCATION
				},REQUEST_LOCATION_PERMISSION);
        }else{
            //Log.d("GetPermissions","getLocation: permission granted");
			mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
				@Override
				public void onSuccess(Location location) {
					if (location != null){
						lastLocation = location;
						txtLastLocation.setText(getString(R.string.location_text,lastLocation.getLatitude(),lastLocation.getLongitude(),lastLocation.getTime()));
					}else{
						txtLastLocation.setText("Lokasi tidak tersedia");
					}
				}
			});
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
}
