package training.g2.controller.client;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import training.g2.dto.Response.Location.LocationDto;
import training.g2.model.ApiResponse;
import training.g2.model.District;
import training.g2.model.Province;
import training.g2.model.Ward;
import training.g2.repository.DistrictRepository;
import training.g2.repository.ProvinceRepository;
import training.g2.repository.WardRepository;

import java.util.List;

@RestController
@RequestMapping("/api/v1/locations")
@RequiredArgsConstructor
public class LocationController {

        private final ProvinceRepository provinceRepository;
        private final DistrictRepository districtRepository;
        private final WardRepository wardRepository;

        @GetMapping("/provinces")
        @Cacheable("provinces")
        public ApiResponse<List<LocationDto>> getProvinces() {
                List<Province> provinces = provinceRepository.findAll();
                return ApiResponse.<List<LocationDto>>builder()
                                .data(provinces.stream()
                                                .map(p -> new LocationDto(p.getId(), p.getName()))
                                                .toList())
                                .message("Lấy danh sách tỉnh/thành phố thành công")
                                .build();
        }

        @GetMapping("/districts")
        public ApiResponse<List<LocationDto>> getDistricts(@RequestParam("provinceId") Long provinceId) {
                List<District> districts = districtRepository.findByProvinceId(provinceId);
                return ApiResponse.<List<LocationDto>>builder()
                                .data(districts.stream()
                                                .map(d -> new LocationDto(d.getId(), d.getName()))
                                                .toList())
                                .message("Lấy danh sách quận/huyện thành công")
                                .build();
        }

        @GetMapping("/wards")
        public ApiResponse<List<LocationDto>> getWards(@RequestParam("districtId") Long districtId) {
                List<Ward> wards = wardRepository.findByDistrictId(districtId);
                return ApiResponse.<List<LocationDto>>builder()
                                .data(wards.stream()
                                                .map(w -> new LocationDto(w.getId(), w.getName()))
                                                .toList())
                                .message("Lấy danh sách phường/xã thành công")
                                .build();
        }
}
