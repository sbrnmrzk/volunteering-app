package com.example.volunteeringapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsFragment extends Fragment {
    GoogleMap map;
    String lat;
    String lat2;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            lat = Double.toString(3.1390);
            lat2 = Double.toString(101.68689);
            LatLng kl = new LatLng(3.1390, 101.6869);
            map = googleMap;
            map.addMarker(new MarkerOptions().position(kl).title("Marker in KL"));
            map.moveCamera(CameraUpdateFactory.newLatLng(kl));
        }

    };

    public String latGet() {
        return lat;
    }

    public String longGet() {
        return lat2;
    }

    public void locationSearch(String address) {

        List<Address> addressList = null;
        MarkerOptions userMarkerOptions = new MarkerOptions();
        float zoomLevel = 16.0f; //This goes up to 21

        if (!TextUtils.isEmpty(address)) {
            Geocoder geocoder = new Geocoder(getContext());
            try {
                addressList = geocoder.getFromLocationName(address, 6);
                if (addressList != null) {
                    for (int i = 0; i<addressList.size(); i++) {
                        Address userAddress = addressList.get(i);
                        LatLng latLng = new LatLng(userAddress.getLatitude(), userAddress.getLongitude());

                        userMarkerOptions.position(latLng);
                        userMarkerOptions.title(address);
                        userMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

                        map.clear();
                        map.addMarker(userMarkerOptions);
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
                    }
                }
            }
            catch (IOException e){
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getContext(), "Please write location name...", Toast.LENGTH_SHORT).show();
        }

//        Toast.makeText(getContext(), address, Toast.LENGTH_SHORT).show();
//        LatLng indo = new LatLng(0.7893, 113.9213);
//        float zoomLevel = 8.0f; //This goes up to 21


    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}