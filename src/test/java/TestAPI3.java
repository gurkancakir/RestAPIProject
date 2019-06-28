import bean.Input;
import bean.Output;
import core.interfaces.RestAPI;

import java.util.HashMap;
import java.util.Map;

@RestAPI(
        endpoint = "/test.json"
)
public class TestAPI3 extends ACompanyAPI<Input, Output> {

    public TestAPI3() {}

    public TestAPI3(Input input) {
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

    @Override
    public Map<String, String> getQueryParams() {
        Map<String, String> params = new HashMap<>();
        params.put("id", "1");
        params.put("name", "gurkan");
        return params;
    }
}
