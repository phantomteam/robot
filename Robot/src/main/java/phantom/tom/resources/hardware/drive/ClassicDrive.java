package phantom.tom.resources.hardware.drive;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import labs.vex.lumen.anemoi.Dose;
import labs.vex.lumen.hardware.HardwareProcessor;
import phantom.tom.resources.hardware.drive.state.AbstractState;

@Dose
public class ClassicDrive extends AbstractDrive {
    private DcMotor left;
    private DcMotor right;

    @Override
    public void init() {
        this.left = HardwareProcessor.<DcMotor>get("drive.left");
        this.right = HardwareProcessor.<DcMotor>get("drive.right");

        this.left.setDirection(DcMotorSimple.Direction.FORWARD);
        this.right.setDirection(DcMotorSimple.Direction.REVERSE);

        this.left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    @Override
    public void state(AbstractState state) {
        double left = state.getValue(AbstractState.FIRST);
        double right = state.getValue(AbstractState.SECOND);

        this.left.setPower(left);
        this.right.setPower(right);
    }
}
