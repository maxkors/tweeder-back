package com.maxkors.tweeder.api;

import com.maxkors.tweeder.domain.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/storage")
@RequiredArgsConstructor
public class StorageController {

    private final StorageService storageService;

    //    @PostMapping
//    ResponseEntity<String> getPresignedPutUrl(@RequestBody MediaData resource) {
//        System.out.println("$$$$$$$$$$$$$$$$$$$$$$ GETPRESIGNEDPUTURL");
//
//        return ResponseEntity.ok(this.storageService.createPresignedPutUrl(resource.name));
//    }
//
//    record MediaData(String name, String type) {
//    }

    @PostMapping
    ResponseEntity<String> uploadFile(@RequestPart(value = "file") MultipartFile file, @RequestPart String text) {
        System.out.println(text);
        return  ResponseEntity.ok().body(this.storageService.uploadFile(file));
    }
}
