package com.example.tapassessment.service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.tapassessment.model.Member;
import com.example.tapassessment.model.Photo;
import com.example.tapassessment.model.Rating;

@Service
public class PhotographicGalleryService {
    private static final String MEMBER_URL = "https://web.socem.plymouth.ac.uk/COMP2005/photographic-gallery-webservice/Member";
    private static final String PHOTO_URL = "https://web.socem.plymouth.ac.uk/COMP2005/photographic-gallery-webservice/Photo";
    private static final String RATING_URL = "https://web.socem.plymouth.ac.uk/COMP2005/photographic-gallery-webservice/Rating";

    // private final RestTemplate restTemplate = new RestTemplate();
    private RestTemplate restTemplate;
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

    // Default constructor creates a new RestTemplate
    public PhotographicGalleryService() {
        this.restTemplate = new RestTemplate();
    }
    
    // Setter to allow injecting a mock RestTemplate for testing
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Fetch all members
    public Member[] fetchMembers() {
        return restTemplate.getForObject(MEMBER_URL, Member[].class);
    }

    // Fetch all photos
    public Photo[] fetchPhotos() {
        return restTemplate.getForObject(PHOTO_URL, Photo[].class);
    }

    // Fetch all ratings
    public Rating[] fetchRatings() {
        return restTemplate.getForObject(RATING_URL, Rating[].class);
    }

    // F1: List photos produced by a specific artist
    public List<Photo> getPhotosByArtist(String artistName) {
        Member[] members = fetchMembers();
        // Match by concatenating forenames and surname (case-insensitive)
        Set<Integer> memberIds = Arrays.stream(members)
            .filter(member -> (member.getForenames() + " " + member.getSurname()).toLowerCase().contains(artistName.toLowerCase()))
            .map(Member::getId)
            .collect(Collectors.toSet());

        Photo[] photos = fetchPhotos();
        return Arrays.stream(photos)
            .filter(photo -> memberIds.contains(photo.getMemberID()))
            .collect(Collectors.toList());
    }

    // F2: List photos that were rated above 4 stars
    public List<Photo> getTopRatedPhotos() {
        Rating[] ratings = fetchRatings();
        Set<Integer> topRatedPhotoIds = Arrays.stream(ratings)
            .filter(r -> r.getRatingValue() > 4)
            .map(Rating::getPhotoID)
            .collect(Collectors.toSet());

        Photo[] photos = fetchPhotos();
        return Arrays.stream(photos)
            .filter(photo -> topRatedPhotoIds.contains(photo.getId()))
            .collect(Collectors.toList());
    }

    // F3: Identify which day of the week has the most ratings
    public String getMostPopularRatingDay() {
        Rating[] ratings = fetchRatings();
        Map<DayOfWeek, Long> dayCounts = Arrays.stream(ratings)
            .map(r -> LocalDateTime.parse(r.getRatingDate(), formatter).getDayOfWeek())
            .collect(Collectors.groupingBy(day -> day, Collectors.counting()));

        return dayCounts.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(entry -> entry.getKey().toString())
            .orElse("Unknown");
    }

    // F4: Get the average rating of photos by a specific artist
    public Double getAverageRatingForArtist(String artistName) {
        List<Photo> photos = getPhotosByArtist(artistName);
        if (photos.isEmpty()) {
            return 0.0;
        }
        Rating[] ratings = fetchRatings();
        // Group ratings by photoID
        Map<Integer, List<Rating>> ratingsByPhoto = Arrays.stream(ratings)
            .collect(Collectors.groupingBy(Rating::getPhotoID));

        List<Integer> allRatings = new ArrayList<>();
        for (Photo photo : photos) {
            List<Rating> photoRatings = ratingsByPhoto.get(photo.getId());
            if (photoRatings != null) {
                photoRatings.forEach(r -> allRatings.add(r.getRatingValue()));
            }
        }
        return allRatings.stream().mapToInt(Integer::intValue).average().orElse(0.0);
    }
}
