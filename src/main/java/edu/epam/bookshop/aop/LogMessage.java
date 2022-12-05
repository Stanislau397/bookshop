package edu.epam.bookshop.aop;

public class LogMessage {

    private LogMessage() {

    }

    //controller
    public static final String CONTROLLER_METHOD_EXECUTION = "Executing Controller method {}.";
    public static final String CONTROLLER_METHOD_FINISHED_EXECUTION = "Controller method {} finished execution.";

    //service
    public static final String SERVICE_METHOD_EXECUTION = "Executing Service method {}.";
    public static final String SERVICE_METHOD_FINISHED_EXECUTION = "Service method {} finished execution.";
    public static final String SERVICE_METHOD_THREW_EXCEPTION = "Service method {} threw exception with message {}";

    //repository
    public static final String REPOSITORY_METHOD_EXECUTION = "Executing Repository method {}.";
    public static final String REPOSITORY_METHOD_FINISHED_EXECUTION = "Repository method {} finished execution.";
}
