package org.example.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.entities.Train;
import org.example.entities.User;
import org.example.entities.Ticket;


import org.example.util.UserServiceUtil;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class UserBookingService{

    private User user;

    private List<User> userList = new ArrayList<>();

    private ObjectMapper objectMapper = new ObjectMapper();

    private final static String USERS_PATH = "app/src/main/java/org/example/localDb/users.json";

    public UserBookingService(User user1) throws IOException {
        this.user = user1;//
        try {
            this.userList = loadUsers();
        }catch(IOException ex){
            System.out.println("load");
        }
    }

    public UserBookingService() throws IOException{
        try {
            this.userList = loadUsers();
        }catch(IOException ex){
            System.out.println("empty loading");
        }
    }



    public List<User> loadUsers() throws IOException{
        File users = new File(USERS_PATH);
        if (!users.exists() || users.length() == 0) {
            this.userList = new ArrayList<>();
        } else {
            this.userList = objectMapper.readValue(users, new TypeReference<List<User>>() {});
        }
        return userList;
    }

    public Boolean loginUser(){
        Optional<User> foundUser = userList.stream().filter(user1 -> {
            return user1.getName().equals(user.getName()) && UserServiceUtil.checkPassword(user.getPassword(), user1.getHashedPassword());
        }).findFirst();
        return foundUser.isPresent();
    }

    public Boolean signUp(User user1){
        try{
            userList.add(user1);
            saveUserListToFile();
            return Boolean.TRUE;
        }catch (IOException ex){
            System.out.println("signup");
            ex.printStackTrace();
            return Boolean.FALSE;
        }
    }


    private void saveUserListToFile() throws IOException {
        File usersFile = new File(USERS_PATH);
        objectMapper.writeValue(usersFile, userList);
    }

    public void fetchBooking(){

//        user.printTickets();
        Optional<User> userFetched = userList.stream().filter(user1 -> {
            return user1.getName().equals(user.getName()) && UserServiceUtil.checkPassword(user.getPassword(), user1.getHashedPassword());
        }).findFirst();
        if(userFetched.isPresent()){
            userFetched.get().printTickets();
        }
        else{
            System.out.println("Not Present");
        }
    }


    public Boolean cancelBooking(String ticketId) {

        return Boolean.FALSE;
    }
//        Scanner s = new Scanner(System.in);
//        System.out.println("Enter the ticket id to cancel");
//        ticketId = s.next();
//
//        if (ticketId == null || ticketId.isEmpty()) {
//            System.out.println("Ticket ID cannot be null or empty.");
//            return Boolean.FALSE;
//        }
//
//        String finalTicketId1 = ticketId;  //Because strings are immutable
//        boolean removed = user.getTicketsBooked().removeIf(ticket -> ticket.getTicketId().equals(finalTicketId1));
//
//        String finalTicketId = ticketId;
//        user.getTicketsBooked().removeIf(Ticket -> Ticket.getTicketId().equals(finalTicketId));
//        if (removed) {
//            System.out.println("Ticket with ID " + ticketId + " has been canceled.");
//            return Boolean.TRUE;
//        }else{
//            System.out.println("No ticket found with ID " + ticketId);
//            return Boolean.FALSE;
//        }
    /// /////////////


    public List<Train> getTrains(String source, String destination){
        try{
            TrainService trainService = new TrainService();
            return trainService.searchTrains(source, destination);
        }catch(IOException ex){
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }
/// ////////





//
    public List<List<Integer>> fetchSeats(Train train){
        try {
            return train.getSeats();
        } catch (NullPointerException e) {
            System.out.println("No seats booked");
            return null;
        }

    }
//
public Boolean bookTrainSeat(Train train, int row, int seat) {
    try {
        // Fetch the list of seats from the train
        List<List<Integer>> seats = train.getSeats();

        // Check if the row and seat indices are valid
        if (row >= 0 && row < seats.size() && seat >= 0 && seat < seats.get(row).size()) {

            // Check if the seat is available (0 means unbooked)
            if (seats.get(row).get(seat) == 0) {

                // Book the seat (set to 1 to mark as booked)
                seats.get(row).set(seat, 1);
                train.setSeats(seats); // Update the train's seat information

                // Save the updated train data (if necessary)
                TrainService trainService = new TrainService();
                trainService.addTrain(train); // Save the updated train (depending on your logic)

                System.out.println("Seat booked successfully at Row: " + row + ", Seat: " + seat);
                return true; // Booking successful
            } else {
                System.out.println("Seat is already booked at Row: " + row + ", Seat: " + seat);
                return false; // Seat is already booked
            }
        } else {
            System.out.println("Invalid row or seat index.");
            return false; // Invalid row or seat index
        }
    } catch (Exception ex) {
        System.out.println("An error occurred while booking the seat.");
        ex.printStackTrace();
        return false; // An error occurred during booking
    }
}

}