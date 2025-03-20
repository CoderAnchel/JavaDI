package Test;

@Route("/route")
public class Controller2 {

    @Inject
    public Service1 service1;

    public void test() {
        this.service1.show();
    }
}
