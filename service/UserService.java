package service;

import java.util.Scanner;

import pojo.User;
import repository.UserRepository;

public class UserService {
    
    private UserRepository userRepository;

    /*
     * Constructor
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /*
     * Create a new a user
     */
    public void createUser(String firstName, String lastName, String address, String postcode, String email,
            String phoneNumber) {
        this.userRepository.createUser(firstName, lastName, address, postcode, email, phoneNumber);
    }

    /*
     * Returns a copy of this.user
     */
    public User retrieveUser() {
        return this.userRepository.retrieveUser();
    }

    /*
     * Returns true if a user has already been created
     */
    public boolean isUserCreated() {
        return this.userRepository.isUserCreated();
    }

    /*
     * Function: createNewUser()
     * Prompts the user for their user information and creates a new user via
     * userService
     */
    public void promptCreateNewUser(Scanner sc) {
        System.out.println("--- Customer Information ---");
        System.out.println("Please enter your personal information");
        this.userRepository.createUser();
        promptFirstName(sc);
        promptLastName(sc);
        promptAddress(sc);
        promptPostcode(sc);
        promptEmail(sc);
        promptPhoneNumber(sc);
        // Clear the terminal screen
        MenuService.clearScreen();
    }

    public void promptFirstName(Scanner sc) {
        System.out.print("First Name: ");
        String firstName = sc.nextLine();
        this.userRepository.setFirstName(firstName);
    }

    public void promptLastName(Scanner sc) {
        System.out.print("Last Name: ");
        String lastName = sc.nextLine();
        this.userRepository.setLastName(lastName);
    }

    public void promptAddress(Scanner sc) {
        System.out.print("Address: ");
        String address = sc.nextLine();
        this.userRepository.setAddress(address);
    }

    public void promptPostcode(Scanner sc) {
        System.out.print("Postcode: ");
        String postcode = sc.nextLine();
        this.userRepository.setPostcode(postcode);
    }

    public void promptEmail(Scanner sc) {
        System.out.print("Email: ");
        String email = sc.nextLine();
        this.userRepository.setEmail(email);
    }

    public void promptPhoneNumber(Scanner sc) {
        System.out.print("Phone Number: ");
        String phoneNumber = sc.nextLine();
        this.userRepository.setPhoneNumber(phoneNumber);
    }

}
