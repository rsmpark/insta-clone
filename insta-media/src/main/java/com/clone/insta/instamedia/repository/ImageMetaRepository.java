package com.clone.insta.instamedia.repository;

import com.clone.insta.instamedia.model.ImageMeta;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ImageMetaRepository extends MongoRepository<ImageMeta, String> {
}
