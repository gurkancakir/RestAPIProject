import bean.Input2;
import bean.Output2;
import core.RequestType;
import core.interfaces.RestAPI;

@RestAPI(
        endpoint    = "/test2.json",
        requestType = RequestType.POST
)
public class TestAPI2 extends ACompanyAPI<Input2, Output2> {

    public TestAPI2() {}

    public TestAPI2(Input2 input) {
        super();
        this.input = input;
    }

    public void beforeExecute(Input2 input) {
        System.out.println("beforeExecute ");
    }

    public void afterExecute(Output2 output) {
        System.out.println("afterExecute " + output.getTest());
    }

    public void catchException(Exception e) {
        System.out.println("catchException " + e.getMessage());
    }
}
