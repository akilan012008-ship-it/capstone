package akhilanmart;

import akhilanmart.dao.ReviewDAO;
import akhilanmart.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class ReviewServiceTest {

    private ReviewDAO reviewDAO;
    private ReviewService reviewService;

    @BeforeEach
    public void setUp() {
        reviewDAO = Mockito.mock(ReviewDAO.class);
        reviewService = new ReviewService(reviewDAO);
    }

    @Test
    public void testInvalidRatingRangeFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            reviewService.addReview(1L, 10L, 6, "Great!"); // Rating 6 is invalid
        });

        assertThrows(IllegalArgumentException.class, () -> {
            reviewService.addReview(1L, 10L, 0, "Bad!"); // Rating 0 is invalid
        });
    }

    @Test
    public void testNonPurchaserReviewFails() throws SQLException {
        when(reviewDAO.checkPurchased(10L, 5L)).thenReturn(false);

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            reviewService.addReview(10L, 5L, 4, "Trying to review without purchase");
        });

        assertTrue(exception.getMessage().contains("Only verified buyers"));
    }

    @Test
    public void testDuplicateReviewFails() throws SQLException {
        when(reviewDAO.checkPurchased(10L, 5L)).thenReturn(true);
        when(reviewDAO.checkExistingReview(10L, 5L)).thenReturn(true);

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            reviewService.addReview(10L, 5L, 5, "Second review attempt");
        });

        assertTrue(exception.getMessage().contains("already reviewed"));
    }
}
