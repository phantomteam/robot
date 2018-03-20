package phantom.tom.resources.gamepad;

import com.qualcomm.robotcore.hardware.Gamepad;

public interface IGamepadMonitor {
    void gamepadEvent(Gamepad gamepad, String name);
}
