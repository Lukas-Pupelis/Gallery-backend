package lt.example.services;

import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lt.example.criteria.PhotoSearchCriteria;
import lt.example.entities.Photo;
import lt.example.entities.Tag;
import lt.example.enums.SortDirection;
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

    public Page<Photo> getPhotos(PhotoSearchCriteria criteria) {
        Sort sort = criteria.getSortDir() == SortDirection.desc
                ? Sort.by(criteria.getSortField()).descending()
                : Sort.by(criteria.getSortField()).ascending();
        Pageable pageable = PageRequest.of(criteria.getPage(), criteria.getSize(), sort);
        return photoRepository.findAllWithTags(pageable);
    }

    public Page<Photo> searchPhotos(PhotoSearchCriteria criteria) {
        Sort sort = criteria.getSortDir() == SortDirection.desc
            ? Sort.by(criteria.getSortField()).descending()
            : Sort.by(criteria.getSortField()).ascending();
        Pageable pageable = PageRequest.of(criteria.getPage(), criteria.getSize(), sort);
        return photoRepository.findAll(PhotoSpecification.buildSpecification(criteria), pageable);
    }
}
