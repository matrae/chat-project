package digitalExamClock.appClasses;

import digitalExamClock.ServiceLocator;
import digitalExamClock.abstractClasses.Model;

/**
 * Copyright 2015, FHNW, Prof. Dr. Brad Richards. All rights reserved. This code
 * is licensed under the terms of the BSD 3-clause license (see the file
 * license.txt).
 * 
 * @author Brad Richards
 */
public class App_Model extends Model {
    ServiceLocator serviceLocator;
    private int value;
    
    public App_Model() {
        value = 0;
        
        serviceLocator = ServiceLocator.getServiceLocator();        
        serviceLocator.getLogger().info("Application model initialized");
    }
    
    public int getValue() {
        return value;
    }
    
    public int incrementValue() {
        value++;
        serviceLocator.getLogger().info("Application model: value incremented to " + value);
        return value;
    }
}
