package lt.example.services;

import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lt.example.criteria.PhotoSearchCriteria;
import lt.example.entities.Photo;
import lt.example.entities.Tag;
import lt.example.repositories.PhotoRepository;
import lt.example.repositories.TagRepository;
import lt.example.specifications.PhotoSpecification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PhotoService {

    private final PhotoRepository photoRepository;
    private final TagRepository tagRepository;

    public void savePhoto(byte[] photoData, String photoName, String photoDescription, Set<String> tagNames) {
        Photo photo = new Photo();
        photo.setFile(photoData);
        photo.setName(photoName);
        photo.setDescription(photoDescription);

        Set<Tag> tagSet = tagNames.stream()
        .map(tagName -> tagRepository.findByName(tagName)
        .orElseGet(() -> buildTag(tagName)))
        .collect(Collectors.toSet());

        photo.setTags(tagSet);
        photoRepository.save(photo);
    }

    public Tag buildTag(String tagName) {
        Tag tag = new Tag();
        tag.setName(tagName);
        return tagRepository.save(tag);
    }

    public Page<Photo> getPhotos(int page, int size, String sortField, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc")
            ? Sort.by(sortField).descending()
            : Sort.by(sortField).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return photoRepository.findAllWithTags(pageable);
    }

    public Page<Photo> searchPhotos(PhotoSearchCriteria criteria, int page, int size, String sortField, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc")
            ? Sort.by(sortField).descending()
            : Sort.by(sortField).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        PhotoSpecification spec = new PhotoSpecification(criteria);
        return photoRepository.findAll(spec, pageable);
    }
}
