package nnt.com.domain.aggregates.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import nnt.com.domain.aggregates.model.dto.response.BookingResponse;
import nnt.com.domain.aggregates.model.dto.response.HomestayResponse;
import nnt.com.domain.aggregates.model.dto.response.statistic.*;
import nnt.com.domain.aggregates.model.entity.Booking;
import nnt.com.domain.aggregates.model.entity.Homestay;
import nnt.com.domain.aggregates.model.entity.Room;
import nnt.com.domain.aggregates.model.entity.User;
import nnt.com.domain.aggregates.model.vo.BookingStatus;
import nnt.com.domain.aggregates.service.*;
import nnt.com.domain.shared.exception.BusinessException;
import nnt.com.domain.shared.exception.ErrorCode;
import nnt.com.domain.shared.model.entity.BaseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Slf4j
public class StatisticDomainServiceImpl implements StatisticDomainService {
    UserDomainService userDomainService;
    HomestayDomainService homestayDomainService;
    RoomDomainService roomDomainService;
    RoomAvailableDomainService roomAvailableDomainService;
    BookingDomainService bookingDomainService;
    PaymentDomainService paymentDomainService;
    SubscriptionDomainService subscriptionDomainService;
    UserSubscriptionDomainService userSubscriptionDomainService;

    static List<String> colors = Arrays.asList(
            "#bbdefb", // Light Blue
            "#2196f3", // Blue
            "#0d47a1", // Dark Blue
            "#64b5f6"
    );

    @Override
    public BusinessKPIOverview getBusinessKPIOverview(String selectedTime) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDomainService.getByEmail(email);

        int days = switch (selectedTime) {
            case "week" -> 7;
            case "month" -> 30;
            case "quarter" -> 90;
            case "year" -> 365;
            default -> throw new BusinessException(ErrorCode.INVALID_DATE);
        };
        LocalDate startDate = LocalDate.now().minusDays(days);
        LocalDate endDate = LocalDate.now();

        BusinessKPIOverview kpiOverview = getBusinessKPIOverviewWithinDateRange(user, startDate, endDate);
        BusinessKPIOverview previousKpiOverview =
                getBusinessKPIOverviewWithinDateRange(user, startDate.minusDays(days), endDate.minusDays(days));

        String incomeChange = calculatePercentageDifference(kpiOverview.getIncome().getValue(), previousKpiOverview.getIncome().getValue());
        String customerChange = calculatePercentageDifference(kpiOverview.getCustomers().getValue(), previousKpiOverview.getCustomers().getValue());
        String bookingChange = calculatePercentageDifference(kpiOverview.getBookings().getValue(), previousKpiOverview.getBookings().getValue());

