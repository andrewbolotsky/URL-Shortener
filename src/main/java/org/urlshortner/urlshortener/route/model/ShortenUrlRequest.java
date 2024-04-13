package org.urlshortner.urlshortener.route.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ShortenUrlRequest implements Serializable {
    private String url;
}
