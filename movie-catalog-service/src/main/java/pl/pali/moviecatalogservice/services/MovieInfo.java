package pl.pali.moviecatalogservice.services;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.pali.moviecatalogservice.models.CatalogItem;
import pl.pali.moviecatalogservice.models.Movie;

@Service
public class MovieInfo {

    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "getFallBackCatalogItem")
    public CatalogItem getCatalogItem(pl.pali.moviecatalogservice.models.Rating rating) {
        Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);
        //put them all together
        return new CatalogItem(movie.getName(), movie.getDescription(), rating.getRating());
    }

    public CatalogItem getFallBackCatalogItem(pl.pali.moviecatalogservice.models.Rating rating) {
        return new CatalogItem("Movie name not found", "", 0);
    }
}
