package training.g2.service.impl;

import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import training.g2.dto.Response.Dashboard.*;
import training.g2.model.Order;
import training.g2.model.enums.PaymentStatusEnum;
import training.g2.repository.*;
import training.g2.service.DashboardService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final CategoryRepository categoryRepository;

    public DashboardServiceImpl(OrderRepository orderRepository, ProductRepository productRepository, UserRepository userRepository, OrderDetailRepository orderDetailRepository, CategoryRepository categoryRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public DashboardStatsResDTO getDashboardStats() {
        Double revenue = orderRepository.calulateTotalRevenue(PaymentStatusEnum.PAID);
        double totalRevenue = (revenue != null) ? revenue : 0.0;
        long totalOrders = orderRepository.count();
        long totalProducts = productRepository.countByDeletedFalse();
        long totalUsers = userRepository.countByDeletedFalse();

        List<Order> orders = orderRepository.findRecentOrders(PageRequest.of(0,5));
        List<RecentOrderDTO> recentOrderDTOs = orders.stream().map(this :: mapToRecentOrderDTO).collect(Collectors.toList());

        List<Object[]> topProductsRaw = orderDetailRepository.findTopSellingProducts(PageRequest.of(0,5));
        List<TopProductDTO> topProducts = topProductsRaw.stream().map(obj ->
                TopProductDTO.builder()
                        .name((String) obj[0])
                        .sold((Long) obj[1])
                        .revenue((Double) obj[2])
                        .build()
        ).collect(Collectors.toList());

        List<Object[]> revenueRaw = orderRepository.getMonthlyRevenue();
        List<MonthlyRevenueDTO> monthlyRevenue = revenueRaw.stream().map(obj ->
                MonthlyRevenueDTO.builder()
                        .month(obj[1]+"/"+obj[0])
                        .revenue((Double) obj[2])
                        .build()).
                collect(Collectors.toList());

        List<Object[]> cateRaw = categoryRepository.countProductsByCategory();
        List<CategoryShareDTO> cateShare = cateRaw.stream().map(obj ->
                CategoryShareDTO.builder()
                        .name((String) obj[0])
                        .productCount((Long) obj[1])
                        .build())
                .collect(Collectors.toList());

        return DashboardStatsResDTO.builder()
                .totalRevenue(totalRevenue)
                .totalOrders(totalOrders)
                .totalProducts(totalProducts)
                .totalUsers(totalUsers)
                .recentOrders(recentOrderDTOs)
                .topProducts(topProducts)
                .monthlyRevenue(monthlyRevenue)
                .categoryShare(cateShare)
                .build();
    }

    private RecentOrderDTO mapToRecentOrderDTO(Order order){
        return RecentOrderDTO.builder().
                orderId(order.getId())
                .customerName(order.getUser()!=null ? order.getUser().getFullName(): "Unknown User")
                .totalPrice(order.getTotalPrice())
                .status(order.getOrderStatus() != null ? order.getOrderStatus().name() : "UNKNOWN")
                .createdAt(order.getCreatedAt().toString())
                .build();
    }
}
