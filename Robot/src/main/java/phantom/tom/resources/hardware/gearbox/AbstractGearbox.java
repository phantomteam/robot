package phantom.tom.resources.hardware.gearbox;


public abstract class AbstractGearbox implements IGearbox{
    public static final int UP   =  1;
    public static final int DOWN = -1;

    private int gear = 0;
    private int maximum;

    public AbstractGearbox(int maximum) {
        this.maximum = maximum;
    }

    public abstract void change();

    @Override
    public void shift(int gear) {
        this.gear += gear;
        if(this.gear < 1)
            this.gear = 1;
        this.gear = Math.min(this.gear, this.maximum);

        this.change();
    }

    @Override
    public double conversion() {
        return this.gear / this.maximum;
    }

    @Override
    public int gear() {
        return this.gear;
    }

    @Override
    public void gear(int gear) {
        this.gear = gear;
    }
}
