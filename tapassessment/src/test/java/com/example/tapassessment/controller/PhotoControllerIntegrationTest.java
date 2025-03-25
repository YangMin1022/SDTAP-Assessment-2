// This class tests the PhotoController class using Spring Boot's MockMvc.
package com.example.tapassessment.controller;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PhotoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetPhotosByArtistEndpoint() throws Exception {
        // Assumes valid data exists for artist "Smith"
        mockMvc.perform(get("/api/photos/artist").param("name", "Smith"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                // Verify that the JSON array is not empty
                .andExpect(jsonPath("$", not(empty())))
                // Check a property on one of the returned Photo objects
                .andExpect(jsonPath("$[0].title", notNullValue()));
    }
    
    @Test
    public void testGetTopRatedPhotosEndpoint() throws Exception {
        mockMvc.perform(get("/api/photos/top-rated"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                // Check that every photo's "ratingValue" is greater than 4.
                .andExpect(jsonPath("$[*].ratingValue", everyItem(greaterThan(4))))
                .andExpect(jsonPath("$", not(empty())));
    }
    
    @Test
    public void testGetAverageRatingEndpoint() throws Exception {
        // Test average rating for a specific artist
        mockMvc.perform(get("/api/photos/average-rating").param("artist", "Smith"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$").value(instanceOf(Number.class)));

    }
    
    @Test
    public void testGetMostPopularRatingDayEndpoint() throws Exception {
        mockMvc.perform(get("/api/ratings/most-popular-day"))
                .andExpect(status().isOk())
                //.andExpect(content().contentType("application/json")) 
                // The response is a string, not JSON
                // Check that the response is a non-empty string (the day of the week)
                .andExpect(jsonPath("$", not(emptyOrNullString())));
    }
}
