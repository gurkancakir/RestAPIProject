import bean.Input;
import bean.Output;
import core.interfaces.RestAPI;

@RestAPI(
        endpoint = "/test.json"
)
public class TestAPI extends ACompanyAPI<Input, Output> {

    public TestAPI() {}

    public TestAPI(Input input) {
        super();
        this.input = input;
    }

    public void beforeExecute(Input input) {
        System.out.println("beforeExecute ");
    }

    public void afterExecute(Output output) {
        System.out.println("afterExecute " + output.getTest());
    }

    public void catchException(Exception e) {
        System.out.println("catchException " + e.getMessage());
    }
}
