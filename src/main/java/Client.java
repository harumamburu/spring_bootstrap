/**
 * Created by lilie0-ASUS on 10/27/2015.
 */
public class Client {

    private Long Id;
    private String fullName;

    public Client(Long id, String fullName) {
        Id = id;
        this.fullName = fullName;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
