package org.urlshortner.urlshortener.route;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.view.RedirectView;
import org.urlshortner.urlshortener.route.model.ShortenUrlRequest;
import org.urlshortner.urlshortener.route.model.ShortenUrlResponse;
import org.urlshortner.urlshortener.service.UrlShortenerService;

import java.net.URL;
import java.util.Optional;

@RestController
public class UrlShortenerController {
    @Autowired
    UrlShortenerService service;
    Logger logger = LoggerFactory.getLogger(UrlShortenerController.class);


    @ResponseBody
    @GetMapping("/shorten_url")
    public ResponseEntity<Object> getShortenUrl(@RequestBody ShortenUrlRequest request) {
        logger.info(String.format("Request to GET shortened URL. URL is: %s", request.getUrl()));
        Optional<URL> url = service.validUrl(request.getUrl());
        if (url.isEmpty()) {
            logger.info(String.format("Request to GET shortened URL is failed because URL is not correct. URL is: %s", request.getUrl()));
            return ResponseEntity.badRequest().body(String.format("URL %s is not correct", request.getUrl()));
        }
        Optional<ShortenUrlResponse> shortenUrlResponse = service.getShortenedUrl(url.get());
        if (shortenUrlResponse.isEmpty()) {
            logger.info(String.format("Request to GET shortened URL is failed because URL is not saved in database. URL is: %s", request.getUrl()));
            return ResponseEntity.badRequest().body(String.format("Does not have shorten URL for: %s. Use POST Request /shorten_url to get new shorten url.", url));
        }
        return ResponseEntity.ok(shortenUrlResponse);
    }

    @ResponseBody
    @PostMapping("/shorten_url")
    @Transactional
    public ResponseEntity<Object> postShortenUrlIfNotPosted(@RequestBody ShortenUrlRequest request) {
        logger.info(String.format("Request to POST shortened URL. URL is: %s", request.getUrl()));
        Optional<URL> url = service.validUrl(request.getUrl());
        if (url.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(String.format("URL %s is not correct", request.getUrl()));
        }
        Optional<ShortenUrlResponse> response = service.postShortenedUrl(url.get());
        if (response.isEmpty()) {
            logger.error(String.format("Too much time spent to generate new key for URL: %s", request.getUrl()));
            return ResponseEntity
                    .internalServerError()
                    .body("Internal server error: too much time spent to generate new key");
        }
        return ResponseEntity.ok(response.get());
    }

    @GetMapping("/{key}")
    public RedirectView redirectToRealUrl(@PathVariable String key) {
        logger.info(String.format("Request to redirect to URL by key. Key is: %s", key));
        Optional<String> redirect = service.getRedirectUrl(key);
        if (redirect.isEmpty()) {
            logger.info(String.format("Request to redirect to URL by key is failed because key is not saved in database. Key is: %s", key));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This redirect key has not found, maybe you should post your URL to make new redirection");
        }
        logger.info(String.format("Request to redirect to URL by key is failed because key is completed. Key is: %s", key));
        return new RedirectView(redirect.get());
    }

}
