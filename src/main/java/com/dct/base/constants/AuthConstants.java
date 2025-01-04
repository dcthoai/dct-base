package com.dct.base.constants;

/**
 * Security configuration parameters and list of permissions and roles
 * @author thoaidc
 */
public interface AuthConstants {

    // Mark the key corresponding to the user's permission list when creating a JWT
    String AUTHORITIES_KEY = "AUTHORITIES_KEY";

    // Configure Content-Security-Policy (CSP), which controls how resources are loaded and executed on the website
    // The main goal is to minimize the risk of attacks such as Cross-Site Scripting (XSS) or Code Injection
    String HEADER_SECURITY_POLICY = "default-src 'self';" + // Only allow resources to be loaded from the same domain
            // Only allow embedded content (iframes) from the same domain (self) or from URLs with schema data
            " frame-src 'self' data:;" +
            // Only allow script execution from the same domain (self), unsafe-inline (allows inline scripts),
            // unsafe-eval (allows eval()), and from the domain https://storage.googleapis.com
            " script-src 'self' 'unsafe-inline' 'unsafe-eval' https://storage.googleapis.com;" +
            // Only allow styles from the same domain (self) and allow inline CSS (unsafe-inline)
            " style-src 'self' 'unsafe-inline';" +
            // Only allow images from the same domain (self) and from URLs with schema data
            " img-src 'self' data:;" +
            // Only allow fonts from the same domain (self) and from URLs with schema data
            " font-src 'self' data:";

    // Permissions-Policy limits access to sensitive browser features, helping protect user privacy
    String HEADER_PERMISSIONS_POLICY = "camera=(), " + // Prevent any origin from using the camera
            "fullscreen=(self), " + // Only allow current domain (self) to use full screen mode
            "geolocation=(), " + // Prevent access to GPS location services
            "gyroscope=(), " + // Prevent use of gyroscope sensor
            "magnetometer=(), " + // Prevent use of magnetometer sensor
            "microphone=(), " + // Prevent microphone access
            "midi=(), " + // Prevent access to MIDI (digital musical instruments)
            "payment=(), " + // Prevent use of payment features
            "sync-xhr=()"; // Prevent use of synchronous XMLHttpRequest requests

    String ROLE_ADMIN = "ADMIN";
    String ROLE_USER = "USER";

    // The list of authorities serves the function of granting permissions to users in the application
    interface PERMISSIONS {

    }
}
