package phantom.tom.resources.hardware.drive.state;

public class SpeedState extends AbstractState {
    public SpeedState(double speedLeft, double speedRight) {
        super(new double[] {speedLeft, speedRight});
    }
}
