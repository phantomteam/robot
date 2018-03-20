package phantom.tom.defaults;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.ArrayMap;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import dalvik.system.DexFile;
import labs.vex.lumen.dexter.EngineException;
import labs.vex.lumen.dexter.IEngine;
import labs.vex.lumen.ion.service.ServiceManager;

public class ServiceEngine implements IEngine {

    @Override
    public Class[] explore(String s) throws EngineException {
        Set<Class<?>> classSet = null;
        try {
            classSet = ServiceEngine.getClasspathClasses(ServiceEngine.getActivity(), s);
        } catch (Exception e) {
            throw new EngineException();
        }
        return classSet.toArray(new Class[] {});
    }

    public static Reader readFromFile(String path) throws ClassNotFoundException, NoSuchMethodException, NoSuchFieldException, IllegalAccessException, InvocationTargetException, IOException {
        AssetManager am = ServiceEngine.getActivity().getAssets();
        InputStream is = am.open(path);
        return new InputStreamReader(is);
    }

    public static Activity getActivity() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        Class activityThreadClass = Class.forName("android.app.ActivityThread");
        Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null);
        Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
        activitiesField.setAccessible(true);
        ArrayMap activities = (ArrayMap) activitiesField.get(activityThread);
        for (Object activityRecord : activities.values()) {
            Class activityRecordClass = activityRecord.getClass();
            Field pausedField = activityRecordClass.getDeclaredField("paused");
            pausedField.setAccessible(true);

            ServiceManager.class.getConstructors()[0].getParameterTypes();

            if (!pausedField.getBoolean(activityRecord)) {
                Field activityField = activityRecordClass.getDeclaredField("activity");
                activityField.setAccessible(true);
                Activity activity = (Activity) activityField.get(activityRecord);
                return activity;
            }
        }
        return null;
    }

    public static Set<Class<?>> getClasspathClasses(Context context, String packageName) throws IOException, ClassNotFoundException {
        Set<Class<?>> classes = new HashSet<Class<?>>();
        DexFile dex = new DexFile(context.getApplicationInfo().sourceDir);
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<String> entries = dex.entries();
        while (entries.hasMoreElements()) {
            String entry = entries.nextElement();
            if (entry.toLowerCase().startsWith(packageName.toLowerCase()))
                classes.add(classLoader.loadClass(entry));
        }
        return classes;
    }
}
