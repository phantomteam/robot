package phantom.tom;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.util.ArrayList;

import labs.vex.lumen.ion.time.TimeThread;

public class State {
    public static TeamColor teamColor;
    public static LinearOpMode currentOpMode;
    public static TimeThread time;
    public static ArrayList<Event> events;

    public static void reset() {
        events = new ArrayList();
    }

    public static void pushEvent(Event e) {
        events.add(e);
    }

    public static boolean hasEvent(Event e) {
        for(Event event: events) {
            if(event == e) {
                return true;
            }
        }
        return false;
    }
}
