package phantom.tom.modules.autonomous;

import labs.vex.lumen.anemoi.Moi;
import labs.vex.lumen.ion.Ion;
import phantom.tom.Scopes;

public class TestAutonomous extends Ion implements Moi {
    public TestAutonomous() {
        super(Scopes.AUTONOMOUS.name, 1);
    }
}
