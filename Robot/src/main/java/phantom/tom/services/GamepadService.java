package phantom.tom.services;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.Gamepad;

import java.util.HashMap;
import java.util.Map;

import labs.vex.lumen.ion.service.AbstractService;
import labs.vex.lumen.ion.service.ServicePacket;
import phantom.tom.State;
import phantom.tom.resources.gamepad.GamepadManager;

public class GamepadService extends AbstractService {
    private Map<String, byte[]> cache = new HashMap<>();
    private Map<String, Gamepad> stage = new HashMap<>();

    public GamepadService() {
        super("GamepadRuntime");
    }

    private void setState() {
        try {
            for(Map.Entry<String, Gamepad> entry: this.stage.entrySet()) {
                this.cache.put(entry.getKey(), entry.getValue().toByteArray());
            }
        } catch (RobotCoreException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ServicePacket init() {
        this.stage.put("gamepad1", phantom.tom.State.currentOpMode.gamepad1);
        this.stage.put("gamepad2", phantom.tom.State.currentOpMode.gamepad2);

        this.setState();
        return null;
    }

    @Override
    public ServicePacket killed() {
        return null;
    }

    @Override
    public boolean tick() {
        for(Map.Entry<String, byte[]> entry: this.cache.entrySet()) {
            try {
                String name = entry.getKey();
                byte[] gamepad = entry.getValue();
                byte[] compare = this.stage.get(name).toByteArray();

                if(gamepad.length != compare.length)
                    continue;

                boolean isEqual = true;
                for(int index = 0; index < compare.length; index++) {
                    if(gamepad[index] != compare[index]) {
                        isEqual = false;
                        break;
                    }
                }

                if(!isEqual) {
                    GamepadManager.notifyChange(this.stage.get(name), name);
                }
            } catch (RobotCoreException e) {
                e.printStackTrace();
            }
        }
        this.setState();
        return true;
    }
}
