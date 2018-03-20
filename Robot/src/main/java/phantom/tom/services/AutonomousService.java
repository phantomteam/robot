package phantom.tom.services;

import labs.vex.lumen.ion.IonException;
import labs.vex.lumen.ion.IonManager;
import labs.vex.lumen.ion.service.AbstractService;
import labs.vex.lumen.ion.service.ServicePacket;
import phantom.tom.Scopes;

public class AutonomousService extends AbstractService {
    public AutonomousService() {
        super("AutonomousRuntime");
    }

    @Override
    public ServicePacket init() {
        try {
            IonManager.bootAccessor(Scopes.AUTONOMOUS.name);
        } catch (IonException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ServicePacket killed() {
        try {
            IonManager.killAccessor(Scopes.AUTONOMOUS.name);
        } catch (IonException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean tick() {
        try {
            IonManager.tickAccessor(Scopes.AUTONOMOUS.name);
        } catch (IonException e) {
            e.printStackTrace();
        }
        this.kill();
        return false;
    }
}
