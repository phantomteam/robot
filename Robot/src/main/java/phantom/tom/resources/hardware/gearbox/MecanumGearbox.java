package phantom.tom.resources.hardware.gearbox;

import labs.vex.lumen.anemoi.Dose;
import phantom.tom.resources.hardware.drive.MecanumDrive;

@Dose
public class MecanumGearbox extends AbstractGearbox {
    private MecanumDrive drive;

    public MecanumGearbox() {
        super(4);
    }

    public void setDrive(MecanumDrive drive) {
        this.drive = drive;
    }

    @Override
    public void change() {
        this.drive.setRatio(this.conversion());
    }
}
