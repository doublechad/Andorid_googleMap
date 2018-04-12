package tw.org.iii.fsit04;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback ,OnStreetViewPanoramaReadyCallback {

    private GoogleMap mMap;
    private ArrayList<WayPoint> wayPoints;
    private ArrayList<HashMap<String,ArrayList<LatLng>>> ways;
//    private MapHandler mapHandler;
    private final PatternItem DOT = new Dot();
    private final PatternItem DASH = new Dash(10.0f);
    private final PatternItem GAP = new Gap(10.0f);
    private final List<PatternItem> PATTERN_POLYGON_ALPHA = Arrays.asList(GAP, DOT);
    private Marker[] marker;
    private HashMap<String,Object>[] destinations;
    private StreetViewPanorama streetViewPanorama;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        StreetViewPanoramaFragment streetViewPanoramaFragment =
                (StreetViewPanoramaFragment) getFragmentManager()
                        .findFragmentById(R.id.streetviewpanorama);
        streetViewPanoramaFragment.getStreetViewPanoramaAsync(this);


        wayPoints = new ArrayList<>();
//        mapHandler= new MapHandler();
        ways =new ArrayList<>();
        HashMap<String,Object> m1 = new HashMap<>();
        m1.put("lat",new LatLng(25.047155,121.514465));
        m1.put("postion","臺北");
        HashMap<String,Object> m2 = new HashMap<>();
        m2.put("lat",new LatLng(25.171855,121.440422));
        m2.put("postion","淡水");
        destinations = new HashMap[]{m1, m2};
        marker = new Marker[destinations.length];

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng taipei = new LatLng(25.047155, 121.514465);
//        LatLng sea =new LatLng(25.171855,121.440422);

//        MarkerOptions m1 =new MarkerOptions().position(taipei).title("Marker in taipei")
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow));
//        marker =mMap.addMarker(m1);
        LatLng ds =new LatLng(25.171855,121.440422);
        for(HashMap<String,Object> hm : destinations){
            MarkerOptions m2 =new MarkerOptions().position((LatLng) hm.get("lat")).title(hm.get("postion").toString());
            mMap.addMarker(m2).showInfoWindow();

        }
        GroundOverlayOptions newarkMap = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.ds))
                .position(ds, 8600f, 6500f);
        mMap.addGroundOverlay(newarkMap);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(taipei));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(10.0f));
        mMap.setOnMarkerClickListener(new MyOnMarkerClickListener());

    }
    //街景回傳
    @Override
    public void onStreetViewPanoramaReady(StreetViewPanorama streetViewPanorama) {
        this.streetViewPanorama=streetViewPanorama;
        streetViewPanorama.setPosition(new LatLng(25.137077, 121.508447));
    }

    public void test2(View view) {


    }
    private class MyOnMarkerClickListener implements GoogleMap.OnMarkerClickListener{

        @Override
        public boolean onMarkerClick(Marker marker) {
            LatLng l1 =marker.getPosition();
            streetViewPanorama.setPosition(l1);
            Log.v("chad",l1.latitude+" : "+l1.longitude);
            return false;
        }
    }
