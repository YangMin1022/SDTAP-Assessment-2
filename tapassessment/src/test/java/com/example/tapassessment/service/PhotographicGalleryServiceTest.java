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
}
