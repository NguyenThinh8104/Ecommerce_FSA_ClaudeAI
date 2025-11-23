package training.g2.service.impl;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import training.g2.service.CloudinaryService;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinary;

    @Value("${cloudinary.folder-root}")
    private String folderRoot;

    private String doUpload(MultipartFile file, Map<String, Object> options) {
        try {
            Map<?, ?> raw = cloudinary.uploader().upload(file.getBytes(), options);
            Object secureUrl = raw.get("secure_url");
            Object url = (secureUrl != null) ? secureUrl : raw.get("url");

            if (!(url instanceof String) || ((String) url).isBlank()) {
                throw new IllegalStateException("Không nhận được URL từ Cloudinary");
            }
            return (String) url;

        } catch (IOException e) {
            throw new RuntimeException("Upload lên Cloudinary thất bại", e);
        }
    }

    @Override
    public List<String> uploadFile(MultipartFile[] files) {
        if (files == null || files.length == 0) {
            throw new IllegalArgumentException("Danh sách file rỗng");
        }

        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file == null || file.isEmpty())
                continue;

            Map<String, Object> options = new HashMap<>();
            options.put("folder", folderRoot);
            options.put("use_filename", true);
            options.put("unique_filename", true);
            options.put("overwrite", false);
            options.put("resource_type", "image");
            options.put("secure", true);

            String url = doUpload(file, options);
            urls.add(url);
        }
        return urls;
    }

    @Override
    public String uploadAvatar(MultipartFile file, Long userId) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File avatar rỗng");
        }
        if (userId == null) {
            throw new IllegalArgumentException("userId không hợp lệ");
        }

        String publicId = folderRoot + "/users/" + userId + "/avatar";

        Map<String, Object> options = new HashMap<>();
        options.put("public_id", publicId);
        options.put("overwrite", true);
        options.put("invalidate", true);
        options.put("resource_type", "image");
        options.put("secure", true);

        return doUpload(file, options);
    }

    @Override
    public String uploadFiles(MultipartFile file, String folder) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File rỗng");
        }
        if (folder == null || folder.isBlank()) {
            throw new IllegalArgumentException("Tên folder không hợp lệ");
        }

        String fullFolder = folderRoot + "/" + folder;

        Map<String, Object> options = new HashMap<>();
        options.put("folder", fullFolder);
        options.put("use_filename", true);
        options.put("unique_filename", true);
        options.put("overwrite", false);
        options.put("resource_type", "image");
        options.put("secure", true);

        return doUpload(file, options);
    }
    @Override
    public String uploadVoucherImg(MultipartFile file, String voucherCode) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File avatar rỗng");
        }
        if (voucherCode == null) {
            throw new IllegalArgumentException("voucherCode không hợp lệ");
        }

        String publicId = folderRoot + "/vouchers/" + voucherCode + "/image";

        Map<String, Object> options = new HashMap<>();
        options.put("public_id", publicId);
        options.put("overwrite", true);
        options.put("invalidate", true);
        options.put("resource_type", "image");
        options.put("secure", true);

        return doUpload(file, options);
    }
}
