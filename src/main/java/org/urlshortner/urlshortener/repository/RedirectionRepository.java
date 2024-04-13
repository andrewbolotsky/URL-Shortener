package org.urlshortner.urlshortener.repository;

import org.springframework.data.repository.CrudRepository;
import org.urlshortner.urlshortener.model.Redirection;

import java.net.URL;
import java.util.Optional;

public interface RedirectionRepository extends CrudRepository<Redirection, Long> {
    Optional<Redirection> findRedirectionByUrl(URL url);

    Optional<Redirection> findRedirectionByShortenedUrlKey(String key);
}
