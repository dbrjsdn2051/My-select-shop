package org.example.myselectshop.controller;

import lombok.RequiredArgsConstructor;
import org.example.myselectshop.dto.FolderRequestDto;
import org.example.myselectshop.dto.FolderResponseDto;
import org.example.myselectshop.security.UserDetailsImpl;
import org.example.myselectshop.service.FolderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FolderController {

    private final FolderService folderService;

    @PostMapping("/folders")
    public ResponseEntity<Void> addFolders(@RequestBody FolderRequestDto dto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<String> folderNames = dto.getFolderNames();
        folderService.addFolders(folderNames, userDetails.getUser());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/folders")
    public List<FolderResponseDto> getFolders(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return folderService.getFolders(userDetails.getUser());
    }
}
