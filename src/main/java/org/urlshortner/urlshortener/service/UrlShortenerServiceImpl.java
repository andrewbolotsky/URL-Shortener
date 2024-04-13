package org.urlshortner.urlshortener.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.urlshortner.urlshortener.model.Redirection;
import org.urlshortner.urlshortener.repository.RedirectionRepository;
import org.urlshortner.urlshortener.route.model.ShortenUrlResponse;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;
import java.util.UUID;

@Service
public class UrlShortenerServiceImpl implements UrlShortenerService {
    @Autowired
    RedirectionRepository repository;

    private String getShortenedUrl(String shortenedUrlKey) {
        final String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        return baseUrl + "/" + shortenedUrlKey;
    }

    @Override
    public Optional<URL> validUrl(String url) {
        try {
            URL checkedUrl = URI.create("https://www.example.com").toURL();
            checkedUrl.toURI();
            return Optional.of(checkedUrl);
        } catch (MalformedURLException | URISyntaxException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<ShortenUrlResponse> getShortenedUrl(URL url) {
        Optional<Redirection> redirection = repository.findRedirectionByUrl(url);
        return redirection.map(value -> new ShortenUrlResponse(getShortenedUrl(value.getShortenedUrlKey())));
    }

    @Transactional
    @Override
    public ShortenUrlResponse postShortenedUrl(URL url) {
        Optional<ShortenUrlResponse> foundedUrl = getShortenedUrl(url);
        if (foundedUrl.isPresent()) {
            return foundedUrl.get();
        }
        Redirection redirection = repository.save(new Redirection(url, uniqueKeyForUrl(url.toString())));
        return new ShortenUrlResponse(getShortenedUrl(redirection.getShortenedUrlKey()));
    }

    @Override
    public Optional<String> getRedirectUrl(String key) {
        return repository.findRedirectionByShortenedUrlKey(key)
                .map(Redirection::getUrl)
                .map(URL::toString);
    }


    private String uniqueKeyForUrl(String ignored) {
        return UUID.randomUUID().toString();
    }
}
