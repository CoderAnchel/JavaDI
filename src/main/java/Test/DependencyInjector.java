package Test;

import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DependencyInjector {
    private static Set<Class<?>> annotatedClassRoutes;
    private static Set<Class<?>> annotatedClassServices;
    public static Map<Class<?>, Object> serviceInstances = new ConcurrentHashMap<>();

    public static void Startup() throws Exception {
            Reflections reflections = new Reflections("Test");
            annotatedClassRoutes = reflections.getTypesAnnotatedWith(Route.class);
            annotatedClassServices = reflections.getTypesAnnotatedWith(Service.class);

            for (Class<?> clazz: annotatedClassRoutes) {
                serviceInstances.put(clazz, clazz.getDeclaredConstructor().newInstance());
            }

            for (Class<?> clazz: annotatedClassServices) {
                serviceInstances.put(clazz, clazz.getDeclaredConstructor().newInstance());
            }

            for (Object instance : serviceInstances.values()) {
                try {
                    inject(instance);
                } catch (RuntimeException e) {
                    throw new RuntimeException(e);
                }
            }
    };

    public static void inject(Object instance) throws IllegalAccessException {
        Class<?> clazz = instance.getClass();

        for (Field field : clazz.getFields()) {
            if (field.isAnnotationPresent(Inject.class)) {
                Class<?> fieldClass = field.getType();
                Object finalInstance = serviceInstances.get(fieldClass);

                if (finalInstance == null) {
                    throw new RuntimeException("No instance found for " + fieldClass.getSimpleName());
                }

                field.setAccessible(true);
                field.set(instance, finalInstance);
            }
        }
    }

    public static void showRouteDependency() throws Exception {
        if (annotatedClassRoutes != null) {
            for (Class<?> clazz: annotatedClassRoutes) {
                System.out.println("Controller name: "+clazz.getName());
            }
        } else throw new Exception("Execute Startup() first");

    };

    public static void showServiceDependency() throws Exception {
        if (annotatedClassServices != null) {
            for (Class<?> clazz: annotatedClassServices) {
                System.out.println("Service name: "+clazz.getName());
            }
        }  else throw new Exception("Execute Startup() first");
    };

    public static Object getService(Class<?> clazz) {
        return serviceInstances.get(clazz);
    }
}
