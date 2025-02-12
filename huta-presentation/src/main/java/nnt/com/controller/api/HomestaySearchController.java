package nnt.com.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.application.service.search.HomestaySearchAppService;
import nnt.com.controller.model.builder.ResponseFactory;
import nnt.com.controller.model.vo.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class HomestaySearchController {
    HomestaySearchAppService homestaySearchAppService;
    ResponseFactory responseFactory;

    @GetMapping
    public ApiResponse searchByContent(@RequestParam String content) {
        return responseFactory.create(homestaySearchAppService.searchByContent(content));
    }

    @GetMapping("/location")
    public ApiResponse searchByLocation(@RequestParam String lat, @RequestParam String lon, @RequestParam int distance) {
        return responseFactory.create(homestaySearchAppService.searchByLocation(lat, lon, distance));
    }

    @GetMapping("/address")
    public ApiResponse searchByAddress(@RequestParam String address) {
        return responseFactory.create(homestaySearchAppService.searchByAddress(address));
    }

}
