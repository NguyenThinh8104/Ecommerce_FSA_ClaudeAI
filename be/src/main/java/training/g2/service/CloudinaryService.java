package training.g2.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {
    List<String> uploadFile(MultipartFile[] file);

    String uploadAvatar(MultipartFile file, Long userId);

    String uploadFiles(MultipartFile files, String folder);
}
