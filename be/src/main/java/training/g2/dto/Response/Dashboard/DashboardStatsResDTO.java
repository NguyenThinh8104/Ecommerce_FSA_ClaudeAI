package training.g2.dto.response.Dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsResDTO {
    private double totalRevenue;
    private long totalOrders;
    private long totalProducts;
    private long totalUsers;
    private List<RecentOrderDTO> recentOrders;
    private List<MonthlyRevenueDTO> monthlyRevenue;
    private List<TopProductDTO> topProducts;
    private List<CategoryShareDTO> categoryShare;
}
