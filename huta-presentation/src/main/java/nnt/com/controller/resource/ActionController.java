package nnt.com.controller.resource;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.application.service.user.ActionAppService;
import nnt.com.controller.model.builder.ResponseFactory;
import nnt.com.controller.model.vo.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/actions")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ActionController {
    ResponseFactory responseFactory;
    ActionAppService actionAppService;

    @GetMapping
    public ApiResponse getAll() {
        return responseFactory.create(actionAppService.getAll());
    }

    @GetMapping("/{id}")
    public ApiResponse getById(@PathVariable String id) {
        return responseFactory.create(actionAppService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ApiResponse delete(@PathVariable String id) {
        actionAppService.delete(id);
        return responseFactory.create("Delete action successfully");
    }

    @GetMapping("/login-stat")
    public ApiResponse getLoginStatByYear(@RequestParam String from, @RequestParam String to) {
        return responseFactory.create(actionAppService.getLoginStatByYear(from, to));
    }
}
