import player.User;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Date;

public class Test {
    private static Manager manager;

    private static void print(Object o){
        System.out.println(o);
    }

    public static void init(){
        manager = Manager.getInstance();
    }

    public static void register(String name, String email, Date DOB, String location, int timezone){
        try{
            manager.register(name, email, DOB, location, timezone);
        }
        catch (Exception e){
            print(e.getMessage());
        }
        print("registered");
    }

    public static void main(String arg[]) throws FileNotFoundException {
        init();
        //register("Hussam", "hussamh10@gmail.com", new Date("18-12-1995"), "Pakistan", +5);
        //User u = manager.getUser("hussamh10@gmail.com");

        //print(u);

    }
}
