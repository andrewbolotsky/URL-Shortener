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

    /**
     * Get shortened url from database.
     *
     * @param url Full URL.
     * @return Optional.of(...) if URL is in database, Optional.empty() otherwise.
     */
    Optional<ShortenUrlResponse> getShortenedUrl(URL url);

    /**
     * Post shortened URL to database if it is not exists in database and return existing value from database if it is does exist.
     *
     * @param url Full URL.
     * @return Optional.of(...) if URL insertion is not failed and key was generated correctly, Optional.empty() otherwise.
     */
    Optional<ShortenUrlResponse> postShortenedUrl(URL url);

    /**
     * Get full url to redirect from shortened URL.
     *
     * @param key Key for redirection.
     * @return Full url to redirect.
     */
    Optional<String> getRedirectUrl(String key);
}
