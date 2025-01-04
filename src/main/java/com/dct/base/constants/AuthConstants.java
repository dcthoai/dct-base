package com.dct.base.constants;

/**
 * Security configuration parameters and list of permissions and roles
 * @author thoaidc
 */
public interface AuthConstants {

    // The encryption complexity in PasswordEncoder's algorithm (between 4 and 31)
    // Higher values mean the password is harder to attack, but too high will reduce performance
    int BCRYPT_COST_FACTOR = 12;

    // Mark the key corresponding to the user's permission list when creating a JWT
    String AUTHORITIES_KEY = "AUTHORITIES_KEY";

    interface REQUEST_MATCHERS {
        String[] OPTIONS = { "/**" };
        String[] ADMIN = { "/api/admin/**", "/admin**" };
        String[] USER = { "/api/users/**", "/users**" };
        String[] PUBLIC = {
            "*.js",
            "*.html",
            "/resources/**",
            "/i18n/**",
            "/test/**",
            "/api/authenticate",
            "/api/auth/**",
            "/api/p/**",
            "/register",
            "/login",
            "/p/**"
        };
    }

    interface CORS {

        String APPLY_FOR = "/**";
        String[] ALLOWED_HEADERS = {
            "Content-Type",     // Content format
            "Authorization",    // Authentication token
            "Accept",           // Client-expected content
            "Origin",           // Origin of the request
            "X-CSRF-Token",     // Anti-CSRF token
            "X-Requested-With", // Ajax request markup
            "Access-Control-Allow-Origin", // Server response header
            "X-App-Version",    // Application version (optional)
            "X-Device-ID"
        };

        String[] ALLOWED_REQUEST_METHODS = {"GET", "PUT", "POST", "DELETE"};
        String[] ALLOWED_ORIGIN_PATTERNS = {"*"};
        boolean ALLOW_CREDENTIALS = true;
    }

    interface HEADER {
        String AUTHORIZATION_HEADER = "Authorization";
        String AUTHORIZATION_GATEWAY_HEADER = "Authorization-Gateway";
        String TOKEN_TYPE = "Bearer "; // JWT token type

        // Configure Content-Security-Policy (CSP), which controls how resources are loaded and executed on the website
        // The main goal is to minimize the risk of attacks such as Cross-Site Scripting (XSS) or Code Injection
        String SECURITY_POLICY = "default-src 'self';" + // Only allow resources to be loaded from the same domain
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
        String PERMISSIONS_POLICY = "camera=(), " + // Prevent any origin from using the camera
                "fullscreen=(self), " + // Only allow current domain (self) to use full screen mode
                "geolocation=(), " + // Prevent access to GPS location services
                "gyroscope=(), " + // Prevent use of gyroscope sensor
                "magnetometer=(), " + // Prevent use of magnetometer sensor
                "microphone=(), " + // Prevent microphone access
                "midi=(), " + // Prevent access to MIDI (digital musical instruments)
                "payment=(), " + // Prevent use of payment features
                "sync-xhr=()"; // Prevent use of synchronous XMLHttpRequest requests
    }

    interface ROLES {
        String ADMIN = "ADMIN";
        String USER = "USER";
    }

    // The list of authorities serves the function of granting permissions to users in the application
    interface PERMISSIONS {

    }
}
