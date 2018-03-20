package phantom.tom.resources.hardware.drive;

import phantom.tom.resources.hardware.drive.state.AbstractState;

public interface IDrive {
    void init();
    void kill();
    void revive();
    void setState(AbstractState state);
}
