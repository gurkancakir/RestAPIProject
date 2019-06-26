import bean.Output;
import bean.Output2;
import core.interfaces.IRestAPI;

public class Test {
    public static void main(String[] args) {
        IRestAPI<Output> api = new TestAPI();
        Output output= api.execute();

        IRestAPI<Output2> api2 = new TestAPI2();
        Output2 output2 = api2.execute();
    }
}