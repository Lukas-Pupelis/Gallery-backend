package lt.example.services;

import java.io.IOException;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lt.example.utilities.ThumbnailUtility;

@Service
@RequiredArgsConstructor
public class ThumbnailService {

    @Cacheable("thumbnails")
    public String createThumbnailBase64(byte[] originalBytes) throws IOException {
        return ThumbnailUtility.createThumbnailBase64(originalBytes);
    }
}