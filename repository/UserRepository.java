package repository;

import pojo.User;

public class UserRepository {
    private User user;

    /*
     * Create a new a user with info filled in
     */
    public void createUser(String firstName, String lastName, String address, String postcode, String email,
            String phoneNumber) {
        this.user = new User(firstName, lastName, address, postcode, email, phoneNumber);
    }

    /*
     * Create a new a user without any info filled in
     */
    public void createUser() {
        this.user = new User();
    }

    /*
     * Returns a copy of this.user
     */
    public User retrieveUser() {
        return new User(this.user);
    }

    /*
     * Returns true if a user has already been created
     */
    public boolean isUserCreated() {
        return this.user != null;
    }

    public void setFirstName(String firstName) {
        this.user.setFirstName(firstName);
    }

    public void setLastName(String lastName) {
        this.user.setLastName(lastName);
    }

    public void setAddress(String address) {
        this.user.setAddress(address);
    }

    public void setPostcode(String postcode) {
        this.user.setPostcode(postcode);
    }

    public void setEmail(String email) {
        this.user.setEmail(email);
    }

    public void setPhoneNumber(String phoneNumber) {
        this.user.setPhoneNumber(phoneNumber);
    }

}
