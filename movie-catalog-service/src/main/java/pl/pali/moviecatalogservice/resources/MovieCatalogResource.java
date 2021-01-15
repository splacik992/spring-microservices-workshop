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
import pl.pali.moviecatalogservice.models.UserRating;

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

    private WebClient.Builder webClientBuilder;

    @RequestMapping("/{userId}")
    @HystrixCommand(fallbackMethod = "getFallbackMethod")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {

//        WebClient.Builder builder = WebClient.builder();

        UserRating ratings = restTemplate.getForObject("http://ratings-data-service/ratingsdata/users/" + userId,
                UserRating.class);

        return ratings.getUserRating().stream().map(rating -> {
            //for each movie ID, call movie info service and get details
            Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);
            //put them all together
            return new CatalogItem(movie.getName(), movie.getDescription(), rating.getRating());
        })
                .collect(Collectors.toList());
    }

    public List<CatalogItem> getFallbackMethod(@PathVariable("userId") String userId) {
        return Arrays.asList(new CatalogItem("No Movie","",0));
    }
}
/*
            Movie movie = webClientBuilder.build()
                    .get()
                    .uri("http://localhost:8081/movies/")
                    .retrieve()
                    .bodyToMono(Movie.class)
                    .block();

 */
