package ma.enset.digitalbanking.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Utility to retrieve the username of the currently authenticated user,
 * used to stamp every Customer / Account / Operation with "who did it".
 */
public final class AuditUtils {
    private AuditUtils() {}

    public static String currentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return "system";
        return auth.getName();
    }
}
