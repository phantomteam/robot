package phantom.tom.resources.gamepad;

import com.qualcomm.robotcore.hardware.Gamepad;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import phantom.tom.State;

public class GamepadManager {

    private static Map<String, ArrayList<IGamepadMonitor>> monitors = new HashMap<>();

    public static void notifyChange(Gamepad gamepad, String name) {
        List<IGamepadMonitor> ms = monitors.get(name);
        if(ms == null)
            return;
        for(IGamepadMonitor m: ms){
            m.gamepadEvent(gamepad, name);
        }
    }

    public static void register(IGamepadMonitor monitor, String[] scopes) {
        for(String scope: scopes){
            ArrayList<IGamepadMonitor> ms = monitors.get(scope);
            if(ms == null)
                ms = new ArrayList<>();
            ms.add(monitor);
            monitors.put(scope, ms);
        }
    }
}
