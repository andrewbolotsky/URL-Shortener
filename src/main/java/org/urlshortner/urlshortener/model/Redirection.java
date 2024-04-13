package org.urlshortner.urlshortener.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;

@Entity
@Data
@NoArgsConstructor
@Table(name = "redirections", schema = "public", catalog = "urlshortener")
public class Redirection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url")
    private URL url;

    @Column(name = "shortened_url")
    private String shortenedUrlKey;

    public Redirection(URL url, String shortenedUrlToken) {
        this.url = url;
        this.shortenedUrlKey = shortenedUrlToken;
    }
}
