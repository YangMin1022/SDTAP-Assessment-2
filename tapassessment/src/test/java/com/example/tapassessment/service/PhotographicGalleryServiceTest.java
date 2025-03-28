// Purpose: Test class for PhotographicGalleryService.
package com.example.tapassessment.service;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import com.example.tapassessment.model.Member;
import com.example.tapassessment.model.Photo;
import com.example.tapassessment.model.Rating;

public class PhotographicGalleryServiceTest {

    @InjectMocks
    private PhotographicGalleryService service;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Inject the mocked RestTemplate into the service
        service.setRestTemplate(restTemplate);
    }

    @Test
    void testGetPhotosByArtist() {
        // Prepare dummy data for members and photos.
        Member member1 = new Member();
        member1.setId(1);
        member1.setForenames("John");
        member1.setSurname("Smith");

        Member[] members = new Member[]{ member1 };

        Photo photo1 = new Photo();
        photo1.setId(101);
        photo1.setTitle("Test Photo");
        photo1.setMemberID(1);

        Photo[] photos = new Photo[]{ photo1 };

        // Stub the external API calls
        when(restTemplate.getForObject("https://web.socem.plymouth.ac.uk/COMP2005/photographic-gallery-webservice/Member", Member[].class))
            .thenReturn(members);
        when(restTemplate.getForObject("https://web.socem.plymouth.ac.uk/COMP2005/photographic-gallery-webservice/Photo", Photo[].class))
            .thenReturn(photos);

        // Call the method under test
        List<Photo> result = service.getPhotosByArtist("Smith");

        // Verify results
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Photo", result.get(0).getTitle());
    }

    @Test
    void testGetPhotosByArtistNoMatch() {
        // Prepare dummy data where no member matches the search criteria.
        Member member1 = new Member();
        member1.setId(1);
        member1.setForenames("Alice");
        member1.setSurname("Johnson");
        Member[] members = new Member[] { member1 };

        Photo photo1 = new Photo();
        photo1.setId(101);
        photo1.setTitle("Photo of Alice");
        photo1.setMemberID(1);
        Photo[] photos = new Photo[] { photo1 };

        // Stub API calls
        when(restTemplate.getForObject("https://web.socem.plymouth.ac.uk/COMP2005/photographic-gallery-webservice/Member", Member[].class))
            .thenReturn(members);
        when(restTemplate.getForObject("https://web.socem.plymouth.ac.uk/COMP2005/photographic-gallery-webservice/Photo", Photo[].class))
            .thenReturn(photos);

        // Call method with a non-matching artist name.
        List<Photo> result = service.getPhotosByArtist("Smith");
        assertNotNull(result);
        // Expect an empty list since "Smith" doesn't match "Alice Johnson"
        assertEquals(0, result.size());
    }

    @Test
    void testGetTopRatedPhotos() {
        // Create dummy ratings
        Rating rating1 = new Rating();
        rating1.setId(1);
        rating1.setPhotoID(101);
        rating1.setRatingValue(5); // This rating qualifies (>4)

        Rating rating2 = new Rating();
        rating2.setId(2);
        rating2.setPhotoID(102);
        rating2.setRatingValue(3); // This one does not qualify

        Rating[] ratings = new Rating[] { rating1, rating2 };

        // Create dummy photos corresponding to the ratings.
        Photo photo1 = new Photo();
        photo1.setId(101);
        photo1.setTitle("Top Rated Photo");

        Photo photo2 = new Photo();
        photo2.setId(102);
        photo2.setTitle("Not Top Rated Photo");

        Photo[] photos = new Photo[] { photo1, photo2 };

        // Stub external API calls
        when(restTemplate.getForObject("https://web.socem.plymouth.ac.uk/COMP2005/photographic-gallery-webservice/Rating", Rating[].class))
            .thenReturn(ratings);
        when(restTemplate.getForObject("https://web.socem.plymouth.ac.uk/COMP2005/photographic-gallery-webservice/Photo", Photo[].class))
            .thenReturn(photos);

        // Call the method under test
        List<Photo> result = service.getTopRatedPhotos();

        // Verify that only photo1 is returned because its rating is > 4.
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Top Rated Photo", result.get(0).getTitle());
    }

    @Test
    void testGetTopRatedPhotosEmpty() {
        // Create dummy ratings that do not qualify.
        Rating rating1 = new Rating();
        rating1.setId(1);
        rating1.setPhotoID(101);
        rating1.setRatingValue(3); // below threshold

        Rating[] ratings = new Rating[] { rating1 };

        // Dummy photo.
        Photo photo1 = new Photo();
        photo1.setId(101);
        photo1.setTitle("Not Top Rated Photo");

        Photo[] photos = new Photo[] { photo1 };

        // Stub API calls.
        when(restTemplate.getForObject("https://web.socem.plymouth.ac.uk/COMP2005/photographic-gallery-webservice/Rating", Rating[].class))
            .thenReturn(ratings);
        when(restTemplate.getForObject("https://web.socem.plymouth.ac.uk/COMP2005/photographic-gallery-webservice/Photo", Photo[].class))
            .thenReturn(photos);

        // Call the method.
        List<Photo> result = service.getTopRatedPhotos();

        // Expect an empty list since no ratings > 4 exist.
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void testGetMostPopularRatingDay() {
        // Create dummy ratings with known dates.
        // "2022-08-08T00:00:00" corresponds to a Monday.
        Rating rating1 = new Rating();
        rating1.setId(1);
        rating1.setRatingDate("2022-08-08T00:00:00");

        Rating rating2 = new Rating();
        rating2.setId(2);
        rating2.setRatingDate("2022-08-08T00:00:00");

        // Another rating from a different day.
        Rating rating3 = new Rating();
        rating3.setId(3);
        rating3.setRatingDate("2022-08-09T00:00:00"); // Tuesday

        Rating[] ratings = new Rating[] { rating1, rating2, rating3 };

        // Stub the API call for ratings
        when(restTemplate.getForObject("https://web.socem.plymouth.ac.uk/COMP2005/photographic-gallery-webservice/Rating", Rating[].class))
            .thenReturn(ratings);

        // Call the method under test
        String mostPopularDay = service.getMostPopularRatingDay();

        // Expect "MONDAY" because Monday appears twice.
        assertNotNull(mostPopularDay);
        assertEquals("MONDAY", mostPopularDay);
    }

    @Test
    void testGetAverageRatingForArtist() {
        // Prepare dummy member data
        Member member1 = new Member();
        member1.setId(1);
        member1.setForenames("John");
        member1.setSurname("Smith");
        Member[] members = new Member[] { member1 };

        // Prepare dummy photo data associated with the member.
        Photo photo1 = new Photo();
        photo1.setId(101);
        photo1.setTitle("Test Photo");
        photo1.setMemberID(1);
        Photo[] photos = new Photo[] { photo1 };

        // Prepare dummy ratings for the photo: two ratings 5 and 3.
        Rating rating1 = new Rating();
        rating1.setId(1);
        rating1.setPhotoID(101);
        rating1.setRatingValue(5);

        Rating rating2 = new Rating();
        rating2.setId(2);
        rating2.setPhotoID(101);
        rating2.setRatingValue(3);

        Rating[] ratings = new Rating[] { rating1, rating2 };

        // Stub the external API calls
        when(restTemplate.getForObject("https://web.socem.plymouth.ac.uk/COMP2005/photographic-gallery-webservice/Member", Member[].class))
            .thenReturn(members);
        when(restTemplate.getForObject("https://web.socem.plymouth.ac.uk/COMP2005/photographic-gallery-webservice/Photo", Photo[].class))
            .thenReturn(photos);
        when(restTemplate.getForObject("https://web.socem.plymouth.ac.uk/COMP2005/photographic-gallery-webservice/Rating", Rating[].class))
            .thenReturn(ratings);

        // Call the method under test
        Double avgRating = service.getAverageRatingForArtist("Smith");

        // Verify that the average is calculated correctly: (5+3)/2 = 4.0.
        assertNotNull(avgRating);
        assertEquals(4.0, avgRating);
    }

    @Test
    void testGetAverageRatingForArtistNoPhotos() {
        // Prepare dummy member data where the artist does not have any photos.
        Member member1 = new Member();
        member1.setId(1);
        member1.setForenames("Alice");
        member1.setSurname("Johnson");
        Member[] members = new Member[] { member1 };

        // Empty photo array for this artist.
        Photo[] photos = new Photo[] {};

        // Prepare dummy ratings.
        Rating rating1 = new Rating();
        rating1.setId(1);
        rating1.setPhotoID(101);
        rating1.setRatingValue(5);
        Rating[] ratings = new Rating[] { rating1 };

        // Stub API calls.
        when(restTemplate.getForObject("https://web.socem.plymouth.ac.uk/COMP2005/photographic-gallery-webservice/Member", Member[].class))
            .thenReturn(members);
        when(restTemplate.getForObject("https://web.socem.plymouth.ac.uk/COMP2005/photographic-gallery-webservice/Photo", Photo[].class))
            .thenReturn(photos);
        when(restTemplate.getForObject("https://web.socem.plymouth.ac.uk/COMP2005/photographic-gallery-webservice/Rating", Rating[].class))
            .thenReturn(ratings);

        // Call the method under test.
        Double avgRating = service.getAverageRatingForArtist("Johnson");

        // Expect 0.0 if no photos are found for the artist.
        assertNotNull(avgRating);
        assertEquals(0.0, avgRating);
    }
}
