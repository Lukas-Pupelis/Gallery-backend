package lt.example.helpers;

import java.awt.*;
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

        int maxSize = 300;
        BufferedImage scaled = Scalr.resize(
            originalImage,
            Scalr.Method.QUALITY,
            Scalr.Mode.AUTOMATIC,
            maxSize,
            maxSize
        );

        BufferedImage finalThumb = new BufferedImage(
            maxSize,
            maxSize,
            BufferedImage.TYPE_INT_RGB
        );
        Graphics2D g2d = finalThumb.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, maxSize, maxSize);

        int x = (maxSize - scaled.getWidth()) / 2;
        int y = (maxSize - scaled.getHeight()) / 2;
        g2d.drawImage(scaled, x, y, null);
        g2d.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(finalThumb, "png", baos);
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }
}
