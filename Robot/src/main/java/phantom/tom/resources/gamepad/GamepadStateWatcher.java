package phantom.tom.resources.gamepad;

import com.qualcomm.robotcore.hardware.Gamepad;

public class GamepadStateWatcher implements Gamepad.GamepadCallback {
    private String name;
    public GamepadStateWatcher(String name) {
        this.name = name;
    }

    @Override
    public void gamepadChanged(Gamepad gamepad) {
        GamepadManager.notifyChange(gamepad, this.name);
    }
}
