package phantom.tom.defaults;

import labs.vex.lumen.hardware.Hardware;
import labs.vex.lumen.hardware.IHardwareHandler;
import phantom.tom.State;

public class HardwareHandler implements IHardwareHandler {
    @Override
    public Object handle(Hardware hardware) {
        return State.currentOpMode.hardwareMap.get(hardware.accessor);
    }
}
