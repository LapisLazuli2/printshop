package service;

import java.util.Scanner;

import pojo.User;

public class UserService {
    private User user;
    private Scanner sc;

    /*
     * Constructor
     */
    public UserService(){
        this.sc = new Scanner(System.in);
    }

    /*
     * Create a new a user
     */
    public void createUser(String firstName, String lastName, String address, String postcode, String email, String phoneNumber){
        this.user = new User(firstName, lastName, address, postcode, email, phoneNumber);
    }

    /*
     * Returns a copy of this.user
     */
    public User retrieveUser(){
        // User copy = new User(this.user);
        // return copy;
        // return this.user;
        return new User(this.user);
    }

    /*
     * Returns true if a user has already been created
     */
    public boolean isUserCreated(){
        return this.user != null;
    }

    /*
     * Function: createNewUser()
     * Prompts the user for their user information and creates a new user via
     * userService
     */
    public void promptCreateNewUser() {
        System.out.println("--- Customer Information ---");
        System.out.println("Please enter your personal information");
        System.out.print("First Name: ");
        String firstName = sc.nextLine();
        System.out.print("Last Name: ");
        String lastName = sc.nextLine();
        System.out.print("Address: ");
        String address = sc.nextLine();
        System.out.print("Postcode: ");
        String postcode = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Phone Number: ");
        String phoneNumber = sc.nextLine();
        this.createUser(firstName, lastName, address, postcode, email, phoneNumber);
        // Clear the terminal screen
        MenuService.clearScreen();
    }
}
