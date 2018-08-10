package org.pipservices.components.log;

/**
 * Logging levels to determine details of logged messages
 */
public enum LogLevel {
	/**
	 * Nothing to be logged
	 */
    None,
    
    /**
     * Logs only fatal errors that cause application to fail
     */
    Fatal,
    
    /**
     * Logs all errors - fatal or recoverable
     */
    Error,
    
    /**
     * Logs errors and warnings
     */
    Warn,
    
    /**
     * Logs errors and important information messages
     */
    Info,
    
    /**
     * Logs everything up to high-level debugging information
     */
    Debug,
    
    /**
     * Logs everything down to fine-granular debugging messages
     */
    Trace;    
}
