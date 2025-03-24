package com.example.tapassessment.controller;

import com.example.tapassessment.model.Photo;
import com.example.tapassessment.service.PhotographicGalleryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PhotoController {
    @Autowired
    private PhotographicGalleryService service;

    // F1: List photos by artist
    @GetMapping("/photos/artist")
    public ResponseEntity<List<Photo>> getPhotosByArtist(@RequestParam String name) {
        return ResponseEntity.ok(service.getPhotosByArtist(name));
    }

    // F2: List photos rated above 4 stars
    @GetMapping("/photos/top-rated")
    public ResponseEntity<List<Photo>> getTopRatedPhotos() {
        return ResponseEntity.ok(service.getTopRatedPhotos());
    }

    // F3: Day with most ratings
    @GetMapping("/ratings/most-popular-day")
    public ResponseEntity<String> getMostPopularRatingDay() {
        return ResponseEntity.ok(service.getMostPopularRatingDay());
    }

    // F4: Average rating for photos by a specific artist
    @GetMapping("/photos/average-rating")
    public ResponseEntity<Double> getAverageRating(@RequestParam String artist) {
        return ResponseEntity.ok(service.getAverageRatingForArtist(artist));
    }
}