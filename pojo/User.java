package pojo;

public class User {
    private String firstName;
    private String lastName;
    private String address;
    private String postcode;
    private String email;
    private String phoneNumber;

    /*
     * Constructor
     */
    public User(String firstName, String lastName, String address, String postcode, String email, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.postcode = postcode;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    /*
     * Empty constructor
     */
    public User(){}
    
    /*
     * Copy Constructor
     */
    public User(User source){
        this.firstName = source.firstName;
        this.lastName = source.lastName;
        this.address = source.address;
        this.postcode = source.postcode;
        this.email = source.email;
        this.phoneNumber = source.phoneNumber;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostcode() {
        return this.postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
