package com.clone.insta.instamedia.service;

import com.clone.insta.instamedia.exception.InvalidFileException;
import com.clone.insta.instamedia.exception.InvalidFileNameException;
import com.clone.insta.instamedia.exception.StorageException;
import com.clone.insta.instamedia.model.ImageMeta;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Slf4j
@Service
public class FileService {
    @Value("${file.upload-dir}")
    private String uploadDirectory;

    @Value("${file.path.prefix}")
    private String filePathPrefix;

    @Autowired
    private Environment environment;


    public ImageMeta store(MultipartFile file, String username) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());

        log.info("storing file {}", filename);

        try {
            if (file.isEmpty()) {
                log.warn("Failed to store empty file {}", filename);
                // TODO: Create InvalidFileException exception
                throw new InvalidFileException("Failed to store empty file " + filename);
            }

            if (filename.contains("..")) {
                // This is a security check
                log.warn("Cannot store file with relative path {}", filename);
                throw new InvalidFileNameException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }

            String extension = FilenameUtils.getExtension(filename);
            String newFilename = UUID.randomUUID() + "." + extension;

            try (InputStream inputStream = file.getInputStream()) {
                Path userDir = Paths.get(uploadDirectory + username);

                if (Files.notExists(userDir)) {
                    Files.createDirectory(userDir);
                }

                Files.copy(inputStream, userDir.resolve(newFilename),
                        StandardCopyOption.REPLACE_EXISTING);
            }

            String port = environment.getProperty("local.server.port");
            String hostName = InetAddress.getLocalHost().getHostName();

            String fileUrl = String.format("http://%s:%s%s/%s/%s",
                    hostName, port, filePathPrefix, username, newFilename);

            log.info("Successfully stored file {} location {}", filename, fileUrl);

            return new ImageMeta(newFilename, fileUrl, file.getContentType());
        } catch (IOException e) {
            log.error("Failed to store file {} error: {}", filename, e);
            // TODO: Create StorageException exception
            throw new StorageException("Failed to store file " + filename, e);
        }
    }
}
