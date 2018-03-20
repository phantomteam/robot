package phantom.tom.resources.hardware.drive;

import android.util.Log;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import labs.vex.lumen.anemoi.Dose;
import labs.vex.lumen.hardware.HardwareProcessor;
import phantom.tom.resources.hardware.drive.state.AbstractState;
import phantom.tom.resources.hardware.drive.state.MecanumState;

@Dose
public class MecanumDrive extends AbstractDrive {
    private static final int FRONT = 1;
    private static final int BACK = 2;
    private static final int LEFT = 4;
    private static final int RIGHT = 8;

    private DcMotor[] motors = new DcMotor[12];

    private double ratio = 1.0;

    public DcMotor getMotor(int combo) {
        Log.d("combo", "getMotor: " + combo);
        return this.motors[combo];
    }

    @Override
    public void init() {
        this.motors[FRONT | LEFT] = HardwareProcessor.get("drive.front_left");
        this.motors[FRONT | RIGHT] = HardwareProcessor.get("drive.front_right");
        this.motors[BACK | LEFT] = HardwareProcessor.get("drive.back_left");
        this.motors[BACK | RIGHT] = HardwareProcessor.get("drive.back_right");

        int step = 0;
        for (int index = 0; index < this.motors.length; index++) {
            DcMotor m = this.motors[index];
            if(m == null)
                continue;

            m.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            if(step % 2 == 1) {
                m.setDirection(DcMotorSimple.Direction.FORWARD);
            } else {
                m.setDirection(DcMotorSimple.Direction.REVERSE);
            }
            step++;
        }
    }

    @Override
    protected void state(AbstractState state) {
        if((state instanceof MecanumState) || state.getValues().length == 3) {
            double y1 = state.getValue(MecanumState.Y1);
            double x1 = state.getValue(MecanumState.X1);
            double x2 = state.getValue(MecanumState.X2);

            this.getMotor(FRONT |  LEFT).setPower(this.forRatio(y1 + x1 + x2));
            this.getMotor(FRONT | RIGHT).setPower(this.forRatio(y1 - x1 - x2));
            this.getMotor(BACK  |  LEFT).setPower(this.forRatio(y1 - x1 + x2));
            this.getMotor(BACK  | RIGHT).setPower(this.forRatio(y1 + x1 - x2));
        }
    }

    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    public double forRatio(double v) {
        return v * this.getRatio();
    }
}
