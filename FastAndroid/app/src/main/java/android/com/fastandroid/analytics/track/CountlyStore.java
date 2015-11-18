package android.com.fastandroid.analytics.track;

import android.com.fastandroid.utils.Log;
import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 项目名称：FastAndroid
 * 包名：android.com.fastandroid.analytics.track
 * 当前类作用：用一句话描述当前类的功能
 * 作者：longlyboyhe on 2015/11/16 11:25
 * 邮箱：longlyboyhe@126.com
 */
public class CountlyStore {
    private static final String PREFERENCES = "COUNTLY_STORE";
    private static final String DELIMITER = ":::";
    private static final String CONNECTIONS_PREFERENCE = "CONNECTIONS";
    private static final String EVENTS_PREFERENCE = "EVENTS";
    private static final String LOCATION_PREFERENCE = "LOCATION";
    private String TAG = "CountlyStore";
    private SharedPreferences preferences;

    /**
     * 初始化获取SharedPreference
     *
     * @param ctx
     */
    protected CountlyStore(Context ctx) {
        if (ctx == null) {
            throw new IllegalArgumentException("must provide valid context");
        }
        preferences = ctx.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
    }

    public String[] connections() {
        String array = preferences.getString(CONNECTIONS_PREFERENCE, null);
        return array == null || "".equals(array) ? new String[0] : array.split(DELIMITER);
    }

    public String connectionsString() {
        String array = preferences.getString(CONNECTIONS_PREFERENCE, null);
        //if (array!=null) Log.d("connections",array);
        return array;
    }

    public String[] events() {
        String array = preferences.getString(EVENTS_PREFERENCE, null);
        return array == null || "".equals(array) ? new String[0] : array.split(DELIMITER);
    }

    /**
     * 返回按时间戳排序的事件
     *
     * @return
     */
    public List<Event> eventsList() {
        String[] array = events();
        if (array.length == 0) return new ArrayList<Event>();
        else {
            List<Event> events = new ArrayList<Event>();
            for (String s : array) {
                try {
                    events.add(jsonToEvent(new JSONObject(s)));
                } catch (JSONException e) {
                    Log.e(TAG, "Cannot parse Event json", e);
                }
            }

            Collections.sort(events, new Comparator<Event>() {
                @Override
                public int compare(Event e1, Event e2) {
                    return e2.timestamp - e1.timestamp;
                }
            });

            return events;
        }
    }

    public boolean isEmptyConnections() {
        return connections().length == 0;
    }

    public boolean isEmptyEvents() {
        return events().length == 0;
    }

    /**
     * Adds a connection to the local store.
     * @param str the connection to be added, ignored if null or empty
     */
    public synchronized void addConnection(String str) {
        if (str != null && str.length() > 0){
            List<String> connections = new ArrayList<String>(Arrays.asList(connections()));
            connections.add(str);
            preferences.edit().putString(CONNECTIONS_PREFERENCE, join(connections, DELIMITER)).commit();
        }
    }

    public void removeAllConnection() {
        List<String> connections = new ArrayList<String>(Arrays.asList(connections()));
        connections.clear();
        preferences.edit().putString(CONNECTIONS_PREFERENCE, join(connections, DELIMITER)).commit();
    }

    /**
     * Removes a connection from the local store.
     * @param str the connection to be removed, ignored if null or empty,
     *            or if a matching connection cannot be found
     */
    public synchronized void removeConnection(String str) {
        if (str != null && str.length() > 0){
            List<String> connections = new ArrayList<String>(Arrays.asList(connections()));
            if(connections.remove(str)){
                preferences.edit().putString(CONNECTIONS_PREFERENCE, join(connections, DELIMITER)).commit();
            }
        }

    }

    public void addEvent(Event event) {
        List<Event> events = eventsList();
        if (!events.contains(event)) events.add(event);
        preferences.edit().putString(EVENTS_PREFERENCE, joinEvents(events, DELIMITER)).commit();
    }

    public void addEvent(String key, Map<String, String> segmentation, int count, double sum) {
        List<Event> events = eventsList();
        Event event = null;
        //判重
        for (Event e : events) if (e.key != null && e.key.equals(key)) event = e;

        //如果是新事件则新建，否则只累加 count 和 sum值
        if (event == null) {
            event = new Event();
            event.key = key;
            event.segmentation = segmentation;
            event.count = 0;
            event.sum = 0;
            event.timestamp = (int) (System.currentTimeMillis() / 1000);
        } else {
            removeEvent(event);
            event.timestamp = Math.round((event.timestamp + (System.currentTimeMillis() / 1000)) / 2);
        }

        event.count += count;
        event.sum += sum;

        addEvent(event);
    }

    public void removeEvent(Event event) {
        List<Event> events = eventsList();
        events.remove(event);
        preferences.edit().putString(EVENTS_PREFERENCE, joinEvents(events, DELIMITER)).commit();
    }

    public void removeEvents(Collection<Event> eventsToRemove) {
        List<Event> events = eventsList();
        for (Event e : eventsToRemove) events.remove(e);
        preferences.edit().putString(EVENTS_PREFERENCE, joinEvents(events, DELIMITER)).commit();
    }

    protected static JSONObject eventToJSON(Event event) {
        JSONObject json = new JSONObject();

        try {
            json.put("key", event.key);
            json.put("count", event.count);
            json.put("sum", event.sum);
            json.put("timestamp", event.timestamp);

            if (event.segmentation != null) {
                json.put("segmentation", new JSONObject(event.segmentation));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }

    /**
     * json对象组装成event
     *
     * @param json
     * @return
     */
    protected static Event jsonToEvent(JSONObject json) {
        Event event = new Event();

        try {
            event.key = json.get("key").toString();
            event.count = Integer.valueOf(json.get("count").toString());
            event.sum = Double.valueOf(json.get("sum").toString());
            event.timestamp = Integer.valueOf(json.get("timestamp").toString());

            if (json.has("segmentation")) {
                JSONObject segm = json.getJSONObject("segmentation");
                HashMap<String, String> segmentation = new HashMap<String, String>();
                Iterator nameItr = segm.keys();

                while (nameItr.hasNext()) {
                    Object obj = nameItr.next();
                    if (obj instanceof String) {
                        segmentation.put((String) obj, ((JSONObject) json.get("segmentation")).getString((String) obj));
                    }
                }

                event.segmentation = segmentation;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return event;
    }

    /**
     * 把所有event转化成一个String，这个方法是将event转换成list然后调用join
     *
     * @param collection
     * @param delimiter
     * @return
     */
    private static String joinEvents(Collection<Event> collection, String delimiter) {
        List<String> strings = new ArrayList<String>();
        for (Event e : collection) strings.add(eventToJSON(e).toString());
        return join(strings, delimiter);
    }

    /**
     * 用分隔符连接collection里的String
     *
     * @param collection
     * @param delimiter
     * @return
     */
    private static String join(Collection<String> collection, String delimiter) {
        StringBuilder builder = new StringBuilder();

        int i = 0;
        for (String s : collection) {
            builder.append(s);
            if (++i < collection.size()) builder.append(delimiter);
        }

        return builder.toString();
    }

    /**
     * Sets location of user and sends it with next request
     */
    void setLocation(final double lat, final double lon) {
        preferences.edit().putString(LOCATION_PREFERENCE, lat + "," + lon).commit();
    }

    /**
     * Get location or empty string in case if no location is specified
     */
    String getAndRemoveLocation() {
        String location = preferences.getString(LOCATION_PREFERENCE, "");
        if (!location.equals("")) {
            preferences.edit().remove(LOCATION_PREFERENCE).commit();
        }
        return location;
    }
}
