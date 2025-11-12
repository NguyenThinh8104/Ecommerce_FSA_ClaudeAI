package training.g2.controller.client;

import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import training.g2.model.ApiResponse;
import training.g2.service.CloudinaryService;

@RestController
@RequestMapping("/api/v1/uploads")
public class CloudinaryController {
    private final CloudinaryService cloudinaryService;

    public CloudinaryController(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping(value = "/files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<List<String>> uploadMultiple(
            @RequestPart("files") MultipartFile[] files) {

        List<String> urls = cloudinaryService.uploadFile(files);
        return new ApiResponse<>("Upload thành công", urls);
    }

    @PostMapping(value = "/users/{userId}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<String>> uploadAvatar(
            @PathVariable Long userId,
            @RequestPart("file") MultipartFile file) {

        String url = cloudinaryService.uploadAvatar(file, userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Upload avatar thành công", url));
    }

    @PostMapping(value = "/folders/{folder}/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<String>> uploadToFolder(
            @PathVariable String folder,
            @RequestPart("file") MultipartFile file) {

        String cleanFolder = StringUtils.trimAllWhitespace(folder);
        String url = cloudinaryService.uploadFiles(file, cleanFolder);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Upload thành công", url));
    }
}
