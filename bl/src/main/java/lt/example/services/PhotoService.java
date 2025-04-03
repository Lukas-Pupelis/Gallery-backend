package lt.example.services;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.persistence.metamodel.SingularAttribute;
import lombok.RequiredArgsConstructor;
import lt.example.criteria.PhotoSearchCriteria;
import lt.example.entities.Photo;
import lt.example.entities.Tag;
import lt.example.enums.SortDirection;
import lt.example.model.PhotoListModel;
import lt.example.repositories.PhotoRepository;
import lt.example.repositories.TagRepository;
import lt.example.specifications.PhotoSpecification;
import lt.example.utilities.ThumbnailUtility;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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

    public Page<PhotoListModel> searchPhotos(PhotoSearchCriteria criteria) {
        SingularAttribute<Photo, ?> sortAttribute = criteria.getSortField().getSortAttribute().get();
        String sortField = sortAttribute.getName();

        String sortDir = criteria.getSortDir().getDirectionName();
        Sort sort = sortDir.equalsIgnoreCase(SortDirection.DESCENDING.getDirectionName())
                ? Sort.by(sortField).descending()
                : Sort.by(sortField).ascending();
        Pageable pageable = PageRequest.of(criteria.getPage(), criteria.getSize(), sort);

        Specification<Photo> spec = PhotoSpecification.buildSpecification(criteria);
        return photoRepository.findPhotoListBySpec(spec, pageable);
    }
}