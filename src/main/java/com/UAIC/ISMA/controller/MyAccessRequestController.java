package com.UAIC.ISMA.controller;

import com.UAIC.ISMA.dto.AccessRequestDTO;
import com.UAIC.ISMA.entity.enums.RequestStatus;
import com.UAIC.ISMA.exception.UserNotFoundException;
import com.UAIC.ISMA.service.AccessRequestService;
import com.UAIC.ISMA.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/my-access-requests")
@Tag(name = "MyAccessRequests", description = "Operations for authenticated user access requests")
public class MyAccessRequestController {

    private static final Logger logger = LogManager.getLogger(MyAccessRequestController.class);

    private final AccessRequestService accessRequestService;
    private final UserService userService;

    public MyAccessRequestController(AccessRequestService accessRequestService, UserService userService) {
        this.accessRequestService = accessRequestService;
        this.userService = userService;
    }

    @Operation(summary = "Get current user's access requests",
            description = "Filter access requests by status and date for the currently authenticated user.")
    @GetMapping
    public ResponseEntity<?> getMyAccessRequests(
            @AuthenticationPrincipal UserDetails currentUser,
            @RequestParam(required = false) @Parameter(description = "Status of access request") RequestStatus status,
            @RequestParam(required = false) @Parameter(description = "Filter by date (yyyy-MM-dd)")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(defaultValue = "0") @Parameter(description = "Page number") int page,
            @RequestParam(required = false) @Parameter(description = "Page size") Integer size
    ) {
        if (currentUser == null || currentUser.getUsername() == null) {
            logger.error("Authenticated user is null or missing username.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated.");
        }

        int pageSize = (size != null) ? size : 10;

        logger.info("Fetching access requests for current user: {}", currentUser.getUsername());

        Long userId = userService.findIdByUsername(currentUser.getUsername());

        List<AccessRequestDTO> results = accessRequestService.findByUserWithFilters(userId, status, date, page, pageSize);

        logger.debug("Found {} access requests for user {}", results.size(), userId);
        return ResponseEntity.ok(results);
    }



    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(UserNotFoundException ex) {
        logger.warn("UserNotFoundException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleGenericError(RuntimeException ex) {
        logger.error("Unexpected error while fetching access requests: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
    }
}
