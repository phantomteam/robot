package phantom.tom.resources.hardware.drive.state;

public class AbstractState {
    public static int FIRST = 0;
    public static int SECOND = 1;

    private double[] values = new double[2];

    public AbstractState(double[] values) {
        this.setValues(values);
    }

    public void setValues(double[] values) {
        this.values = values;
    }

    public double[] getValues() {
        return this.values;
    }

    public double getValue(int index) {
        return this.getValues()[index];
    }
}
