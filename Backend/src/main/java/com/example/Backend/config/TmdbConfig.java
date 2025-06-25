package com.example.Backend.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;


@Configuration
@ConfigurationProperties(prefix = "tmdb.api")
@Validated
@Getter
@Setter
public class TmdbConfig {
    // API Connection Info
    // @ConfigurationProperties 방식으로 통일
    @NotBlank(message = "TMDB API Key required")
    private String apiKey;

    @NotBlank(message = "TMDB base url required")
    private String baseUrl;

    @NotBlank(message = "TMDB image base url required")
    private String imageBaseUrl;

    private String language;
    private String region;

    // API settings
    @Positive(message = "Page size must be positive")
    private Integer pageSize;

    @Positive(message = "Max retry count must be positive")
    private Integer maxRetryCount;

    @Positive(message = "Cache expiration minutes must be positive")
    private Integer cacheExpirationMinutes;

    private Endpoints endpoints = new Endpoints();
    private ImageSize imageSize = new ImageSize();
    private RateLimit rateLimit = new RateLimit();
    // API endpoints
    @Getter
    @Setter
    public static class Endpoints{
        private String popularMovies;
        private String nowPlaying;
        private String upcoming;
        private String searchMovie;
        private String movieDetails;
        private String movieCredits;
        private String genreList;
    }

    // image size
    @Getter
    @Setter
    public static class ImageSize{
        private String poster;
        private String backdrop;
        private String profile;
        private String still;
    }

    // ratelimit
    @Getter
    @Setter
    public static class RateLimit{
        @Positive(message = "Requests per second must be positive")
        private Integer requestsPerSecond;
        private Boolean enabled;
    }

    // method 정의
    // utility methods
    public String buildApiUrl(String endpoint){
        return baseUrl + endpoint;
    }

    public String buildImageUrl(String imagePath, String size){
        if(imagePath == null || imagePath.isEmpty()){
            return null; // imagePath empty error
        }
        return imageBaseUrl + "/" + size + imagePath;
    }

    public String buildPosterUrl(String posterPath){
        return buildImageUrl(posterPath, imageSize.getPoster());
    }

    public String buildBackdropUrl(String backdropPath){
        return buildImageUrl(backdropPath, imageSize.getBackdrop());
    }

    public String buildProfileUrl(String profilePath){
        return buildImageUrl(profilePath, imageSize.getProfile());
    }

    public String getDefaultQueryParams(){
        return String.format("api_key=%s&language=%s&region=%s",
                apiKey, language, region);
    }

    public boolean isValidConfiguration() {
        return apiKey != null && !apiKey.trim().isEmpty()
                && baseUrl != null && !baseUrl.trim().isEmpty();
    }

}
