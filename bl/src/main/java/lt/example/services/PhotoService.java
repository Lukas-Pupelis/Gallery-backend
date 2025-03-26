package lt.example.services;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lt.example.criteria.PhotoSearchCriteria;
import lt.example.entities.Photo;
import lt.example.entities.Tag;
import lt.example.enums.SortDirection;
import lt.example.enums.SortField;
import lt.example.repositories.PhotoRepository;
import lt.example.repositories.TagRepository;
import lt.example.specifications.PhotoSpecification;
import lt.example.utilities.ThumbnailUtility;

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

    public void savePhoto(byte[] photoData, String photoName, String photoDescription, Set<String> tagNames) throws IOException {
        Photo photo = new Photo();
        photo.setFile(photoData);
        photo.setName(photoName);
        photo.setDescription(photoDescription);
        photo.setThumbnail(ThumbnailUtility.createThumbnailBase64(photoData));

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

    public String generateAndSaveThumbnail(Long photoId, byte[] file) {
        Photo photo = photoRepository.findById(photoId)
        .orElseThrow(() -> new RuntimeException("Photo not found for id: " + photoId));

        if (photo.getThumbnail() == null) {
            try {
                String generatedThumbnail = ThumbnailUtility.createThumbnailBase64(file);
                photo.setThumbnail(generatedThumbnail);

                photoRepository.save(photo);
                return generatedThumbnail;
            } catch (IOException ex) {
                throw new RuntimeException("Error generating thumbnail for photo id " + photoId, ex);
            }
        }
        return photo.getThumbnail();
    }

    public Page<Photo> getPhotos(PhotoSearchCriteria criteria) {
        String sortField = mapSortField(criteria.getSortField());
        Sort sort = criteria.getSortDir() == SortDirection.DESCENDING
                ? Sort.by(sortField).descending()
                : Sort.by(sortField).ascending();
        Pageable pageable = PageRequest.of(criteria.getPage(), criteria.getSize(), sort);
        return photoRepository.findAll(pageable);
    }

    public Page<Photo> searchPhotos(PhotoSearchCriteria criteria) {
        String sortField = mapSortField(criteria.getSortField());
        Sort sort = criteria.getSortDir() == SortDirection.DESCENDING
                ? Sort.by(sortField).descending()
                : Sort.by(sortField).ascending();
        Pageable pageable = PageRequest.of(criteria.getPage(), criteria.getSize(), sort);

        return photoRepository.findAll(
            PhotoSpecification.buildSpecification(criteria),
            pageable
        );
    }

    private String mapSortField(SortField sortField) {
        return switch (sortField) {
            case ID -> "id";
            case NAME -> "name";
            case DESCRIPTION -> "description";
            case DATE -> "createdAt";
        };
    }
}