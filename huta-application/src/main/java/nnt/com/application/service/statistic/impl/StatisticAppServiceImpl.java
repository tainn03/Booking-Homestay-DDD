package nnt.com.application.service.statistic.impl;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.application.service.statistic.StatisticAppService;
import nnt.com.domain.aggregates.model.dto.response.statistic.*;
import nnt.com.domain.aggregates.service.StatisticDomainService;
import nnt.com.infrastructure.cache.redis.RedisCache;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class StatisticAppServiceImpl implements StatisticAppService {
    StatisticDomainService statisticDomainService;
    RedisCache redisCache;

    @Override
    public BusinessKPIOverview getBusinessKPIOverview(String selectedTime) {
        BusinessKPIOverview cachedResponse = getBusinessKPIOverviewFromCache(selectedTime);
        if (cachedResponse != null) {
            return cachedResponse;
        }

        BusinessKPIOverview response = statisticDomainService.getBusinessKPIOverview(selectedTime);
        setBusinessKPIOverviewToCache(selectedTime, response);
        return response;
    }

    private BusinessKPIOverview getBusinessKPIOverviewFromCache(String selectedTime) {
        String key = generateCacheKey("KPI:" + selectedTime);
        return redisCache.getObject(key, BusinessKPIOverview.class);
    }

    private void setBusinessKPIOverviewToCache(String selectedTime, BusinessKPIOverview response) {
        String key = generateCacheKey("KPI:" + selectedTime);
        redisCache.setObject(key, response, 1L, TimeUnit.HOURS);
    }

    @Override
    public HomestayIncomeResponse getBusinessHomestayIncome(int year) {
        HomestayIncomeResponse cachedResponse = getBusinessHomestayIncomeFromCache(String.valueOf(year));
        if (cachedResponse != null) {
            return cachedResponse;
        }

        HomestayIncomeResponse response = statisticDomainService.getBusinessHomestayIncome(year);
        setBusinessHomestayIncomeToCache(String.valueOf(year), response);
        return response;
    }

    private HomestayIncomeResponse getBusinessHomestayIncomeFromCache(String year) {
        String key = generateCacheKey("income:" + year);
        return redisCache.getObject(key, HomestayIncomeResponse.class);
    }

    private void setBusinessHomestayIncomeToCache(String year, HomestayIncomeResponse response) {
        String key = generateCacheKey("income:" + year);
        redisCache.setObject(key, response, 1L, TimeUnit.HOURS);
    }

    @Override
    public BookingLineChartResponse getBookingLineChart(int year) {
        BookingLineChartResponse cachedResponse = getBookingLineChartFromCache(String.valueOf(year));
        if (cachedResponse != null) {
            return cachedResponse;
        }

        BookingLineChartResponse response = statisticDomainService.getBookingLineChart(year);
        setBookingLineChartToCache(String.valueOf(year), response);
        return response;
    }

    private void setBookingLineChartToCache(String year, BookingLineChartResponse response) {
        String key = generateCacheKey("booking:" + year);
        redisCache.setObject(key, response, 1L, TimeUnit.HOURS);
    }

    private BookingLineChartResponse getBookingLineChartFromCache(String year) {
        String key = generateCacheKey("booking:" + year);
        return redisCache.getObject(key, BookingLineChartResponse.class);
    }

    @Override
    public BookingLineChartResponse getBookingLineChartByHomestay(Long homestayId, int[] years) {
        BookingLineChartResponse cachedResponse = getBookingLineChartByHomestayFromCache(homestayId, years);
        if (cachedResponse != null) {
            return cachedResponse;
        }

        BookingLineChartResponse response = statisticDomainService.getBookingLineChartByHomestay(homestayId, years);
        setBookingLineChartByHomestayToCache(homestayId, years, response);
        return response;
    }

    private void setBookingLineChartByHomestayToCache(Long homestayId, int[] years, BookingLineChartResponse response) {
        String key = generateCacheKey(homestayId + ":" + Arrays.stream(years).mapToObj(String::valueOf).reduce((a, b) -> a + "," + b).orElse(""));
        redisCache.setObject(key, response, 1L, TimeUnit.HOURS);
    }

    private BookingLineChartResponse getBookingLineChartByHomestayFromCache(Long homestayId, int[] years) {
        String key = generateCacheKey(homestayId + ":" + Arrays.stream(years).mapToObj(String::valueOf).reduce((a, b) -> a + "," + b).orElse(""));
        return redisCache.getObject(key, BookingLineChartResponse.class);
    }

    @Override
    public StateRatePieChartResponse getBookingStatePieChart(int year, Long homestayId) {
        StateRatePieChartResponse cachedResponse = getBookingStatePieChartFromCache(year, homestayId);
        if (cachedResponse != null) {
            return cachedResponse;
        }

        StateRatePieChartResponse response = statisticDomainService.getBookingStatePieChart(year, homestayId);
        setBookingStatePieChartToCache(year, homestayId, response);
        return response;
    }

    private void setBookingStatePieChartToCache(int year, Long homestayId, StateRatePieChartResponse response) {
        String key = generateCacheKey("booking:state:" + year + ":" + homestayId);
        redisCache.setObject(key, response, 1L, TimeUnit.HOURS);
    }

    private StateRatePieChartResponse getBookingStatePieChartFromCache(int year, Long homestayId) {
        String key = generateCacheKey("booking:state:" + year + ":" + homestayId);
        return redisCache.getObject(key, StateRatePieChartResponse.class);
    }

    @Override
    public StateRatePieChartResponse getUserStatePieChart(int year, Long homestayId) {
        StateRatePieChartResponse cachedResponse = getUserStatePieChartFromCache(year, homestayId);
        if (cachedResponse != null) {
            return cachedResponse;
        }
        StateRatePieChartResponse response = statisticDomainService.getUserStatePieChart(year, homestayId);
        setUserStatePieChartToCache(year, homestayId, response);
        return response;
    }

    private void setUserStatePieChartToCache(int year, Long homestayId, StateRatePieChartResponse response) {
        String key = generateCacheKey("user:state:" + year + ":" + homestayId);
        redisCache.setObject(key, response, 1L, TimeUnit.HOURS);
    }

    private StateRatePieChartResponse getUserStatePieChartFromCache(int year, Long homestayId) {
        String key = generateCacheKey("user:state:" + year + ":" + homestayId);
        return redisCache.getObject(key, StateRatePieChartResponse.class);
    }

    @Override
    public RoomOccupancyResponse getRoomOccupancy(Long homestayId, int year, int month) {
        RoomOccupancyResponse cachedResponse = getRoomOccupancyFromCache(homestayId, year, month);
        if (cachedResponse != null) {
            return cachedResponse;
        }

        RoomOccupancyResponse response = statisticDomainService.getRoomOccupancy(homestayId, year, month);
        setRoomOccupancyToCache(homestayId, year, month, response);
        return response;
    }

    private void setRoomOccupancyToCache(Long homestayId, int year, int month, RoomOccupancyResponse response) {
        String key = generateCacheKey("room:occupancy:" + homestayId + ":" + year + ":" + month);
        redisCache.setObject(key, response, 1L, TimeUnit.HOURS);
    }

    private RoomOccupancyResponse getRoomOccupancyFromCache(Long homestayId, int year, int month) {
        String key = generateCacheKey("room:occupancy:" + homestayId + ":" + year + ":" + month);
        return redisCache.getObject(key, RoomOccupancyResponse.class);
    }

    private String generateCacheKey(String key) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return "statistic:" + key + ":" + email;
    }
}
