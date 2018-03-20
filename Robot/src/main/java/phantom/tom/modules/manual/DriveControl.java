package phantom.tom.modules.manual;

import com.qualcomm.robotcore.hardware.Gamepad;

import labs.vex.lumen.anemoi.Inject;
import labs.vex.lumen.anemoi.Moi;
import labs.vex.lumen.firefly.Configure;
import labs.vex.lumen.firefly.IConfigurable;
import labs.vex.lumen.ion.Ion;
import phantom.tom.Scopes;
import phantom.tom.resources.gamepad.GamepadManager;
import phantom.tom.resources.gamepad.IGamepadMonitor;
import phantom.tom.resources.hardware.drive.MecanumDrive;
import phantom.tom.resources.hardware.drive.state.MecanumState;
import phantom.tom.resources.hardware.gearbox.AbstractGearbox;
import phantom.tom.resources.hardware.gearbox.MecanumGearbox;

public class DriveControl extends Ion implements Moi, IGamepadMonitor, IConfigurable {
    @Configure(namespace = "modules.manual", config = "drive", access = "ratio")
    public static final double ratio = 1.0;

    @Inject public MecanumDrive drive;
    @Inject public MecanumGearbox gearbox;

    public DriveControl() {
        super(Scopes.MANUAL.name, 0);
    }

    @Override
    public void boot() {
        GamepadManager.register(this, new String[] {"gamepad1"});
        this.drive.init();
        this.gearbox.setDrive(this.drive);
        this.gearbox.shift(0);
    }

    @Override
    public void kill() {
        this.drive.kill();
    }

    @Override
    public void gamepadEvent(Gamepad gamepad, String name) {
        if(gamepad.left_bumper) {
            this.gearbox.shift(AbstractGearbox.DOWN);
            while(gamepad.left_bumper);
        } else if(gamepad.right_bumper) {
            this.gearbox.shift(AbstractGearbox.UP);
            while(gamepad.right_bumper);
        }

        this.drive.setState(new MecanumState(-gamepad.left_stick_y, gamepad.left_stick_x, gamepad.right_stick_x));
    }
}
