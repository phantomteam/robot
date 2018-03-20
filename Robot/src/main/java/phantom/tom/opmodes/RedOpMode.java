package phantom.tom.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.Gamepad;

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
import phantom.tom.services.ManualService;
import phantom.tom.services.WatchService;

@TeleOp(name = "Red")
public class RedOpMode extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        State.reset();
        State.currentOpMode = this;
        State.teamColor = TeamColor.RED;

        try {
            gamepad1.copy(new Gamepad(new GamepadStateWatcher("gamepad1")));
            gamepad2.copy(new Gamepad(new GamepadStateWatcher("gamepad2")));
        } catch (RobotCoreException e) {
            e.printStackTrace();
        }

        Curiosity.installEngine(new ServiceEngine());
        Bootstrap bootstrap = new Bootstrap(new Describer(), new HardwareHandler(), new ConfigurationLoader());

        try {
            Lumen.installBootstrap(bootstrap);
            bootstrap.bootstrap("main");
            IonManager.initAccessor(Scopes.AUTONOMOUS.name);

            int manual = ServiceManager.mount(new ManualService(), new ServiceHandler() {
                @Override
                public ServicePacket onDie(ServicePacket packet) {
                    State.pushEvent(Event.END_MANUAL);
                    State.time.cancel();
                    return null;
                }
            });

            int watch = ServiceManager.mount(new WatchService(), new ServiceHandler() {
                @Override
                public ServicePacket onDie(ServicePacket packet) {
                    ServiceManager.kill(manual);
                    return null;
                }
            });

            int autonomous = ServiceManager.mount(new AutonomousService(), new ServiceHandler(){
                @Override
                public ServicePacket onDie(ServicePacket packet) {
                    State.pushEvent(Event.END_AUTONOMOUS);
                    State.pushEvent(Event.START_MANUAL);
                    ServiceManager.boot(manual);
                    ServiceManager.boot(watch);
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
            TimeManager.start(time, 1000);
            State.pushEvent(Event.START_AUTONOMOUS);
            ServiceManager.boot(autonomous);
            while (opModeIsActive());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
