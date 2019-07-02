package com.horsehwak.mapnaver;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.overlay.PolygonOverlay;
import com.naver.maps.map.overlay.PolylineOverlay;

import java.util.Arrays;

public  class MainActivity extends FragmentActivity
    implements OnMapReadyCallback {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment) fm.findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);
    }

    @UiThread
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        CameraUpdate cameraUpdate = CameraUpdate.scrollTo(new LatLng(35.932277, 126.876428));
        cameraUpdate.zoomTo(8);
        naverMap.moveCamera(cameraUpdate);

        Marker GunsanUniv = new Marker();
        GunsanUniv.setPosition(new LatLng(35.945357, 126.682163));
        GunsanUniv.setMap(naverMap);

        Marker JeonbukUniv = new Marker();
        JeonbukUniv.setPosition(new LatLng(35.846715, 127.129386));
        JeonbukUniv.setMap(naverMap);

        Marker GunsanCityHall = new Marker();
        GunsanCityHall.setPosition(new LatLng(35.967587, 126.736843));
        GunsanCityHall.setMap(naverMap);

        Marker WonGwangUniv = new Marker();
        WonGwangUniv.setPosition(new LatLng(35.969449, 126.957322));
        WonGwangUniv.setMap(naverMap);

        InfoWindow infoWindow = new InfoWindow();

        GunsanUniv.setTag("군산대학교");
        GunsanUniv.setOnClickListener(overlay -> {
            infoWindow.open(GunsanUniv);
            return  true;
        });

        JeonbukUniv.setTag("전북대학교");
        JeonbukUniv.setOnClickListener(overlay -> {
            infoWindow.open(JeonbukUniv);
            return true;
        });

        GunsanCityHall.setTag("군산시청");
        GunsanCityHall.setOnClickListener(overlay -> {
            infoWindow.open(GunsanCityHall);
            return true;
        });

        WonGwangUniv.setTag("원광대학교");
        WonGwangUniv.setOnClickListener(overlay -> {
            infoWindow.open(WonGwangUniv);
            return true;
        });

        infoWindow.setAdapter(new InfoWindow.DefaultTextAdapter(getApplicationContext()) {
            @NonNull
            @Override
            public CharSequence getText(@NonNull InfoWindow infoWindow) {
                return (CharSequence)infoWindow.getMarker().getTag();
             }
        });
        naverMap.setOnMapClickListener((coord, point) -> {
            infoWindow.close();
        });

        PolylineOverlay polyline = new PolylineOverlay();
        polyline.setCoords(Arrays.asList(
                new LatLng(35.945357, 126.682163),
                new LatLng(35.967587, 126.736843),
                new LatLng(35.969449, 126.957322),
                new LatLng(35.846715, 127.129386)
        ));
        polyline.setMap(naverMap);
        polyline.setWidth(10);
    }
}

