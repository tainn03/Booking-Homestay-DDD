package nnt.com.controller.http;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.application.service.search.HomestaySearchAppService;
import nnt.com.domain.homestay.model.document.HomestaySearch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class HomestaySearchController {
    HomestaySearchAppService homestaySearchAppService;

    @GetMapping
    public List<HomestaySearch> searchByContent(@RequestParam String content) {
        return homestaySearchAppService.searchByContent(content);
    }

    @GetMapping("/location")
    public List<HomestaySearch> searchByLocation(@RequestParam String lat, @RequestParam String lon, @RequestParam int distance) {
        return homestaySearchAppService.searchByLocation(lat, lon, distance);
    }

}
