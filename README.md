# 📝 Android Praktikum 09 - Location


## 🎺 Hasil
!['hehehe'](./gif/TIO6_15.gif)


## Praktikum 9.2

```java
mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
	@Override
		public void onSuccess(Location location) {
			if (location != null){
				lastLocation = location;
				txtLastLocation.setText(getString(R.string.location_text,lastLocation.getLatitude(),lastLocation.getLongitude(),lastLocation.getTime()));
				new AddressAsycTask(MainActivity.this,MainActivity.this).execute(location);
			}else{
				txtLastLocation.setText("Lokasi tidak tersedia");
			}
		}
	});
```

## Menyiapkan beberapa variabel

```java
	Geocoder geocoder = new Geocod (mContext,Locale.getDefault());
	Location location = locations[0];
	String resultMessage = "";
	List<Address> addresses = null;
```

Kemudian melakukan try catch untuk menghandle sebuah exception

```java
try{

}catch (IOException e){
}catch (IllegalArgumentException ilegal){

}

```

Geolocation to lattitude logitude

```java
addresses = geocoder.getFromLocation(
	location.getLatitude(),
	location.getLongitude(),1);
```

Kemudian simpan dalam bentuk array
```java
Address address = addresses.get(0);
ArrayList<String> listAddress = new ArrayList<>();

for (int i=0; i<= address.getMaxAddressLineIndex(); i++){
	listAddress.add(address.getAddressList(i));
}

    resultMessage = TextUtils.join("\n", listAddress);
```

### Praktikum 9.4

Menampilkan data secara periodik

```java
mFusedLocationClient.requestLocationUpdates(getLocationRequest(),mLocationCallback,null);
```

## 🌝 Penyusun
- Ivan Fadila Putra - TO06_15

## 📦 File Pendukung
- [Sample GPX File](./gif/run.gpx)