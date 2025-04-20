package nnt.com.infrastructure.persistence.booking.database.jpa;

import nnt.com.domain.aggregates.model.entity.Booking;
import nnt.com.domain.aggregates.model.vo.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingInfraRepositoryJpa extends JpaRepository<Booking, Long> {
    @Query("SELECT b FROM Booking b WHERE b.status = ?1")
    List<Booking> findByStatus(BookingStatus bookingStatus);

    @Query("SELECT b FROM Booking b WHERE b.code = ?1")
    Booking findByCode(String orderInfo);

    @Query("SELECT b FROM Booking b JOIN b.rooms r WHERE r.homestay.id = ?1")
    List<Booking> findByHomestayId(long homestayId);

    @Query("SELECT b FROM Booking b JOIN b.rooms r WHERE r.id IN ?1 AND b.checkIn <= ?3 AND b.checkIn >= ?2")
    List<Booking> findBookingsWithinDateRange(List<Long> roomIds, LocalDate startDate, LocalDate endDate);

    @Query(value = """
                SELECT
                    COUNT(DISTINCT b.user.id) AS new_customers,
                    COUNT(DISTINCT b2.user.id) AS returning_customers
                FROM Booking b
                LEFT JOIN Booking b2 ON b2.user.id = b.user.id
                    AND b2.createdAt < :startOfYear
                JOIN b.rooms r
                WHERE r.id IN :roomIds
                  AND b.createdAt >= :startOfYear
                  AND b.createdAt < :startOfNextYear
                  AND (b2 IS NULL OR b2.createdAt >= :startOfYear)
            """)
    Object[] countNewAndReturningCustomers(
            @Param("roomIds") List<Long> roomIds,
            @Param("startOfYear") LocalDateTime startOfYear,
            @Param("startOfNextYear") LocalDateTime startOfNextYear
    );
}