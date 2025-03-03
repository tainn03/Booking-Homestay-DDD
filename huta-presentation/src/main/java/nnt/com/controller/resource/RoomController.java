package nnt.com.controller.resource;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.application.service.room.RoomAppService;
import nnt.com.controller.model.builder.ResponseFactory;
import nnt.com.controller.model.vo.ApiResponse;
import nnt.com.domain.aggregates.model.dto.request.DiscountRequest;
import nnt.com.domain.aggregates.model.dto.request.RoomRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoomController {
    ResponseFactory responseFactory;
    RoomAppService roomAppService;

    @GetMapping("/homestays/{homestayId}")
    public ApiResponse getRoomsByHomestayId(@PathVariable Long homestayId) {
        return responseFactory.create(roomAppService.getRoomsByHomestayId(homestayId));
    }

    @GetMapping("/homestays/{homestayId}/available")
    public ApiResponse getAvailableRoomsByHomestayId(@PathVariable Long homestayId, @RequestParam String checkIn, @RequestParam String checkOut) {
        return responseFactory.create(roomAppService.getAvailableRoomsByHomestayId(homestayId, checkIn, checkOut));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'LANDLORD', 'USER')")
    public ApiResponse createRoom(@RequestBody @Valid RoomRequest roomRequest) {
        return responseFactory.create(roomAppService.createRoom(roomRequest));
    }

    @PatchMapping("/{roomId}/price")
    @PreAuthorize("hasAnyRole('ADMIN', 'LANDLORD')")
    public ApiResponse updateRoomPrice(@PathVariable Long roomId, @RequestParam int dailyPrice, @RequestParam int weekendPrice) {
        return responseFactory.create(roomAppService.updateRoomPrice(roomId, dailyPrice, weekendPrice));
    }

    @PatchMapping("/{roomId}/discount")
    @PreAuthorize("hasAnyRole('ADMIN', 'LANDLORD')")
    public ApiResponse updateRoomDiscount(@PathVariable Long roomId, @RequestParam int weeklyDiscount, @RequestParam int monthlyDiscount) {
        return responseFactory.create(roomAppService.updateRoomDiscount(roomId, weeklyDiscount, monthlyDiscount));
    }

    @PostMapping("/{roomId}/discount")
    @PreAuthorize("hasAnyRole('ADMIN', 'LANDLORD')")
    public ApiResponse addCustomDiscount(@PathVariable Long roomId, @RequestBody @Valid DiscountRequest request) {
        return responseFactory.create(roomAppService.addCustomDiscount(roomId, request));
    }

    @GetMapping("/{roomId}")
    public ApiResponse getRoomById(@PathVariable Long roomId) {
        return responseFactory.create(roomAppService.getRoomById(roomId));
    }

    @DeleteMapping("/{roomId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LANDLORD')")
    public ApiResponse deleteRoom(@PathVariable Long roomId) {
        roomAppService.deleteRoom(roomId);
        return responseFactory.create("Room deleted successfully");
    }
}
