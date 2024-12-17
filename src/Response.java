import java.io.Serializable;
import java.util.ArrayList;

public class Response implements Serializable{

    private String value;

    public Response(String val){
        this.value = val;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return this.getValue();
    }

    public boolean Equals(Response other) {
        return (this.getValue().equals(other.getValue()));
    }

}
