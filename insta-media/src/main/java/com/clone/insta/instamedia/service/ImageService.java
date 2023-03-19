package com.clone.insta.instamedia.service;

import com.clone.insta.instamedia.model.ImageMeta;
import com.clone.insta.instamedia.repository.ImageMetaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class ImageService {

    @Autowired
    private FileService fileService;

    @Autowired
    private ImageMetaRepository imageMetadataRepository;


    public ImageMeta upload(MultipartFile file, String username) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());

        log.info("storing file {}", filename);

        ImageMeta metadata = fileService.store(file, username);
        return imageMetadataRepository.save(metadata);
    }
}