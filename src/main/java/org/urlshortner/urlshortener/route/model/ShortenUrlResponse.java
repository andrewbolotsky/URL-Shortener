package org.urlshortner.urlshortener.route.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ShortenUrlResponse {
    private String shortenedUrl;
}
