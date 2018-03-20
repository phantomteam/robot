package phantom.tom.resources.hardware.drive.state;

public class MecanumState extends AbstractState {
    public static final int Y1 = 0;
    public static final int X1 = 1;
    public static final int X2 = 2;

    public MecanumState(double y1, double x1, double x2) {
        super(new double[] {y1, x1, x2});
    }
}
