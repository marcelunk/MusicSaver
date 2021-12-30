package com.example.musicSaver;

import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * As a Singleton.
 */
public class Keys {

    private static final Keys keys = new Keys();

    private String CLIENT_ID;
    private String CLIENT_SECRET;

    private Keys() {
        FileReader reader = null;
        Properties props = new Properties();
        try {
            File file = new File(
	                getClass().getClassLoader().getResource("./static/spotify.properties").getFile()
            );
            reader = new FileReader(file);
            props.load(reader);      
        } catch(IOException e) {
            e.printStackTrace();
        }
        
        CLIENT_ID = props.getProperty("CLIENT_ID");
        CLIENT_SECRET = props.getProperty("CLIENT_SECRET");
    }

    public static Keys getInstance() {
        return keys;
    }

    public String getClientId() {
        return this.CLIENT_ID;
    }

    public String getClientSecret() {
        return this.CLIENT_SECRET;
    }
}
