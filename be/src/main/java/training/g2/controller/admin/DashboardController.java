package training.g2.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import training.g2.dto.Response.Dashboard.DashboardStatsResDTO;
import training.g2.model.ApiResponse;
import training.g2.service.DashboardService;

import static training.g2.constant.Constants.Message.GENERAL_SUCCESS_MESSAGE;

@RestController
@RequestMapping("/api/v1/admin/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public ApiResponse<DashboardStatsResDTO> getDashboardStats() {
        DashboardStatsResDTO stats = dashboardService.getDashboardStats();
        return new ApiResponse<>(GENERAL_SUCCESS_MESSAGE, stats);
    }


}
