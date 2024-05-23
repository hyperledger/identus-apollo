package io.iohk.atala.prism.apollo

/**
 * Provides information about the platform on which the application is running.
 */
public actual object Platform {
    /**
     * Represents the operating system on which the application is running.
     */
    public actual val OS: String = "JVM"
}
