package org.urlshortner.urlshortener.service;

import org.urlshortner.urlshortener.route.model.ShortenUrlResponse;

import java.net.URL;
import java.util.Optional;

public interface UrlShortenerService {

    /**
     * Checks if URL is correct, otherwise returns Optional.empty().
     *
     * @param url URL string.
     * @return Optional.of(URL) if url is correct, Optional.empty() otherwise.
     */
    Optional<URL> validUrl(String url);

    Optional<ShortenUrlResponse> getShortenedUrl(URL url);

    ShortenUrlResponse postShortenedUrl(URL url);

    Optional<String> getRedirectUrl(String key);
}
