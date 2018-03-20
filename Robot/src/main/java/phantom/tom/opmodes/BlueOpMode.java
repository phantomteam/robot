package phantom.tom.opmodes;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

import java.util.Timer;

import labs.vex.lumen.Bootstrap;
import labs.vex.lumen.Lumen;
import labs.vex.lumen.dexter.Curiosity;
import labs.vex.lumen.ion.IonManager;
import labs.vex.lumen.ion.service.ServiceHandler;
import labs.vex.lumen.ion.service.ServiceManager;
import labs.vex.lumen.ion.service.ServicePacket;
import labs.vex.lumen.ion.time.TimeManager;
import labs.vex.lumen.ion.time.TimeThread;
import phantom.tom.Event;
import phantom.tom.Scopes;
import phantom.tom.State;
import phantom.tom.TeamColor;
import phantom.tom.defaults.ConfigurationLoader;
import phantom.tom.defaults.Describer;
import phantom.tom.defaults.HardwareHandler;
import phantom.tom.defaults.ServiceEngine;
import phantom.tom.resources.gamepad.GamepadStateWatcher;
import phantom.tom.services.AutonomousService;
import phantom.tom.services.GamepadService;
import phantom.tom.services.ManualService;
import phantom.tom.services.WatchService;

@TeleOp(name = "Blue")
public class BlueOpMode extends LinearOpMode {
    private static String TAG = "Blue";
    @Override
    public void runOpMode() throws InterruptedException {
        State.reset();
        State.currentOpMode = this;
        State.teamColor = TeamColor.BLUE;

        Curiosity.installEngine(new ServiceEngine());
        Bootstrap bootstrap = new Bootstrap(new Describer(), new HardwareHandler(), new ConfigurationLoader());

        try {
            Lumen.installBootstrap(bootstrap);
            bootstrap.bootstrap("main");
            IonManager.initAccessor(Scopes.AUTONOMOUS.name);

            int gamepads = ServiceManager.mount(new GamepadService(), new ServiceHandler() {
                @Override
                public ServicePacket onMount(ServicePacket packet) {
                    Log.d(TAG, "onMount: gamepads");
                    return null;
                }

                @Override
                public ServicePacket onDie(ServicePacket packet) {
                    Log.d(TAG, "onDie: gamepads");
                    return null;
                }
            });
            
            int manual = ServiceManager.mount(new ManualService(), new ServiceHandler() {
                @Override
                public ServicePacket onMount(ServicePacket packet) {
                    Log.d(TAG, "onMount: manual");
                    return null;
                }

                @Override
                public ServicePacket onDie(ServicePacket packet) {
                    Log.d(TAG, "onDie: manual");
                    State.pushEvent(Event.END_MANUAL);
                    State.time.cancel();
                    ServiceManager.kill(gamepads);
                    return null;
                }
            });

            int watch = ServiceManager.mount(new WatchService(), new ServiceHandler() {
                @Override
                public ServicePacket onMount(ServicePacket packet) {
                    Log.d(TAG, "onMount: watch");
                    return null;
                }

                @Override
                public ServicePacket onDie(ServicePacket packet) {
                    Log.d(TAG, "onDie: watch");
                    ServiceManager.kill(manual);
                    return null;
                }
            });

            int autonomous = ServiceManager.mount(new AutonomousService(), new ServiceHandler(){
                @Override
                public ServicePacket onMount(ServicePacket packet) {
                    Log.d(TAG, "onMount: autonomous");
                    return null;
                }

                @Override
                public ServicePacket onDie(ServicePacket packet) {
                    Log.d(TAG, "onDie: autonomous");
                    State.pushEvent(Event.END_AUTONOMOUS);
                    State.pushEvent(Event.START_MANUAL);
                    ServiceManager.boot(manual);
                    ServiceManager.boot(watch);
                    ServiceManager.boot(gamepads);
                    return null;
                }
            });

            TimeThread time = new TimeThread("General");
            State.time = time;
            time.at(30, () -> {
                if(!State.hasEvent(Event.END_AUTONOMOUS)) {
                    ServiceManager.kill(autonomous);
                }
            });

            this.waitForStart();
            Timer timer = TimeManager.start(time, 1000);
            State.pushEvent(Event.START_AUTONOMOUS);
            ServiceManager.boot(autonomous);
            while (this.opModeIsActive());
            timer.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
