import core.AbstractRestAPI;

public abstract class ACompanyAPI<T, V> extends AbstractRestAPI<T, V> {

    public ACompanyAPI() {
        super("http://localhost:8080");
    }
}
