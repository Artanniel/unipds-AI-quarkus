package dev.ai;

import java.time.LocalDate;
import java.util.Locale.Category;

public record Booking(
                Long id,
        String customerName,
        String destination,
        LocalDate startDate,
        LocalDate endDate,
        BookingStatus status,
        Category category) {
}