package phantom.tom.services;

import labs.vex.lumen.ion.service.AbstractService;
import labs.vex.lumen.ion.service.ServicePacket;

public class WatchService extends AbstractService {
    public WatchService() {
        super("WatchRuntime");
    }

    @Override
    public ServicePacket init() {
        return null;
    }

    @Override
    public ServicePacket killed() {
        return null;
    }

    @Override
    public boolean tick() {
        if(!phantom.tom.State.currentOpMode.opModeIsActive()) {
            this.kill();
            return false;
        }
        return true;
    }
}
