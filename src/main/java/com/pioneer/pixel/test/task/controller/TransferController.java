package com.pioneer.pixel.test.task.controller;

import com.pioneer.pixel.test.task.dto.TransferRequest;
import com.pioneer.pixel.test.task.service.TransferService;
import com.pioneer.pixel.test.task.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/transfer")
@Validated
@Tag(name = "Transfer", description = "Operations for transferring money between users")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    @Operation(summary = "Transfer money", description = "Transfer a specified amount from the authenticated user to another user.")
    public ResponseEntity<Void> transfer(@AuthenticationPrincipal User user,
            @Valid @RequestBody TransferRequest request) {
        transferService.transfer(user.getId(), request.getToUserId(), request.getAmount());
        return ResponseEntity.ok().build();
    }
}