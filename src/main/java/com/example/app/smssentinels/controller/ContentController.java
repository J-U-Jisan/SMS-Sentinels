package com.example.app.smssentinels.controller;

import com.example.app.smssentinels.dto.BaseResponse;
import com.example.app.smssentinels.dto.ContentResponse;
import com.example.app.smssentinels.service.ContentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "contents")
public class ContentController {

    private final ContentService contentService;

    public ContentController(ContentService contentService) {
        this.contentService = contentService;
    }

    @GetMapping(value = "process")
    public Mono<ResponseEntity<BaseResponse>> processContents() {
        // Return the Mono that calls the service method
        return contentService.fetchAndStoreContents()
                .thenReturn(ResponseEntity.ok(new BaseResponse(200, "Contents processed and saved successfully.")))
                .onErrorResume(error -> Mono.just(ResponseEntity.status(500).body(new BaseResponse( 500, "Error processing contents: " + error.getMessage()))));
    }
}
