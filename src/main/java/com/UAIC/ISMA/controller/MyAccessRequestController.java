package com.UAIC.ISMA.controller;

import com.UAIC.ISMA.dto.AccessRequestDTO;
import com.UAIC.ISMA.entity.enums.RequestStatus;
import com.UAIC.ISMA.service.AccessRequestService;
import com.UAIC.ISMA.service.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/my-access-requests")
public class MyAccessRequestController {

    private final AccessRequestService accessRequestService;
    private final UserService userService;

    public MyAccessRequestController(AccessRequestService accessRequestService, UserService userService) {
        this.accessRequestService = accessRequestService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<AccessRequestDTO>> getMyAccessRequests(
            @AuthenticationPrincipal UserDetails currentUser,
            @RequestParam(required = false) RequestStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) Integer size
    ) {
        int pageSize = (size != null) ? size : 10;

        Long userId = userService.findIdByUsername(currentUser.getUsername());
        List<AccessRequestDTO> results = accessRequestService.findByUserWithFilters(userId, status, date, page, pageSize);

        return ResponseEntity.ok(results);
    }
}
