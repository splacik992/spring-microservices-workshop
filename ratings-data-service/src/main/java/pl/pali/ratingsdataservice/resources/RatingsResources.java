package pl.pali.ratingsdataservice.resources;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.pali.ratingsdataservice.models.Rating;
import pl.pali.ratingsdataservice.models.UserRating;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/ratingsdata")
public class RatingsResources {

@RequestMapping("/{movieId}")
    public Rating getRating(@PathVariable("movieId") String movieId){
    return new Rating(movieId,4);
}

    @RequestMapping("users/{userId}")
    public UserRating getUserRating(@PathVariable("userId") String userId){
         List<Rating> ratings = Arrays.asList(
                new Rating("550", 3),
                new Rating("100", 3)

        );
        UserRating userRating = new UserRating();
        userRating.setUserRating(ratings);
        return userRating;
    }
}
