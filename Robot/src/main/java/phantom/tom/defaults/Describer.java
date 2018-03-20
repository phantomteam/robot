package phantom.tom.defaults;

import org.firstinspires.ftc.robotcore.internal.collections.SimpleGson;

import labs.vex.lumen.hardware.Hardware;
import labs.vex.lumen.module.Descriptor;
import labs.vex.lumen.module.IDescriber;

public class Describer implements IDescriber {
    @Override
    public Descriptor describe(String s) {
        try {
            return SimpleGson.getInstance().fromJson(ServiceEngine.readFromFile("configuration/" + s + ".json"), Descriptor.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Hardware[] analyze(Descriptor descriptor) {
        try {
            return SimpleGson.getInstance().fromJson(ServiceEngine.readFromFile("configuration/" + descriptor.hardware + ".json"), Hardware[].class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
