package ru.gkcdu.mailstatus.controller;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.Data;

@RestController
@RequestMapping("mail")
public class MailstatusController {
    private static final Logger LOGGER = LogManager.getLogger(MailstatusController.class);

    


    @FeignClient(url = "hhttps://tracking.russianpost.ru")
    public interface ApiClient {


        @RequestMapping(method = RequestMethod.GET, value = "/fc")
        List<?> getPosts(@RequestParam("_limit") final int postLimit);

    }

    @GetMapping()
    public ResponseEntity getMailStatus() {
        try {
            // 80104390892323

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(
                            new ErrorResponse(
                                    e.getMessage(),
                                    ExceptionUtils.getStackTrace(e)));
        }

        return ResponseEntity.ok("OK");
    }

    @Data
    @AllArgsConstructor
    static class ErrorResponse {
        String error;
        String cause;
    }

}
