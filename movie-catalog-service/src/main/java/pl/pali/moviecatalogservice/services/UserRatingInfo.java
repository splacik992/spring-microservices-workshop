package pl.pali.moviecatalogservice.services;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;
import pl.pali.moviecatalogservice.models.CatalogItem;
import pl.pali.moviecatalogservice.models.Rating;
import pl.pali.moviecatalogservice.models.UserRating;

import java.util.Arrays;
import java.util.List;

@Service
public class UserRatingInfo {

    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "getFallbackUserRating")
    public UserRating getUserRating(String userId) {
        return restTemplate.getForObject("http://ratings-data-service/ratingsdata/users/" + userId,
                UserRating.class);
    }

    public UserRating getFallbackUserRating(String userId) {
        UserRating userRating = new UserRating();
        userRating.setUserId(userId);
        userRating.setUserRating(Arrays.asList(new Rating("0",0)));
        return userRating;
    }

    public List<CatalogItem> getFallbackMethod(@PathVariable("userId") String userId) {
        return Arrays.asList(new CatalogItem("No Movie", "", 0));
    }
}
