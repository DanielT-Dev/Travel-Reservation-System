package model;

public class User implements Identifiable<Integer> {
    private Integer id;
    private String name;
    private String email;
    private String password;

    public User(Integer id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    @Override
    public String toString() {
        return id + " " +  name + " " + email + " " + password;
    }

    @Override
    public Integer getID() {
        return 0;
    }

    @Override
    public void setID(Integer id) {

    }
}
