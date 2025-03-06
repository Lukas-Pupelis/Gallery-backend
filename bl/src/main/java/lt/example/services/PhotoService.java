package lt.example.services;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lt.example.entities.Photo;
import lt.example.entities.Tag;
import lt.example.repositories.PhotoRepository;
import lt.example.repositories.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PhotoService {

    private final PhotoRepository photoRepository;
    private final TagRepository tagRepository;

    public Photo savePhoto(byte[] photoData, String photoName, String photoDescription, Set<String> tagNames) {
        Photo photo = new Photo();
        photo.setPhoto(photoData);
        photo.setPhotoName(photoName);
        photo.setPhotoDescription(photoDescription);

        Set<Tag> tagSet = tagNames.stream()
        .map(String::trim)
        .filter(tagName -> !tagName.isEmpty())
        .map(tagName -> Optional.ofNullable(tagRepository.findByName(tagName))
            .orElseGet(() -> {
                Tag tag = new Tag();
                tag.setName(tagName);
                return tagRepository.save(tag);
            }))
        .collect(Collectors.toSet());

        photo.setTags(tagSet);
        return photoRepository.save(photo);
    }
}
