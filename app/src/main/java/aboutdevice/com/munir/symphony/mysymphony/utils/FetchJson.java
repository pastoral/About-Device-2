package aboutdevice.com.munir.symphony.mysymphony.utils;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import aboutdevice.com.munir.symphony.mysymphony.R;

/**
 * Created by munirul.hoque on 11/15/2016.
 */

public class FetchJson {
    private Map<String,String> featureMap  = new HashMap<String,String>();
    private Map <String,String> modelMap = new HashMap<String, String>();
    private ArrayList<String> featureList = new ArrayList<String>();
    public ArrayList<String> featureNameList = new ArrayList<String>();
    private JSONObject jObject;
    private int [] allIcons = {R.mipmap.processor, R.mipmap.camera, R.mipmap.display, R.mipmap.os,
            R.mipmap.data_connection, R.mipmap.connectivity,R.mipmap.battery,
            R.mipmap.sim, R.mipmap.sensor,R.mipmap.others};
    //private InputStream is ;
    private Context context;
    public FetchJson(Context context){
        this.context = context;

    }
    public FetchJson(){

    }
    public String readJSONFromAsset() {
        String json = null;
        try {
            InputStream is = context.getResources().openRawResource(R.raw.features); //Context.get.open("features.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public void jsonToMap(String jsonString) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();
        jObject = new JSONObject(jsonString);

        Iterator<String> keysItr = jObject.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            String value = String.valueOf(jObject.get(key));
            featureMap.put(key,value);
        }

    }

    public ArrayList<String> getMapData(String model) throws JSONException {
        String jsonString = String.valueOf(featureMap.get(model));
        JSONObject jOj = new JSONObject(jsonString);
        Iterator<String> keysItr = jOj.keys();
        while (keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = jOj.get(key);
            featureList.add(String.valueOf(value));
            featureNameList.add(key);
        }
        return featureList;
    }

    public String retriveModelinfo(List list){
        String temp = " ";
        for(int i = 0; i<list.size(); i++){
            temp = temp + list.get(i);
        }
        return temp;
    }

    /*  public static Map<String, String> jsonToMap(JSONObject json) throws JSONException {
        Map<String, String> retMap = new Hashtable<>() ;

        if(json != JSONObject.NULL) {
            retMap = toMap(json);
        }
        return retMap;
    }

    public static Map<String, String> toMap(JSONObject object) throws JSONException {
        Map<String, String> map = new HashMap<String, String>();

        Iterator<String> keysItr = object.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, String.valueOf(value));
        }
        return map;
    }

    public static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for(int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }*/

    public boolean searchModelName (String modelName){
        boolean modelFound = false;
        if(featureMap.containsKey(modelName)){
            modelFound = true;
        }

        return modelFound;
    }

    public LinkedHashMap<Integer,String> createFeatureMap(List list ){
        LinkedHashMap<Integer,String> linkedHashMap = new LinkedHashMap<>();
        for(int i = 0; i<list.size(); i++){
            linkedHashMap.put(allIcons[i],String.valueOf(list.get(i)));
        }

        return linkedHashMap;
    }
}
