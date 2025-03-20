package lt.example.helpers;

import java.awt.Color;
import java.awt.Graphics2D;
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
    final int MAX_SIZE = 300;

    public String createThumbnailBase64(byte[] photoData) throws IOException {
        BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(photoData));
        if (originalImage == null) {
            throw new IOException("Could not read image data");
        }

        BufferedImage scaled = Scalr.resize(
            originalImage,
            Scalr.Method.QUALITY,
            Scalr.Mode.AUTOMATIC,
            MAX_SIZE,
            MAX_SIZE
        );

        BufferedImage finalThumb = new BufferedImage(
            MAX_SIZE,
            MAX_SIZE,
            BufferedImage.TYPE_INT_RGB
        );
        Graphics2D g2d = finalThumb.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, MAX_SIZE, MAX_SIZE);

        int x = (MAX_SIZE - scaled.getWidth()) / 2;
        int y = (MAX_SIZE - scaled.getHeight()) / 2;
        g2d.drawImage(scaled, x, y, null);
        g2d.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(finalThumb, "png", baos);
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }
}
