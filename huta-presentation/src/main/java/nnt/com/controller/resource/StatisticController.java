package nnt.com.controller.resource;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.application.service.statistic.StatisticAppService;
import nnt.com.controller.model.builder.ResponseFactory;
import nnt.com.controller.model.vo.ApiResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/statistic")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatisticController {
    ResponseFactory responseFactory;
    StatisticAppService service;

    @GetMapping("/business/kpi-overview")
    @PreAuthorize("hasAnyRole('ADMIN', 'LANDLORD')")
    public ApiResponse getBusinessKPIOverview(@RequestParam String selectedTime) {
        return responseFactory.create(service.getBusinessKPIOverview(selectedTime));
    }

    @GetMapping("/business/income")
    @PreAuthorize("hasAnyRole('ADMIN', 'LANDLORD')")
    public ApiResponse getBusinessHomestayIncome(@RequestParam int year) {
        return responseFactory.create(service.getBusinessHomestayIncome(year));
    }

    @GetMapping("/bookings/line-chart")
    @PreAuthorize("hasAnyRole('ADMIN', 'LANDLORD')")
    public ApiResponse getBookingLineChart(@RequestParam int year) {
        return responseFactory.create(service.getBookingLineChart(year));
    }

    @GetMapping("/bookings/line-chart/{homestayId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LANDLORD')")
    public ApiResponse getBookingLineChart(@RequestParam int[] years, @PathVariable Long homestayId) {
        return responseFactory.create(service.getBookingLineChartByHomestay(homestayId, years));
    }

    @GetMapping("/bookings/pie-chart/{year}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LANDLORD')")
    public ApiResponse getBookingStatePieChart(@PathVariable int year, @RequestParam(required = false) Long homestayId) {
        return responseFactory.create(service.getBookingStatePieChart(year, homestayId));
    }

    @GetMapping("/users/pie-chart/{year}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LANDLORD')")
    public ApiResponse getUserStatePieChart(@PathVariable int year, @RequestParam(required = false) Long homestayId) {
        return responseFactory.create(service.getUserStatePieChart(year, homestayId));
    }

    @GetMapping("/room-occupancy/{homestayId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LANDLORD')")
    public ApiResponse getRoomOccupancy(@PathVariable Long homestayId, @RequestParam int year, @RequestParam int month) {
        return responseFactory.create(service.getRoomOccupancy(homestayId, year, month));
    }
}
