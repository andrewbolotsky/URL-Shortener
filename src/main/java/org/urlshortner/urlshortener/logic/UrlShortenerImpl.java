package org.urlshortner.urlshortener.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.UUID;

@Component
public class UrlShortenerImpl implements UrlShortener {
    private final static String hashingAlgoritm = "MD5";
    private final static int shortenedUrlKeyLength = 7;

    private final static String allowedSymbolsInUniqueKey = "0123456789qwertyuiopasdfghjklzxcvbnm";
    private MessageDigest messageDigest;

    Logger logger = LoggerFactory.getLogger(UrlShortenerImpl.class);

    UrlShortenerImpl() {
        try {
            this.messageDigest = MessageDigest.getInstance(hashingAlgoritm);
        } catch (NoSuchAlgorithmException exception) {
            logger.error(String.format("No %s algorithm for generating unique keys", hashingAlgoritm));
        }
    }

    @Override
    public String generateUniqueKey(String url) {
        messageDigest.update((url + UUID.randomUUID()).getBytes());
        StringBuilder hashedUrlBuilder = new StringBuilder();
        for (byte currentByte : messageDigest.digest()) {
            hashedUrlBuilder.append(allowedSymbolsInUniqueKey.charAt((int)
                    currentByte & (allowedSymbolsInUniqueKey.length() - 1)));
        }
        String hashedString = hashedUrlBuilder.toString();
        StringBuilder resultBuilder = new StringBuilder();
        Random random = new Random();
        for (int index = 0; index < shortenedUrlKeyLength; ++index) {
            resultBuilder.append(hashedString.charAt(random.nextInt(hashedString.length())));
        }
        return resultBuilder.toString();

    }

}
