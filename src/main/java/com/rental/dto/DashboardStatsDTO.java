package com.rental.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class DashboardStatsDTO {
    private long totalVehicles;
    private long availableVehicles;
    private long pendingBookings;
    private long ongoingBookings;
    private List<BookingDTO> recentBookings;
}
