package pl.pali.moviecatalogservice.resources;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import pl.pali.moviecatalogservice.models.CatalogItem;
import pl.pali.moviecatalogservice.models.Movie;
import pl.pali.moviecatalogservice.models.Rating;
import pl.pali.moviecatalogservice.models.UserRating;
import pl.pali.moviecatalogservice.services.MovieInfo;
import pl.pali.moviecatalogservice.services.UserRatingInfo;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
@EnableCircuitBreaker
public class MovieCatalogResource {

    @LoadBalanced
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    MovieInfo movieInfo;

    @Autowired
    UserRatingInfo userRatingInfo;



    @RequestMapping("/{userId}")
    @HystrixCommand
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {
        UserRating ratings = userRatingInfo.getUserRating(userId);
        return ratings.getUserRating().stream().map(rating -> movieInfo.getCatalogItem(rating))
                .collect(Collectors.toList());
    }
}
/*

    @Autowired
    private WebClient.Builder webClientBuilder;
    ----------------------------------------------
            Movie movie = webClientBuilder.build()
                    .get()
                    .uri("http://localhost:8081/movies/")
                    .retrieve()
                    .bodyToMono(Movie.class)
                    .block();

 */
