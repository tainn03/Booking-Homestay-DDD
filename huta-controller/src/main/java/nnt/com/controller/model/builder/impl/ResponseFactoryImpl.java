package nnt.com.controller.model.builder.impl;

import nnt.com.controller.model.builder.ResponseFactory;
import nnt.com.controller.model.response.ApiResponse;
import nnt.com.controller.model.response.Meta;
import nnt.com.controller.model.response.PageResponse;
import nnt.com.domain.common.exception.ErrorCode;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponseFactoryImpl implements ResponseFactory {

    @Override
    public ApiResponse create(HttpStatus status, Object data) {
        Object payload = data instanceof Page<?> pageData ? getPageResponse(pageData, getMeta(pageData)) : data;
        return ApiResponse.builder()
                .code(status.value())
                .message(status.getReasonPhrase())
                .payload(payload)
                .build();
    }

    @Override
    public ApiResponse create(Object data) {
        Object payload = data instanceof Page<?> pageData ? getPageResponse(pageData, getMeta(pageData)) : data;
        return ApiResponse.builder()
                .payload(payload)
                .build();
    }

    @Override
    public ApiResponse create(ErrorCode errorCode) {
        return ApiResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
    }

    private Meta getMeta(Page<?> pageData) {
        return Meta.builder()
                .number(pageData.getNumber())
                .size(pageData.getSize())
                .pages(pageData.getTotalPages())
                .total(pageData.getTotalElements())
                .build();
    }

    private PageResponse<Object> getPageResponse(Page<?> pageData, Meta meta) {
        return PageResponse.builder()
                .meta(meta)
                .data((List<Object>) pageData.getContent())
                .build();
    }

}
