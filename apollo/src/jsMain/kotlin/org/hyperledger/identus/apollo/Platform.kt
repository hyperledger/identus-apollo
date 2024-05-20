package org.hyperledger.identus.apollo

/**
 * Provides information about the platform on which the application is running.
 */
public actual object Platform {
    /**
     * The current operating system.
     *
     * This property represents the name of the operating system. It is a string value that indicates the type of operating system
     * that the code is running on.
     *
     * In this implementation, the value is set to "JS" which stands for JavaScript. This indicates that the code is running on a JavaScript platform.
     */
    public actual val OS: String = "JS"
}
