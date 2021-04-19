package org.mal.ls;

import java.io.IOException;

import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

public class MalDebugLogger {

    Logger logger = Logger.getLogger("MALDebugLogger");  
    FileHandler fh;

    public MalDebugLogger() {
        try {
            this.fh = new FileHandler("/home/erik/Documents/Log.log");  
            logger.addHandler(this.fh);
            SimpleFormatter formatter = new SimpleFormatter();  
            fh.setFormatter(formatter);
            this.logger.info("!!!!!!!");
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void log(String text) {
        this.logger.info(text);
    }
}