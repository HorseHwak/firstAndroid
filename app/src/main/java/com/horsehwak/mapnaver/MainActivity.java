package com.horsehwak.mapnaver;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.geometry.LatLngBounds;
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

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Arrays;

import static android.media.CamcorderProfile.get;


public  class MainActivity extends FragmentActivity
    implements OnMapReadyCallback {
    boolean setValue = false;
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
        //각 장소별 경위도
        ArrayList<LatLng> Location  = new ArrayList<LatLng>();
        ArrayList<Marker> PlaceName = new ArrayList<Marker>();
        ArrayList<String> MarkerTag = new ArrayList<String>();


        Location.add(new LatLng(35.945357, 126.682163));
        Location.add(new LatLng(35.846715, 127.129386));
        Location.add(new LatLng(35.967587, 126.736843));
        Location.add(new LatLng(35.969449, 126.957322));

        MarkerTag.add("군산대학교");
        MarkerTag.add("전북대학교");
        MarkerTag.add("군산시청");
        MarkerTag.add("원광대학교");

        LatLngBounds latLngBounds = new LatLngBounds(Location.get(0), Location.get(1));

        //중간값 화면 잡기
        CameraUpdate cameraUpdate = CameraUpdate.fitBounds(latLngBounds);
        naverMap.moveCamera(cameraUpdate);

        //마커 찍기
        for(int i = 0; i < Location.size(); i++){
            Marker marker = new Marker();
            marker.setPosition(Location.get(i));
            PlaceName.add(marker);
        }
        for (Marker marker : PlaceName){
            marker.setMap(naverMap);
        }

        //정보창 객체 생성
        InfoWindow infoWindow = new InfoWindow();

        //마커와 정보창 연결
        for (int i = 0; i < Location.size(); i++){
            PlaceName.get(i).setTag(MarkerTag.get(i));
            int finalI = i;
            PlaceName.get(i).setOnClickListener(overlay -> {
                infoWindow.open(PlaceName.get(finalI));
                return true;
            });
        }

        infoWindow.setAdapter(new InfoWindow.DefaultTextAdapter(getApplicationContext()) {
            @NonNull
            @Override
            public CharSequence getText(@NonNull InfoWindow infoWindow) {
                return (CharSequence)infoWindow.getMarker().getTag();
             }
        });

        // 네이버맵 클릭 시  정보창 닫음
        naverMap.setOnMapClickListener((coord, point) -> {
            infoWindow.close();
        });

        // 경로표시
        PolylineOverlay polyline = new PolylineOverlay();
        polyline.setCoords(Arrays.asList(
               Location.get(0),Location.get(1),Location.get(2),Location.get(3)
        ));

        polyline.setMap(naverMap);
        polyline.setWidth(10);

        // 버튼클릭으로 정보창 및 경로 ON/OFF
        Button btn = (Button)findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (setValue == true) {
                    for(int i = 0 ; i < Location.size(); i++){
                        PlaceName.get(i).setMap(naverMap);
                    }
                    polyline.setMap(naverMap);

                    setValue = false;
                }
                else if (setValue == false) {
                    for(int i = 0 ; i < Location.size(); i++){
                        PlaceName.get(i).setMap(null);
                    }
                    polyline.setMap(null);

                    setValue = true;
                }
            }

        });
        //클릭시 경위도표시
        naverMap.setOnMapLongClickListener((point, coord) -> {
            Toast.makeText(getApplicationContext(), "위도: " + coord.latitude + ", 경도: " + coord.longitude, Toast.LENGTH_SHORT).show();
        });
    }
}

