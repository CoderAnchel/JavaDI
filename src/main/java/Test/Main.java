package Test;

public class Main {
    public static void main(String[] args) {
        try {
            DependencyInjector.Startup();
            DependencyInjector.showRouteDependency();
            DependencyInjector.showServiceDependency();

            Controller2 controller2 = (Controller2) DependencyInjector.getService(Controller2.class);
            controller2.test();
        } catch (Exception ignored) {

        }
    }
}
