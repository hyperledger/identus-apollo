package io.iohk.atala.prism.apollo

/**
 * The `Platform` class represents the platform on which the application is running.
 * This class provides information about the operating system of the device.
 */
public actual object Platform {
    /**
     * Represents the operating system on which the application is running.
     *
     * This variable provides the name and version of the operating system in a string format.
     *
     * The value of this variable is generated at runtime, and it is only applicable for the Android platform.
     *
     * Example:
     *     Android 29
     */
    public actual val OS: String = "Android ${android.os.Build.VERSION.SDK_INT}"
}