        return BusinessKPIOverview.builder()
                .income(ObjectResponse.builder()
                        .key("Tổng thu nhập")
                        .value(kpiOverview.getIncome().getValue())
                        .desc(incomeChange)
                        .build())
                .customers(ObjectResponse.builder()
                        .key("Số lượng khách hàng")
                        .value(kpiOverview.getCustomers().getValue())
                        .desc(customerChange)
                        .build())
                .bookings(ObjectResponse.builder()
                        .key("Số đơn đặt phòng")
                        .value(kpiOverview.getBookings().getValue())
                        .desc(bookingChange)
                        .build())
                .build();
    }

    private String calculatePercentageDifference(String current, String previous) {
        long currentValue = Long.parseLong(current);
        long previousValue = Long.parseLong(previous);
        if (previousValue == 0) {
            return currentValue > 0 ? "+100%" : "0%";
        }
        long difference = ((currentValue - previousValue) * 100) / previousValue;
        return difference > 0 ? "+" + difference + "%" : (difference < 0 ? difference + "%" : "0%");
    }

    private BusinessKPIOverview getBusinessKPIOverviewWithinDateRange(User user, LocalDate startDate, LocalDate endDate) {
        List<Long> roomIds = user.getHomestays().stream()
                .flatMap(homestay -> homestay.getRooms().stream())
                .map(BaseEntity::getId)
                .collect(Collectors.toList());
        List<Booking> bookings = bookingDomainService.getBookingsWithinDateRange(roomIds, startDate, endDate);
        long totalBookings = bookings.size();
        long totalIncome = bookings.stream()
                .mapToLong(Booking::getTotalCost)
                .sum();
        long totalCustomers = bookings.stream()
                .map(Booking::getUser)
                .distinct()
                .count();

        return BusinessKPIOverview.builder()
                .income(ObjectResponse.builder()
                        .value(String.valueOf(totalIncome))
                        .build())
                .customers(ObjectResponse.builder()
                        .value(String.valueOf(totalCustomers))
                        .build())
                .bookings(ObjectResponse.builder()
                        .value(String.valueOf(totalBookings))
                        .build())
                .build();
    }

    @Override
    public HomestayIncomeResponse getBusinessHomestayIncome(int year) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDomainService.getByEmail(email);
        List<HomestayResponse> homestays = homestayDomainService.getByOwner(user.getId());

        List<HomestayIncomeDataset> dataset = buildHomestayIncomeDatasets(homestays, year);
        String total = calculateTotalIncome(dataset);
        return HomestayIncomeResponse.builder()
                .year(year)
                .total(total)
                .dataset(dataset)
                .series(buildHomestayIncomeSeries(homestays, colors))
                .build();
    }

    private String calculateTotalIncome(List<HomestayIncomeDataset> datasets) {
        long total = datasets.stream()
                .flatMap(dataset -> dataset.getHomestayIncomes().values().stream())
                .mapToLong(Long::valueOf)
                .sum();
        if (total / 1000000 > 0) {
            int million = (int) (total / 1000000);
            return new DecimalFormat("#.###").format(million) + " triệu đồng";
        } else if (total / 1000 > 0) {
            int thousand = (int) (total / 1000);
            return new DecimalFormat("#.###").format(thousand) + " nghìn đồng";
        }
        return new DecimalFormat("#.###").format((total)) + " đồng";
    }

    private int getHomestayIncomeInMonth(int month, int year, long homestayId) {
        List<BookingResponse> bookings = bookingDomainService.getBookingsByHomestay(homestayId);
        long totalIncome = bookings.stream()
                .filter(booking -> {
                    try {
                        String trimmedCheckIn = booking.getCheckIn().trim();
                        LocalDate checkInDate = LocalDate.parse(trimmedCheckIn, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
                        return checkInDate.getMonthValue() == month && checkInDate.getYear() == year;
                    } catch (DateTimeParseException e) {
                        log.error("Failed to parse check-in date: {}", booking.getCheckIn(), e);
                        return false; // Skip invalid dates
                    }
                })
                .mapToLong(booking -> paymentDomainService.getTotalPaymentByBooking(booking.getId()))
                .sum();
        return (int) totalIncome;
    }

    private List<HomestayIncomeDataset> buildHomestayIncomeDatasets(List<HomestayResponse> homestays, int year) {
        return Arrays.asList(
                HomestayIncomeDataset.builder()
                        .month("Tháng 1")
                        .homestayIncomes(homestays.stream().collect(Collectors.toMap(
                                HomestayResponse::getId,
                                homestay -> getHomestayIncomeInMonth(1, year, homestay.getId())
                        )))
                        .build(),
                HomestayIncomeDataset.builder()
                        .month("Tháng 2")
                        .homestayIncomes(homestays.stream().collect(Collectors.toMap(
                                HomestayResponse::getId,
                                homestay -> getHomestayIncomeInMonth(2, year, homestay.getId())
                        )))
                        .build(),
                HomestayIncomeDataset.builder()
                        .month("Tháng 3")
                        .homestayIncomes(homestays.stream().collect(Collectors.toMap(
                                HomestayResponse::getId,
                                homestay -> getHomestayIncomeInMonth(3, year, homestay.getId())
                        )))
                        .build(),
                HomestayIncomeDataset.builder()
                        .month("Tháng 4")
                        .homestayIncomes(homestays.stream().collect(Collectors.toMap(
                                HomestayResponse::getId,
                                homestay -> getHomestayIncomeInMonth(4, year, homestay.getId())
                        )))
                        .build(),
                HomestayIncomeDataset.builder()
                        .month("Tháng 5")
                        .homestayIncomes(homestays.stream().collect(Collectors.toMap(
                                HomestayResponse::getId,
                                homestay -> getHomestayIncomeInMonth(5, year, homestay.getId())
                        )))
                        .build(),
                HomestayIncomeDataset.builder()
                        .month("Tháng 6")
                        .homestayIncomes(homestays.stream().collect(Collectors.toMap(
                                HomestayResponse::getId,
                                homestay -> getHomestayIncomeInMonth(6, year, homestay.getId())
                        )))
                        .build(),
                HomestayIncomeDataset.builder()
                        .month("Tháng 7")
                        .homestayIncomes(homestays.stream().collect(Collectors.toMap(
                                HomestayResponse::getId,
                                homestay -> getHomestayIncomeInMonth(7, year, homestay.getId())
                        )))
                        .build(),
                HomestayIncomeDataset.builder()
                        .month("Tháng 8")
                        .homestayIncomes(homestays.stream().collect(Collectors.toMap(
                                HomestayResponse::getId,
                                homestay -> getHomestayIncomeInMonth(8, year, homestay.getId())
                        )))
                        .build(),
                HomestayIncomeDataset.builder()
                        .month("Tháng 9")
                        .homestayIncomes(homestays.stream().collect(Collectors.toMap(
                                HomestayResponse::getId,
                                homestay -> getHomestayIncomeInMonth(9, year, homestay.getId())
                        )))
                        .build(),
                HomestayIncomeDataset.builder()
                        .month("Tháng 10")
                        .homestayIncomes(homestays.stream().collect(Collectors.toMap(
                                HomestayResponse::getId,
                                homestay -> getHomestayIncomeInMonth(10, year, homestay.getId())
                        )))
                        .build(),
                HomestayIncomeDataset.builder()
                        .month("Tháng 11")
                        .homestayIncomes(homestays.stream().collect(Collectors.toMap(
                                HomestayResponse::getId,
                                homestay -> getHomestayIncomeInMonth(11, year, homestay.getId())
                        )))
                        .build(),
                HomestayIncomeDataset.builder()
                        .month("Tháng 12")
                        .homestayIncomes(homestays.stream().collect(Collectors.toMap(
                                HomestayResponse::getId,
                                homestay -> getHomestayIncomeInMonth(12, year, homestay.getId())
                        )))
                        .build()
        );
    }

    private List<HomestayIncomeSeries> buildHomestayIncomeSeries(List<HomestayResponse> homestays, List<String> colors) {
        return homestays.stream()
                .map(homestay -> HomestayIncomeSeries.builder()
                        .dataKey(String.valueOf(homestay.getId()))
                        .label(homestay.getTitle())
                        .color(colors.get((int) (homestay.getId() % colors.size())))
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public BookingLineChartResponse getBookingLineChart(int year) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDomainService.getByEmail(email);

        List<HomestayResponse> homestays = homestayDomainService.getByOwner(user.getId());
        List<BookingSeriesData> series = homestays.stream()
                .map(homestay -> BookingSeriesData.builder()
                        .label(homestay.getTitle())
                        .color(colors.get((int) (homestay.getId() % colors.size())))
                        .data(getNumberOfBookingsInMonths(homestay.getId(), year))
                        .build())
                .collect(Collectors.toList());
        return BookingLineChartResponse.builder()
                .series(series)
                .build();
    }

    private List<Integer> getNumberOfBookingsInMonths(Long homestayId, int year) {
        return Arrays.asList(
                getNumberOfBookingsInMonth(homestayId, 1, year),
                getNumberOfBookingsInMonth(homestayId, 2, year),
                getNumberOfBookingsInMonth(homestayId, 3, year),
                getNumberOfBookingsInMonth(homestayId, 4, year),
                getNumberOfBookingsInMonth(homestayId, 5, year),
                getNumberOfBookingsInMonth(homestayId, 6, year),
                getNumberOfBookingsInMonth(homestayId, 7, year),
                getNumberOfBookingsInMonth(homestayId, 8, year),
                getNumberOfBookingsInMonth(homestayId, 9, year),
                getNumberOfBookingsInMonth(homestayId, 10, year),
                getNumberOfBookingsInMonth(homestayId, 11, year),
                getNumberOfBookingsInMonth(homestayId, 12, year)
        );
    }

    private Integer getNumberOfBookingsInMonth(Long homestayId, int i, int year) {
        Homestay homestay = homestayDomainService.getById(homestayId);
        List<Long> roomIds = homestay.getRooms().stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toList());
        List<Booking> bookings = bookingDomainService.getBookingsWithinDateRange(roomIds,
                LocalDate.of(year, i, 1), YearMonth.of(year, i).atEndOfMonth());
        return bookings.size();
    }

    @Override
    public BookingLineChartResponse getBookingLineChartByHomestay(Long homestayId, int[] years) {
        List<String> customColors = Arrays.asList(
                "#ff8a65", // Coral
                "#1e88e5", // Darker Blue
                "#0d47a1", // Navy Blue
                "#ffcc80", // Light Orange
                "#66bb6a" // Darker Green
        );
        validateHomestayId(homestayId);
        List<BookingSeriesData> series = Arrays.stream(years)
                .mapToObj(year -> BookingSeriesData.builder()
                        .label("Năm " + year)
                        .color(customColors.get(year % customColors.size()))
                        .data(getNumberOfBookingsInMonths(homestayId, year))
                        .build())
                .collect(Collectors.toList());
        return BookingLineChartResponse.builder()
                .series(series)
                .build();
    }


    private void validateHomestayId(Long homestayId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDomainService.getByEmail(email);
        List<Homestay> homestays = user.getHomestays();
        if (homestays.stream().noneMatch(homestay -> homestay.getId().equals(homestayId))) {
            throw new BusinessException(ErrorCode.HOMESTAY_NOT_FOUND);
        }
    }

    @Override
    public StateRatePieChartResponse getBookingStatePieChart(int year, Long homestayId) {
        if (homestayId != null) {
            validateHomestayId(homestayId);
            return StateRatePieChartResponse.builder()
                    .data(buildBookingStateRateData(homestayId, year))
                    .build();
        } else {
            return StateRatePieChartResponse.builder()
                    .data(buildBookingStateRateDataForAllHomestays(year))
                    .build();
        }
    }

    private List<StateRateData> buildBookingStateRateDataForAllHomestays(int year) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Homestay> homestays = userDomainService.getByEmail(email).getHomestays();
        List<Long> roomIds = homestays.stream()
                .flatMap(homestay -> homestay.getRooms().stream())
                .map(BaseEntity::getId)
                .distinct()
                .collect(Collectors.toList());
        return buildBookingData(roomIds, year);
    }

    private List<StateRateData> buildBookingStateRateData(Long homestayId, int year) {
        Homestay homestay = homestayDomainService.getById(homestayId);
        List<Long> roomIds = homestay.getRooms().stream()
                .map(BaseEntity::getId)
                .distinct()
                .collect(Collectors.toList());
        return buildBookingData(roomIds, year);
    }

    private List<StateRateData> buildBookingData(List<Long> roomIds, int year) {
        return Arrays.asList(
                StateRateData.builder()
                        .label("Đã thanh toán")
                        .value(getNumberOfBookingsByState(roomIds, year, BookingStatus.PAID))
                        .color("#4caf50")
                        .build(),
                StateRateData.builder()
                        .label("Đã hủy")
                        .value(getNumberOfBookingsByState(roomIds, year, BookingStatus.CANCELLED))
                        .color("#ff980")
                        .build(),
                StateRateData.builder()
                        .label("Đang chờ")
                        .value(getNumberOfBookingsByState(roomIds, year, BookingStatus.PENDING))
                        .color("#f44336")
                        .build(),
                StateRateData.builder()
                        .label("Đã hoàn tiền")
                        .value(getNumberOfBookingsByState(roomIds, year, BookingStatus.REFUNDED))
                        .color("#2196f3")
                        .build()
        );
    }

    private long getNumberOfBookingsByState(List<Long> roomIds, int year, BookingStatus bookingStatus) {
        List<Booking> bookings = bookingDomainService.getBookingsWithinDateRange(roomIds,
                YearMonth.of(year, 1).atDay(1), YearMonth.of(year, 12).atEndOfMonth());
        return bookings.stream()
                .filter(booking -> booking.getStatus() == bookingStatus)
                .count();
    }

    @Override
    public StateRatePieChartResponse getUserStatePieChart(int year, Long homestayId) {
        List<Long> roomIds;
        if (homestayId == null) {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userDomainService.getByEmail(email);
            roomIds = user.getHomestays().stream()
                    .flatMap(homestay -> homestay.getRooms().stream())
                    .map(BaseEntity::getId)
                    .distinct()
                    .collect(Collectors.toList());
        } else {
            roomIds = homestayDomainService.getById(homestayId).getRooms().stream()
                    .map(BaseEntity::getId)
                    .distinct()
                    .collect(Collectors.toList());
        }

        LocalDateTime startOfYear = LocalDateTime.of(year, 1, 1, 0, 0);
        LocalDateTime startOfNextYear = LocalDateTime.of(year + 1, 1, 1, 0, 0);

        // Query the database for new and returning customers
        Object[] result = bookingDomainService.countNewAndReturningCustomers(roomIds, startOfYear, startOfNextYear);
        Object[] customers = (Object[]) result[0];
        long newCustomers = (long) customers[0];
        long returningCustomers = (long) customers[1];

        return StateRatePieChartResponse.builder()
                .data(Arrays.asList(
                        StateRateData.builder()
                                .label("Khách mới")
                                .value(newCustomers)
                                .color("#2196f3")
                                .build(),
                        StateRateData.builder()
                                .label("Khách quay lại")
                                .value(returningCustomers)
                                .color("#64b5f6")
                                .build()
                ))
                .build();
    }

    @Override
    public RoomOccupancyResponse getRoomOccupancy(Long homestayId, int year, int month) {
        int daysInMonth = YearMonth.of(year, month).lengthOfMonth();
        List<Room> rooms = homestayDomainService.getById(homestayId).getRooms();
        List<List<Integer>> data = buildRoomOccupancyData(rooms.stream().map(BaseEntity::getId).collect(Collectors.toList())
                , year, month, daysInMonth);
        return RoomOccupancyResponse.builder()
                .daysInMonth(daysInMonth)
                .roomNames(rooms.stream()
                        .map(Room::getName)
                        .collect(Collectors.toList()))
                .data(data)
                .build();
    }

    private List<List<Integer>> buildRoomOccupancyData(List<Long> roomIds, int year, int month, int daysInMonth) {
        List<List<Integer>> data = new ArrayList<>();
        for (int j = 0; j < roomIds.size(); j++) {
            List<LocalDate> unavailableDates = roomAvailableDomainService.getUnavailableDates(
                    roomIds.get(j),
                    LocalDate.of(year, month, 1),
                    LocalDate.of(year, month, daysInMonth)
            );
            for (int i = 0; i < daysInMonth; i++) {
                LocalDate currentDate = LocalDate.of(year, month, i + 1);
                int state = unavailableDates.contains(currentDate) ? 100 : 0;
                data.add(List.of(i, j, state));
            }
        }
        return data;
    }

}
