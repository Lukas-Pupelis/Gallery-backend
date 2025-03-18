package lt.example.helpers;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.springframework.stereotype.Component;

@Component
public class ThumbnailHelper {

    public String createThumbnailBase64(byte[] photoData) throws IOException {
        BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(photoData));
        if (originalImage == null) {
            throw new IOException("Could not read image data");
        }

        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

        int newWidth, newHeight;
        if (originalWidth >= originalHeight) {
            newHeight = 300;
            newWidth = (int) (originalWidth * (300.0 / originalHeight));
        } else {
            newWidth = 300;
            newHeight = (int) (originalHeight * (300.0 / originalWidth));
        }

        BufferedImage scaled = Scalr.resize(
                originalImage,
                Scalr.Method.QUALITY,
                newWidth,
                newHeight
        );

        int x = (scaled.getWidth() - 300) / 2;
        int y = (scaled.getHeight() - 300) / 2;
        BufferedImage finalThumb = Scalr.crop(scaled, x, y, 300, 300);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(finalThumb, "png", baos);
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }
}


