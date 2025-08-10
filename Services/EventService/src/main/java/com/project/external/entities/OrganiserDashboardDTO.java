package com.project.external.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrganiserDashboardDTO {
    private long totalEvents;
    private long activeEvents;
    private long pastEvents;
    private double totalRevenue;
    private long totalTicketsSold;
}
