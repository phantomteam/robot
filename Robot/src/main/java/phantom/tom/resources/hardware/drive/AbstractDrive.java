package phantom.tom.resources.hardware.drive;

import phantom.tom.resources.hardware.drive.state.AbstractState;
import phantom.tom.resources.hardware.drive.state.StopState;

public abstract class AbstractDrive implements IDrive {

    private boolean alive = true;
    private AbstractState currentState = null;

    public void kill() {
        this.setState(new StopState());
        this.alive = false;
    }

    public void revive() {
        this.alive = true;
    }

    public abstract void init();
    protected abstract void state(AbstractState state);

    public void setState(AbstractState state) {
        if(alive) {
            this.currentState = state;
            this.state(state);
        }
    }

    public AbstractState getState() {
        return this.currentState;
    }
}
