package com.inventory.domain.policy;

import com.inventory.domain.model.Reservation;
import org.springframework.stereotype.Component;
import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class ExpirationPolicy {
    
    private static final int EXPIRING_SOON_MINUTES = 5;
    
    public boolean isExpired(Reservation reservation) {
        return LocalDateTime.now().isAfter(reservation.getExpiresAt());
    }
    
    public Duration timeUntilExpiration(Reservation reservation) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = reservation.getExpiresAt();
        
        if (now.isAfter(expiresAt)) {
            return Duration.ZERO;
        }
        
        return Duration.between(now, expiresAt);
    }
    
    public boolean isExpiringSoon(Reservation reservation) {
        Duration remaining = timeUntilExpiration(reservation);
        long remainingMinutes = remaining.toMinutes();
        
        return remainingMinutes > 0 && remainingMinutes < EXPIRING_SOON_MINUTES;
    }
    
    public int getMinutesUntilExpiration(Reservation reservation) {
        return (int) timeUntilExpiration(reservation).toMinutes();
    }
}

