package com.example.antiaedes;

import android.Manifest;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.antiaedes.dao.DenunciaDao;
import com.example.antiaedes.entities.Denuncia;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MapActivity extends FragmentActivity {

    private Session mSession;
    private ArrayList<InfoMarker> markers;
    private GoogleMap map;
    private OnInfoWindowElemTouchListener infoButtonListener;
    private TextView tvFoco;
    private TextView tvFocoR;
    private TextView tvSuspect;
    private TextView tvSuspectR;
    private Button details;
    private ViewGroup infoWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        if (getIntent().hasExtra("session"))
            mSession = (Session) getIntent().getSerializableExtra("session");

        infoWindow = (ViewGroup) getLayoutInflater().inflate(R.layout.windowmaplayout, null);
        tvFoco = (TextView) infoWindow.findViewById(R.id.tvFoco);
        tvFocoR = (TextView) infoWindow.findViewById(R.id.tvFocosResolved);

        tvSuspect = (TextView) infoWindow.findViewById(R.id.tvSuspicion);
        tvSuspectR = (TextView) infoWindow.findViewById(R.id.tvSuspicionResolved);

        details = (Button) infoWindow.findViewById(R.id.btOpenDetails);

        if (mSession != null) {
            if (mSession.getCpf() == null) {
                details.setEnabled(false);
                details.setVisibility(View.INVISIBLE);
            }
        } else {
            details.setEnabled(false);
            details.setVisibility(View.INVISIBLE);
        }


        markers = new ArrayList<>();

        //MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        //mapFragment.getMapAsync(this);


        if (android.os.Build.VERSION.SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        if (!(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED))
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        ActionBar actionBar = this.getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

        DenunciaDao denunciaDao = new DenunciaDao();
        final MapWrapperLayout mapWrapperLayout = (MapWrapperLayout) findViewById(R.id.map_relative_layout);

        mapWrapperLayout.init(map, getPixelsFromDp(this, 39 + 20));
        mapWrapperLayout.setMinimumWidth(200);

        ArrayList<Denuncia> denuncias = denunciaDao.getAllDenunciation();
        ArrayList<Integer> percorrido = new ArrayList<>();
        if (denuncias != null) {
            int x = 0;
            InfoMarker marker;
            for (Denuncia denuncia : denuncias) {
                if (!percorrido.contains(denuncia.getId())) {
                    String latitude = denuncia.getLatitude();
                    String longitude = denuncia.getLongitude();
                    LatLng latLngDen = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                    marker = new InfoMarker(denunciaDao.getAllDenunciationsActives());
                    markers.add(marker);
                    x++;
                    marker.addDenunciation(denuncia);
                    percorrido.add(denuncia.getId());
                    for (Denuncia den : denuncias) {
                        if (!percorrido.contains(den.getId())) {
                            LatLng latLng = new LatLng(Double.parseDouble(den.getLatitude()), Double.parseDouble(den.getLongitude()));
                            if (CalculationByDistance(latLngDen, latLng) < 7) {
                                if (!marker.containsDenunciation(den)) {
                                    marker.addDenunciation(den);
                                    percorrido.add(den.getId());
                                }
                            }
                        }
                    }

                }
            }
            for (InfoMarker infoMarker : markers) {
                map.addMarker(new MarkerOptions().position(infoMarker.getLatLng()).title(infoMarker.getId() + ""));
            }
        }

        infoButtonListener = new OnInfoWindowElemTouchListener(details, null, null) {
            @Override
            protected void onClickConfirmed(View v, Marker marker) {
                InfoMarker im = searchMarker(Integer.parseInt(marker.getTitle()));
                if (im != null) {
                    Intent intent = new Intent(v.getContext(), ListDenunciationsActivity.class);
                    intent.putExtra("denunciations", im);
                    intent.putExtra("session", mSession);
                    startActivity(intent);
                }
            }
        };
        details.setOnTouchListener(infoButtonListener);
        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                if (marker.getTitle() != null) {
                    InfoMarker infoMarker = searchMarker(Integer.parseInt(marker.getTitle()));
                    if (infoMarker != null) {
                        tvFoco.setText(infoMarker.getFocus_den() + "");
                        tvFocoR.setText(infoMarker.getFocus_res() + "");
                        tvSuspect.setText(infoMarker.getSuspicion_den() + "");
                        tvSuspectR.setText(infoMarker.getSuspicion_res() + "");
                        infoButtonListener.setMarker(marker);
                    }
                }
                mapWrapperLayout.setMarkerWithInfoWindow(marker, infoWindow);
                return infoWindow;
            }
        });
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng arg0) {
                // Clears any existing markers from the GoogleMap
                // Creating an instance of MarkerOptions to set position
                MarkerOptions markerOptions = new MarkerOptions();
                // Setting position on the MarkerOptions
                markerOptions.position(arg0);

                // Animating to the currently touched position
                map.animateCamera(CameraUpdateFactory.newLatLng(arg0));

                // Adding marker on the GoogleMap
                Marker marker = map.addMarker(markerOptions);

                // Showing InfoWindow on the GoogleMap
                marker.showInfoWindow();

                }
        });
        /**googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
        @Override public void onMapClick(LatLng latLng) {
        googleMap.clear();

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        Marker marker = googleMap.addMarker(markerOptions);
        marker.showInfoWindow();
        }
        });**/
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        GPSLocation gps = new GPSLocation(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED)
            ;
        else
            ActivityCompat.requestPermissions(MapActivity.this, new String[]{Manifest.permission.INTERNET}, 1);
        if (locationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER))
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gps);
        if (locationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER))
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, gps);

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gps.getLatitude(), gps.getLongitude()), 10.0f));
        //googleMap.addMarker(new MarkerOptions().position(new LatLng(gps.getLatitude(), gps.getLongitude())).snippet("Alerta de Dengue").title("gogogo")).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.iconemosquito));;

    }

    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        return kmInDec;
    }

    public InfoMarker searchMarker(int id) {
        if (markers != null) {
            for (InfoMarker info : markers) {
                if (info.getId() == id)
                    return info;
            }
        }
        return null;
    }

    public static int getPixelsFromDp(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
