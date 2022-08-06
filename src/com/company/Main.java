package com.company;

import java.io.Console;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;


public class Main {
    static int size;
    static ArrayList<Room> hotelRoomSystem = new ArrayList<>();
    static ArrayList<Request> requestList = new ArrayList<>();
    static LinkedHashMap<Request, String> bookingResults = new LinkedHashMap<>();

    public static void main(String[] args) {

        System.out.println("Welcome to HotelRes, this is a hotel room reservation system.");
        Console console = System.console();

        if (console != null) {
            System.out.println("First of all, let's build your hotel.\n" +
                    "Please define the size of the hotel. It has to be an integer, and 1 <= size <= 1000.");
            hotelSize:
            while (true) {
                try {
                    size = Integer.parseInt(console.readLine());
                    if (1 <= size && size <= 1000) {
                        break hotelSize;
                    } else {
                        System.out.println("Invalid size, 1 <= size <= 1000, please try again.");
                    }
                } catch (NumberFormatException nfe) {
                    System.out.println("Invalid size, it has to be an integer, please try again.");
                }
            }
            for (int i=0; i<size; i++) {
                hotelRoomSystem.add(new Room());
            }
            System.out.println("Great! A Hotel with size " + size + " is defined.");

            newRequest:
            while (true) {
                System.out.println("Now, you can enter a series of booking requests in the form of \"(startDate,endDate),(startDate,endDate), ...\"\n" +
                        "Note: Booking is limited up to 365 days, so 0 =< startDate <= endDate <= 364.");
                String requestInput = console.readLine();
                try {
                    Matcher matcher = Pattern.compile("[\\(*][,]{1}[*\\)]").matcher(requestInput); //"\\([0-9]+,[0-9]+\\)"
                    int numRequest = 0;
                    while (matcher.find()) {
                        numRequest++;
                    }
                    if (numRequest == 1) {
                        String request = requestInput;
                            int startDate = Integer.parseInt(request.split(",")[0].replaceAll("[^-?\\d+]", ""));
                            int endDate = Integer.parseInt(request.split(",")[1].replaceAll("[^-?\\d+]", ""));
                            Request r = new Request(startDate, endDate);
                            requestList.add(r);
                    } else {
                        String[] requests = requestInput.split("[\\s]*\\),[\\s]*\\([\\s]*");
                        for (int i=0; i<requests.length; i++) {
                            int startDate = Integer.parseInt(requests[i].split(",")[0].replaceAll("[^-?\\d+]", ""));
                            int endDate = Integer.parseInt(requests[i].split(",")[1].replaceAll("[^-?\\d+]", ""));
                            Request r = new Request(startDate, endDate);
                            requestList.add(r);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Invalid booking request, please try again.");
                }

                for (int i=0; i<requestList.size(); i++) {
                    Request request = requestList.get(i);
                    String result = "Decline";
                    if (0 <= request.startDate && request.startDate <= request.endDate && request.endDate <= 364) {
                        for (int j=0; j<hotelRoomSystem.size(); j++) {
                            if (sumSection(hotelRoomSystem.get(j).dailyAvailability, request.startDate, request.endDate+1) == 0) {
                                result = "Accept";
                                for (int k=request.startDate; k<request.endDate+1; k++) {
                                    hotelRoomSystem.get(j).dailyAvailability[k] = 1;
                                }
                                break;
                            }
                        }
                    }
                    bookingResults.put(request, result);
                }

                for (Request r: bookingResults.keySet()) {
                    String request = r.toString();
                    String result = bookingResults.get(r);
                    System.out.println(request + "\t" + result);
                }

                for (int i=0; i<hotelRoomSystem.size(); i++) {
                    System.out.println(hotelRoomSystem.get(i) + "\t" + Arrays.toString(hotelRoomSystem.get(i).dailyAvailability));
                }

                System.out.println("Do you want to exit HotelRes booking System? y/n");
                if (console.readLine().equalsIgnoreCase("y")) {
                    break newRequest;
                }
            }
        }
    }

    static int sumSection(int[] numArray, int start, int stop) {
        return IntStream.range(start, stop).map(i -> numArray[i]).sum();
    }
}
