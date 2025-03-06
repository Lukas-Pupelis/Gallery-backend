package lt.example.services;

import java.util.HashSet;
import java.util.Set;
import lt.example.entities.Photo;
import lt.example.entities.Tag;
import lt.example.repositories.PhotoRepository;
import lt.example.repositories.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PhotoService {

    private final PhotoRepository photoRepository;
    private final TagRepository tagRepository;

    public PhotoService(PhotoRepository photoRepository, TagRepository tagRepository) {
        this.photoRepository = photoRepository;
        this.tagRepository = tagRepository;
    }

    public Photo savePhoto(byte[] photoData, Set<String> tagNames) {
        Photo photo = new Photo();
        photo.setPhoto(photoData);

        Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tagNames) {
            tagName = tagName.trim();
            if (!tagName.isEmpty()) {
                Tag tag = tagRepository.findByName(tagName);
                if (tag == null) {
                    tag = new Tag();
                    tag.setName(tagName);
                    tag = tagRepository.save(tag);
                }
                tagSet.add(tag);
            }
        }
        photo.setTags(tagSet);
        return photoRepository.save(photo);
    }
}
