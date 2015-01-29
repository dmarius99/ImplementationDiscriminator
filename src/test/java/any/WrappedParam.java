package any;

/**
 * Created by Marius on 27/09/14.
 */
public class WrappedParam {

    private String name;
    private Long id;

    public WrappedParam(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
