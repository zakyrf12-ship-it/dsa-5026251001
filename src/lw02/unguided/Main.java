package lw02.unguided;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {

    static final int MAX_BORROW = 2;

    public static void main(String[] args) throws FileNotFoundException {

        LinkedList<String[]> requests = new LinkedList<>();
        LinkedList<String[]> books = new LinkedList<>();
        LinkedList<String[]> members = new LinkedList<>();
        LinkedList<String[]> successful = new LinkedList<>();

        Queue<String[]> queue = new LinkedList<>();
        Stack<String[]> failed = new Stack<>();

        // Starting stock of each book
        books.add(new String[]{"Kalkulus", "2"});
        books.add(new String[]{"Fisika", "1"});
        books.add(new String[]{"Statistika", "2"});

        // Read file
        // Path is relative to where you run the program (the src folder)
        Scanner scanner = new Scanner(new File("lw02/unguided/borrowing.txt"));

        while (scanner.hasNext()) {
            String[] request = new String[2];
            request[0] = scanner.next();
            request[1] = scanner.next();
            requests.add(request);

            // Add member only the first time the name appears
            boolean exists = false;

            for (String[] m : members) {
                if (m[0].equals(request[0])) {
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                members.add(new String[]{request[0], "0"});
            }
        }

        scanner.close();

        // LinkedList -> Queue
        queue.addAll(requests);

        // Process requests using FIFO
        while (!queue.isEmpty()) {

            String[] request = queue.poll();

            String name = request[0];
            String title = request[1];

            // Find book
            String[] book = null;

            for (String[] b : books) {
                if (b[0].equals(title)) {
                    book = b;
                    break;
                }
            }

            // Find member
            String[] member = null;

            for (String[] m : members) {
                if (m[0].equals(name)) {
                    member = m;
                    break;
                }
            }

            // Check both conditions
            if (book != null && member != null) {

                int stock = Integer.parseInt(book[1]);
                int borrowed = Integer.parseInt(member[1]);

                if (stock > 0 && borrowed < MAX_BORROW) {

                    book[1] = String.valueOf(stock - 1);
                    member[1] = String.valueOf(borrowed + 1);
                    successful.add(request);

                } else {
                    // Out of stock or limit reached -> Stack
                    failed.push(request);
                }

            } else {
                // Unknown book -> Stack
                failed.push(request);
            }
        }

        // Successful requests
        System.out.println("=== Successfully Processed Requests ===");

        for (String[] request : successful) {
            System.out.println(request[0] + " " + request[1]);
        }

        // Remaining stock
        System.out.println("\n=== Remaining Book Stock ===");

        for (String[] book : books) {
            System.out.println(book[0] + " : " + book[1]);
        }

        // Failed requests (LIFO)
        System.out.println("\n=== Failed Requests ===");

        while (!failed.isEmpty()) {

            String[] request = failed.pop();

            System.out.println(request[0] + " " + request[1]);
        }
    }
}
