package lt.example.utilities;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import javax.imageio.ImageIO;
import org.imgscalr.Scalr;

public final class ThumbnailUtility {

    public static final int SIZE = 300;

    private ThumbnailUtility() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static String createThumbnailBase64(byte[] photoData) throws IOException {
        BufferedImage src = ImageIO.read(new ByteArrayInputStream(photoData));
        if (src == null) {
            throw new IOException("Could not read image data");
        }
        BufferedImage resized = Scalr.resize(src, Scalr.Method.QUALITY, Scalr.Mode.AUTOMATIC, SIZE, SIZE);
        return encodeImage(resized);
    }

    private static String encodeImage(BufferedImage image) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", baos);
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        }
    }
}