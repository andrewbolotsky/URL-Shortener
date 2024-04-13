package org.urlshortner.urlshortener.route;

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


    @ResponseBody
    @GetMapping("/shorten_url")
    public ResponseEntity<Object> getShortenUrl(@RequestBody ShortenUrlRequest request) {
        Optional<URL> url = service.validUrl(request.getUrl());
        if (url.isEmpty()) {
            return ResponseEntity.badRequest().body(String.format("URL %s is not correct", request.getUrl()));
        }
        Optional<ShortenUrlResponse> shortenUrlResponse = service.getShortenedUrl(url.get());
        if (shortenUrlResponse.isEmpty()) {
            return ResponseEntity.badRequest().body(String.format("Does not have shorten URL for: %s. Use POST Request /shorten_url to get new shorten url.", url));
        }
        return ResponseEntity.ok(shortenUrlResponse);
    }

    @ResponseBody
    @PostMapping("/shorten_url")
    @Transactional
    public ResponseEntity<Object> postShortenUrlIfNotPosted(@RequestBody ShortenUrlRequest request) {
        Optional<URL> url = service.validUrl(request.getUrl());
        return url.<ResponseEntity<Object>>map(value ->
                        ResponseEntity.ok(service.postShortenedUrl(value)))
                .orElseGet(() ->
                        ResponseEntity
                                .badRequest()
                                .body(String.format("URL %s is not correct", request.getUrl()))
                );
    }

    @GetMapping("/{key}")
    public RedirectView redirectToRealUrl(@PathVariable String key) {
        Optional<String> redirect = service.getRedirectUrl(key);
        if (redirect.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This redirect key has not found, maybe you should post your url to make new redirection");
        }
        return new RedirectView(redirect.get());
    }

}
