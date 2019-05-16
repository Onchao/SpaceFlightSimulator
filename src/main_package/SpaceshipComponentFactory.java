package main_package;

import java.lang.reflect.InvocationTargetException;

public class SpaceshipComponentFactory<T extends SpaceshipComponent> {
    private Class<T> cls;

    public SpaceshipComponentFactory(Class<T> c) {
        cls = c;
    }

    public Class<T> getComponentClass() {
        return cls;
    }

    public T getInstance(Object... args) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        return (T)cls.getConstructors()[0].newInstance(args);
    }

    @Override
    public String toString() {
        String ret = null;
        try {
            ret = (String)cls.getMethod("getName").invoke(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
        } catch (NoSuchMethodException e) {
        }
        return ret;
    }
}
