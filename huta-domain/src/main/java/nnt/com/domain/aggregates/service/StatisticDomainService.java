package nnt.com.domain.aggregates.service;

import nnt.com.domain.aggregates.model.dto.response.statistic.*;

public interface StatisticDomainService {
    BusinessKPIOverview getBusinessKPIOverview(String selectedTime);

    HomestayIncomeResponse getBusinessHomestayIncome(int year);

    BookingLineChartResponse getBookingLineChart(int year);

    BookingLineChartResponse getBookingLineChartByHomestay(Long homestayId, int[] years);

    StateRatePieChartResponse getBookingStatePieChart(int year, Long homestayId);

    StateRatePieChartResponse getUserStatePieChart(int year, Long homestayId);

    RoomOccupancyResponse getRoomOccupancy(Long homestayId, int year, int month);
}
