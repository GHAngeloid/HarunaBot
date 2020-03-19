package configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

/**
 * Reference Class
 * Load Properties File
 */
public class AppConfig {

    public static Properties PROPERTIES = new Properties();

    static Logger logger = LoggerFactory.getLogger(AppConfig.class);

    public static TreeSet<Command> commandSet = new TreeSet<>();

    public static ArrayList<String> tagList = new ArrayList<>();

    public static void initializeProperties() throws IOException{
        AppConfig.PROPERTIES.load(AppConfig.class.getClassLoader().getResourceAsStream("application.properties"));

        // load tagfilter file to a list
        FileInputStream fileInputStream = new FileInputStream("tagfilter.txt");
        int data = fileInputStream.read();
        String word = "";
        while(data != -1) {
            if((char)data == '\n') {
                tagList.add(word);
                word = "";
            }
            else {
                word += (char) data;
            }
            data = fileInputStream.read();
        }
        fileInputStream.close();
        logger.info("Tag filter list is loaded");

        // retrieve serialized file
        try {
            fileInputStream = new FileInputStream(AppConfig.PROPERTIES.getProperty("SERIALFILE"));
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            commandSet = (TreeSet<Command>) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
            logger.info("Total Commands in Set: " + commandSet.size());
            logger.info("Commands are loaded");
        }catch(IOException | ClassNotFoundException e) {
            logger.info("Commands file does not exist");
        }
    }

}
