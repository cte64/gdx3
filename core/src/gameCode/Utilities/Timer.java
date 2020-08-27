package gameCode.Utilities;

import java.util.HashMap;

public class Timer {

    HashMap<String, Float> timers;

    public Timer() {
        timers = new HashMap<String, Float>();
    }

    public void addTimer(String newTimer) {
        timers.put(newTimer, 0.0f);
    }

    public void update(String timerKey, float timeBtw) {
        if(timers.containsKey(timerKey))
            timers.put(timerKey, timers.get(timerKey) + timeBtw);
    }

    public void update(float timeBtw){
        for(String key: timers.keySet()) {
            timers.put(key, timers.get(key) + timeBtw);
        }
    }

    public float getTime(String key) {
        if(timers.containsKey(key)) return timers.get(key);
        else return 0;
    }

    public void resetTimer(String key) {
        if(timers.containsKey(key))
            timers.put(key, 0.0f);
    }
}