//    //向URL提出計算路徑的請求
//    public void test(View view) {
//        new Thread(){
//            @Override
//            public void run() {
//                StringBuffer result= new StringBuffer();
//                try {
//                    URL url = new URL("https://maps.googleapis.com/maps/api/directions/json?&mode=transit&origin=台北車站&destination=101大樓&language=zh-TW&key=AIzaSyCGgxfNALIYB5ngyxEQuvfIswpw96yiA8o");
//                    URLConnection conn = url.openConnection();
//                    conn.connect();
//                    InputStream in =conn.getInputStream();
//                    byte[] buff =new byte[1024*1024];
//                    int size=-1;
//                    while((size=in.read(buff))!=-1){
//                        result.append(new String(buff,0,size));
//                    }
//                }catch(Exception e){
//                    Log.v("chad",e.toString());
//                }
//                try {
//                    JSONObject all =new JSONObject(result.toString());
//                    JSONArray array =all.getJSONArray("routes");
//                    JSONObject routes =(JSONObject)array.get(0);
//                    JSONArray legs =routes.getJSONArray("legs");
//                    for(int i=0;i<legs.length();i++){
//                        JSONArray steps1 =legs.getJSONObject(i).getJSONArray("steps");
//                        for(int x=0;x<steps1.length();x++){
//                            Object t1 =steps1.getJSONObject(x).get("html_instructions");
//                            JSONObject t2 =steps1.getJSONObject(x).getJSONObject("start_location");
//                            String polyline =steps1.getJSONObject(x).getJSONObject("polyline").getString("points");
//                            ArrayList<LatLng> thePoly =(ArrayList)decodePoly(polyline);
//                            JSONArray steps2 =steps1.getJSONObject(x).optJSONArray("steps");
//                            //步行才有第2個steps
//                            if(steps2!=null) {
//                                for (int y = 0; y < steps2.length(); y++) {
//                                    Object tt1 = steps2.getJSONObject(y).get("html_instructions");
//                                    JSONObject tt2 = steps2.getJSONObject(y).getJSONObject("start_location");
////                                    Log.v("chad", "html = " + tt1.toString() + " Lat :lng = " + tt2.toString());
//                                    WayPoint wp = new WayPoint(tt2.getDouble("lat"),tt2.getDouble("lng"),tt1.toString(),"");
//                                    wayPoints.add(wp);
//                                }
//                                HashMap<String, ArrayList<LatLng>> map =new HashMap<>();
//                                map.put("walks",thePoly);
//                                ways.add(map);
//                            }else{
//                                WayPoint wp = new WayPoint(t2.getDouble("lat"),t2.getDouble("lng"),t1.toString(),"");
//                                wayPoints.add(wp);
//                                HashMap<String, ArrayList<LatLng>> map =new HashMap<>();
//                                map.put("transit",thePoly);
//                                ways.add(map);
//                            }
//                        }
//                    }
//
//                } catch (JSONException e1) {
//                    Log.v("chad",e1.toString());
//                }
//                mapHandler.sendEmptyMessage(0);
//            }
//
//        }.start();
//
//    }
//    //路徑解碼器
//    private List<LatLng> decodePoly(String encoded) {
//        List<LatLng> poly = new ArrayList<LatLng>();
//        int index = 0, len = encoded.length();
//        int lat = 0, lng = 0;
//
//        while (index < len) {
//            int b, shift = 0, result = 0;
//            do {
//                b = encoded.charAt(index++) - 63;
//                result |= (b & 0x1f) << shift;
//                shift += 5;
//            } while (b >= 0x20);
//            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
//            lat += dlat;
//
//            shift = 0;
//            result = 0;
//            do {
//                b = encoded.charAt(index++) - 63;
//                result |= (b & 0x1f) << shift;
//                shift += 5;
//            } while (b >= 0x20);
//            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
//            lng += dlng;
//
//            LatLng p = new LatLng((((double) lat / 1E5)),
//                    (((double) lng / 1E5)));
//            poly.add(p);
//        }
//
//        return poly;
//    }
//    //畫路線在MAP上
//    private void drawRoute(){
//        if(ways.size()>0){
//            for(HashMap<String,ArrayList<LatLng>> maps :ways) {
//                if(maps.containsKey("walks")) {
//                    //走路畫點
//                    ArrayList<LatLng> wp = maps.get("walks");
//                    PolylineOptions rectOptions = new PolylineOptions();
//                    for (LatLng point : wp) {
//                        rectOptions.add(point);
//                    }
//                    Polyline polyline = mMap.addPolyline(rectOptions);
//                    polyline.setWidth(10.0f);
//                    polyline.setColor(Color.BLUE);
//                    polyline.setPattern(PATTERN_POLYGON_ALPHA);
//                }else{
//                    //運輸工具不畫點
//                    ArrayList<LatLng> wp = maps.get("transit");
//                    PolylineOptions rectOptions = new PolylineOptions();
//                    for (LatLng point : wp) {
//                        rectOptions.add(point);
//                    }
//                    Polyline polyline = mMap.addPolyline(rectOptions);
//                    polyline.setWidth(10.0f);
//                    polyline.setColor(Color.BLUE);
//                }
//            }
//
//        }else{
//            Log.v("chad","list=null");
//        }
//
//
//    }
//    private void drawMark(){
//        Log.v("chad","ok");
//        for(WayPoint point :wayPoints){
////            Log.v("chad",point.lat+" : "+point.lng+ " : "+point.notify);
//            MarkerOptions markerOptions = new MarkerOptions();
//            LatLng latLng =new LatLng(point.lat,point.lng);
//            markerOptions.position(latLng);
//            String text =point.notify.replace("<b>","").replace("</b>","")
//                    .replace("<div style=\"font-size:0.9em\">", "").replace("</div>","")
//                    .replace("<span class=\"location\">", "").replace("</span>","");
//            markerOptions.title(text);
//            markerOptions.snippet("test");
//            mMap.addMarker(markerOptions);
//        }
//    }
//    private class MapHandler extends Handler{
//        @Override
//        public void handleMessage(Message msg) {
//            drawRoute();
//            drawMark();
//            mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 1000, null);
////            marker.setRotation(90.0f);
//        }
//    }
}
class WayPoint{
    String notify;
    Double lat;
    Double lng;
    String time;
    WayPoint(Double lat,Double lng,String notify,String time){
        this.time =time;
        this.notify=notify;
        this.lat=lat;
        this.lng=lng;
    }
}