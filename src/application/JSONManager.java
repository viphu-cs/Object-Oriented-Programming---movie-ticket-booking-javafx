package application;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class JSONManager {
	private static final String MOVIE_FILE = "movies.json";
	private static final String SEAT_FILE = "seats.json";

    // เมธอดสร้างไฟล์ (ถ้ายังไม่มี)
    public static void createFile(String filename) {
        File file = new File(filename);
        try {
            if (file.createNewFile()) {
                System.out.println("File created: " + filename);
            } else {
                System.out.println("File already exists: " + filename);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //อ่านไฟล์
    public static void readFile(String filename) {
        try (Scanner scanner = new Scanner(new FileReader(filename))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;

                if(filename.equals("Customer.json")) {
	                try {
	                    JSONObject user = new JSONObject(line);
	                    System.out.println("Username: " + user.getString("username"));
	                } catch (org.json.JSONException e) {
	                    System.out.println("Invalid JSON line skipped: " + line);
	                }
                }else if(filename.equals("Employee.json")) {
	                try {
	                    JSONObject user = new JSONObject(line);
	                    System.out.println("Username: " + user.getString("username"));
	                } catch (org.json.JSONException e) {
	                    System.out.println("Invalid JSON line skipped: " + line);
	                }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    // เมธอดแก้ไขไฟล์ (เช่น แก้ไขอีเมลของ username ที่ระบุ)
    public static void updateFile(String filename, String username, String newEmail) {
        File file = new File(filename);
        StringBuilder updatedContent = new StringBuilder();

        try (Scanner scanner = new Scanner(new FileReader(file))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;

                try {
                    JSONObject user = new JSONObject(line);
                    if (user.getString("username").equals(username)) {
                        user.put("email", newEmail);
                    }
                    updatedContent.append(user.toString()).append("\n");
                } catch (org.json.JSONException e) {
                    System.err.println("Skipping invalid JSON: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(updatedContent.toString());
            System.out.println("File updated successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addUser(String filename,User u) {
        JSONObject user = new JSONObject();
        user.put("firstname", u.getFirstname());
        user.put("lastname", u.getLastname());
        user.put("username", u.getUsername());
        user.put("email", u.getEmail());
        user.put("password", u.getPassword());

        try (FileWriter file = new FileWriter(filename, true)) {
            file.write(user.toString() + "\n");  // 1 object ต่อ 1 บรรทัด
            file.flush();
            System.out.println("User added: " + u.getUsername());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 // ตรวจสอบว่า username มีอยู่แล้วหรือไม่
    public static boolean isUsernameExists(String filename, String username) {
        try (Scanner scanner = new Scanner(new FileReader(filename))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;

                try {
                    JSONObject user = new JSONObject(line);
                    if (user.getString("username").equals(username)) {
                        return true;
                    }
                } catch (org.json.JSONException e) {
                    System.err.println("Invalid JSON: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    
 // เพิ่มหนังใหม่ลงในไฟล์ JSON
    public static void addMovie(Movie m) {
        JSONObject movie = new JSONObject();
        movie.put("title", m.getTitle());
        movie.put("description", m.getDescription());
        movie.put("startDate", m.getStartDate());
        movie.put("endDate", m.getEndDate());
        movie.put("times", m.getTimes());
        movie.put("posterPath", m.getPosterPath());
        movie.put("ticketprice", m.getTicketprice());

        JSONArray moviesArray = readMovies();
        moviesArray.put(movie);
        writeMovies(moviesArray);
        System.out.println("Movie added: " + m.getTitle());
    }

    // ลบหนังโดยใช้ชื่อเรื่อง
    public static void removeMovie(String title) {
        JSONArray moviesArray = readMovies();
        JSONArray updatedMovies = new JSONArray();
        
        for (int i = 0; i < moviesArray.length(); i++) {
            JSONObject movie = moviesArray.getJSONObject(i);
            if (!movie.getString("title").equalsIgnoreCase(title)) {
                updatedMovies.put(movie);
            }
        }
        
        writeMovies(updatedMovies);
        System.out.println("Movie removed: " + title);
    }

    // อ่านไฟล์ JSON และคืนค่าเป็น JSONArray
    public static JSONArray readMovies() {
        File file = new File(MOVIE_FILE);
        if (!file.exists()) return new JSONArray();
        
        try (Scanner scanner = new Scanner(new FileReader(file))) {
            StringBuilder content = new StringBuilder();
            while (scanner.hasNextLine()) {
                content.append(scanner.nextLine());
            }
            return new JSONArray(content.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }

    // เขียนข้อมูลลงไฟล์ JSON
    private static void writeMovies(JSONArray moviesArray) {
        try (FileWriter file = new FileWriter(MOVIE_FILE)) {
            file.write(moviesArray.toString(4)); // จัดรูปแบบ JSON
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void saveBookedSeats(Set<Booking> bookings) {
        JSONArray seatArray = new JSONArray();
        for (Booking booking : bookings) {
            JSONObject obj = new JSONObject();
            obj.put("status", booking.getStatus());
            obj.put("username", booking.getUsername());
            obj.put("firstName", booking.getFirstName());
            obj.put("lastName", booking.getLastName());
            obj.put("movie", booking.getMovie());
            obj.put("date", booking.getDate());
            obj.put("time", booking.getTime());
            obj.put("seat", booking.getSeat());
            obj.put("ID", booking.getID());
            obj.put("ticketprice", booking.getTicketprice());
            seatArray.put(obj);
        }

        try (FileWriter file = new FileWriter(SEAT_FILE)) {
            file.write(seatArray.toString(4)); // จัดรูปแบบ JSON ให้สวยงาม
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Set<Booking> loadBookedSeats() {
        Set<Booking> bookings = new HashSet<>();
        File file = new File(SEAT_FILE);
        if (!file.exists()) return bookings;

        try (Scanner scanner = new Scanner(new FileReader(file))) {
            StringBuilder content = new StringBuilder();
            while (scanner.hasNextLine()) {
                content.append(scanner.nextLine());
            }
            JSONArray seatArray = new JSONArray(content.toString());

            for (int i = 0; i < seatArray.length(); i++) {
                JSONObject obj = seatArray.getJSONObject(i);
                Booking booking = new Booking(
                        obj.getString("status"),
                        obj.getString("username"),
                        obj.getString("firstName"),
                        obj.getString("lastName"),
                        obj.getString("movie"),
                        obj.getString("date"),
                        obj.getString("time"),
                        obj.getString("seat"),
                        obj.getString("ID"),
                        obj.getDouble("ticketprice")
                );
                bookings.add(booking);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bookings;
    }
    
    public static JSONObject getUserByUsername(String username) {
        String[] files = {"Customer.json", "Employee.json"};

        for (String filename : files) {
            try (Scanner scanner = new Scanner(new FileReader(filename))) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine().trim();
                    if (line.isEmpty()) continue;

                    try {
                        JSONObject user = new JSONObject(line);
                        if (user.getString("username").equals(username)) {
                            return user; // คืนค่า JSONObject ของผู้ใช้ที่พบ
                        }
                    } catch (org.json.JSONException e) {
                        System.err.println("Invalid JSON in " + filename + ": " + line);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading file: " + filename);
                e.printStackTrace();
            }
        }
        return null; // ถ้าไม่พบผู้ใช้
    }

}

