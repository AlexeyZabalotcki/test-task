package com.pioneer.pixel.test.task.controller;

import com.pioneer.pixel.test.task.dto.EmailDto;
import com.pioneer.pixel.test.task.dto.UpdateEmailRequest;
import com.pioneer.pixel.test.task.dto.PhoneDto;
import com.pioneer.pixel.test.task.dto.UpdatePhoneRequest;
import com.pioneer.pixel.test.task.dto.UserDto;
import com.pioneer.pixel.test.task.service.UserService;
import com.pioneer.pixel.test.task.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;

@RestController
@RequestMapping("/users")
@Validated
@Tag(name = "Users", description = "Operations for retrieving and managing user data")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "Search users", description = "Search users by optional filters (dateOfBirth, phone, email, name) with pagination")
    public ResponseEntity<Page<UserDto>> searchUsers(
            @RequestParam(name = "dateOfBirth", required = false) @DateTimeFormat(pattern = "dd.MM.yyyy") @Parameter(description = "Filter: dateOfBirth > given date (format dd.MM.yyyy)") LocalDate dateOfBirth,
            @RequestParam(name = "phone", required = false) String phone,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        Page<UserDto> result = userService.searchUsers(dateOfBirth, phone, email, name, PageRequest.of(page, size));
        return ResponseEntity.ok(result);
    }

    @PostMapping("/emails")
    @Operation(summary = "Add email", description = "Add a new email address to the authenticated user")
    public ResponseEntity<EmailDto> addEmail(@AuthenticationPrincipal User user,
            @Valid @RequestBody EmailDto emailDto) {
        return ResponseEntity.ok(userService.addEmail(user.getId(), emailDto));
    }

    @PutMapping("/emails/{emailId}")
    @Operation(summary = "Update email", description = "Update an existing email address of the authenticated user")
    public ResponseEntity<EmailDto> updateEmail(@AuthenticationPrincipal User user,
            @PathVariable Long emailId,
            @Valid @RequestBody UpdateEmailRequest request) {
        EmailDto dto = new EmailDto();
        dto.setEmail(request.getEmail());
        return ResponseEntity.ok(userService.updateEmail(user.getId(), emailId, dto));
    }

    @DeleteMapping("/emails/{emailId}")
    @Operation(summary = "Delete email", description = "Delete an email address by ID for the authenticated user")
    public ResponseEntity<Void> deleteEmail(@AuthenticationPrincipal User user,
            @PathVariable Long emailId) {
        userService.deleteEmail(user.getId(), emailId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/phones")
    @Operation(summary = "Add phone", description = "Add a new phone number to the authenticated user")
    public ResponseEntity<PhoneDto> addPhone(@AuthenticationPrincipal User user,
            @Valid @RequestBody PhoneDto phoneDto) {
        return ResponseEntity.ok(userService.addPhone(user.getId(), phoneDto));
    }

    @PutMapping("/phones/{phoneId}")
    @Operation(summary = "Update phone", description = "Update an existing phone number of the authenticated user")
    public ResponseEntity<PhoneDto> updatePhone(@AuthenticationPrincipal User user,
            @PathVariable Long phoneId,
            @Valid @RequestBody UpdatePhoneRequest request) {
        PhoneDto dto = new PhoneDto();
        dto.setPhone(request.getPhone());
        return ResponseEntity.ok(userService.updatePhone(user.getId(), phoneId, dto));
    }

    @DeleteMapping("/phones/{phoneId}")
    @Operation(summary = "Delete phone", description = "Delete a phone number by ID for the authenticated user")
    public ResponseEntity<Void> deletePhone(@AuthenticationPrincipal User user,
            @PathVariable Long phoneId) {
        userService.deletePhone(user.getId(), phoneId);
        return ResponseEntity.ok().build();
    }
}