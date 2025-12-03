package com.grocart.grocart.Services;






import com.grocart.grocart.DTO.AnalyticsDTO.*;
import com.grocart.grocart.Entities.Order;

import com.grocart.grocart.Repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderAnalyticsService {

    @Autowired
    private OrderRepository orderRepository;

    // ==================== COMPREHENSIVE ANALYTICS (ALL DATA IN ONE CALL) ====================

    public ComprehensiveAnalyticsDTO getAllAnalytics(
            int dailySalesDays,
            int monthlySalesMonths,
            int topCustomersLimit,
            int hourlySalesDays,
            int recentOrdersLimit) {

        ComprehensiveAnalyticsDTO analytics = new ComprehensiveAnalyticsDTO();

        // Gather all analytics data
        analytics.setOverview(getDashboardOverview());
        analytics.setDailySales(getDailySales(dailySalesDays));
        analytics.setMonthlySales(getMonthlySales(monthlySalesMonths));
        analytics.setRevenueBreakdown(getRevenueBreakdown());
        analytics.setOrderStatusDistribution(getOrderStatusDistribution());
        analytics.setTopCustomers(getTopCustomers(topCustomersLimit));
        analytics.setSalesByProvince(getSalesByProvince());
        analytics.setSalesByCity(getSalesByCity());
        analytics.setGrowthMetrics(getGrowthMetrics());
        analytics.setHourlySales(getHourlySalesPattern(hourlySalesDays));
        analytics.setRecentOrders(getRecentOrders(recentOrdersLimit));

        return analytics;
    }

    // ==================== OVERVIEW METRICS ====================

    public DashboardOverviewDTO getDashboardOverview() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfToday = now.toLocalDate().atStartOfDay();
        LocalDateTime startOfMonth = now.withDayOfMonth(1).toLocalDate().atStartOfDay();
        LocalDateTime startOfYear = now.withDayOfYear(1).toLocalDate().atStartOfDay();

        List<Order> allOrders = orderRepository.findAll();
        List<Order> todayOrders = filterOrdersByDateRange(allOrders, startOfToday, now);
        List<Order> monthOrders = filterOrdersByDateRange(allOrders, startOfMonth, now);
        List<Order> yearOrders = filterOrdersByDateRange(allOrders, startOfYear, now);

        DashboardOverviewDTO overview = new DashboardOverviewDTO();
        overview.setTotalRevenue(calculateTotalRevenue(allOrders));
        overview.setTotalOrders((long) allOrders.size());
        overview.setTodayRevenue(calculateTotalRevenue(todayOrders));
        overview.setTodayOrders((long) todayOrders.size());
        overview.setMonthRevenue(calculateTotalRevenue(monthOrders));
        overview.setMonthOrders((long) monthOrders.size());
        overview.setYearRevenue(calculateTotalRevenue(yearOrders));
        overview.setYearOrders((long) yearOrders.size());
        overview.setAverageOrderValue(calculateAverageOrderValue(allOrders));
        overview.setPendingOrders(countOrdersByStatus(allOrders, "Pending"));
        overview.setCompletedOrders(countOrdersByStatus(allOrders, "Completed"));

        return overview;
    }

    // ==================== DAILY SALES ====================

    public List<DailySalesDTO> getDailySales(int days) {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(days);

        List<Order> orders = orderRepository.findByCreatedAtBetween(startDate, endDate);

        Map<LocalDate, List<Order>> ordersByDate = orders.stream()
                .collect(Collectors.groupingBy(order -> order.getCreatedAt().toLocalDate()));

        List<DailySalesDTO> dailySales = new ArrayList<>();

        for (int i = 0; i < days; i++) {
            LocalDate date = endDate.minusDays(days - 1 - i).toLocalDate();
            List<Order> dayOrders = ordersByDate.getOrDefault(date, new ArrayList<>());

            DailySalesDTO dto = new DailySalesDTO();
            dto.setDate(date);
            dto.setTotalSales(calculateTotalRevenue(dayOrders));
            dto.setOrderCount((long) dayOrders.size());
            dto.setAverageOrderValue(calculateAverageOrderValue(dayOrders));

            dailySales.add(dto);
        }

        return dailySales;
    }

    // ==================== MONTHLY SALES ====================

    public List<MonthlySalesDTO> getMonthlySales(int months) {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusMonths(months);

        List<Order> orders = orderRepository.findByCreatedAtBetween(startDate, endDate);

        Map<YearMonth, List<Order>> ordersByMonth = orders.stream()
                .collect(Collectors.groupingBy(order ->
                        YearMonth.from(order.getCreatedAt())));

        List<MonthlySalesDTO> monthlySales = new ArrayList<>();

        for (int i = 0; i < months; i++) {
            YearMonth yearMonth = YearMonth.from(endDate.minusMonths(months - 1 - i));
            List<Order> monthOrders = ordersByMonth.getOrDefault(yearMonth, new ArrayList<>());

            MonthlySalesDTO dto = new MonthlySalesDTO();
            dto.setYear(yearMonth.getYear());
            dto.setMonth(yearMonth.getMonthValue());
            dto.setMonthName(yearMonth.getMonth().name());
            dto.setTotalSales(calculateTotalRevenue(monthOrders));
            dto.setOrderCount((long) monthOrders.size());
            dto.setAverageOrderValue(calculateAverageOrderValue(monthOrders));

            monthlySales.add(dto);
        }

        return monthlySales;
    }

    // ==================== REVENUE BREAKDOWN ====================

    public RevenueBreakdownDTO getRevenueBreakdown() {
        List<Order> allOrders = orderRepository.findAll();

        RevenueBreakdownDTO breakdown = new RevenueBreakdownDTO();
        breakdown.setTotalSubtotal(allOrders.stream()
                .mapToDouble(o -> o.getSubtotal() != null ? o.getSubtotal() : 0.0)
                .sum());
        breakdown.setTotalTax(allOrders.stream()
                .mapToDouble(o -> o.getTax() != null ? o.getTax() : 0.0)
                .sum());
        breakdown.setTotalDiscount(allOrders.stream()
                .mapToDouble(o -> o.getDiscount() != null ? o.getDiscount() : 0.0)
                .sum());
        breakdown.setTotalRevenue(calculateTotalRevenue(allOrders));

        return breakdown;
    }

    // ==================== ORDER STATUS DISTRIBUTION ====================

    public List<OrderStatusDistributionDTO> getOrderStatusDistribution() {
        List<Order> allOrders = orderRepository.findAll();

        Map<String, Long> statusCount = allOrders.stream()
                .collect(Collectors.groupingBy(
                        order -> order.getStatus() != null ? order.getStatus() : "Unknown",
                        Collectors.counting()
                ));

        return statusCount.entrySet().stream()
                .map(entry -> {
                    OrderStatusDistributionDTO dto = new OrderStatusDistributionDTO();
                    dto.setStatus(entry.getKey());
                    dto.setCount(entry.getValue());
                    dto.setPercentage((entry.getValue() * 100.0) / allOrders.size());
                    return dto;
                })
                .sorted(Comparator.comparing(OrderStatusDistributionDTO::getCount).reversed())
                .collect(Collectors.toList());
    }

    // ==================== TOP CUSTOMERS ====================

    public List<TopCustomerDTO> getTopCustomers(int limit) {
        List<Order> allOrders = orderRepository.findAll();

        Map<String, List<Order>> ordersByCustomer = allOrders.stream()
                .collect(Collectors.groupingBy(Order::getEmail));

        return ordersByCustomer.entrySet().stream()
                .map(entry -> {
                    List<Order> customerOrders = entry.getValue();
                    TopCustomerDTO dto = new TopCustomerDTO();
                    dto.setEmail(entry.getKey());
                    dto.setFirstName(customerOrders.get(0).getFirstName());
                    dto.setLastName(customerOrders.get(0).getLastName());
                    dto.setTotalOrders((long) customerOrders.size());
                    dto.setTotalSpent(calculateTotalRevenue(customerOrders));
                    dto.setAverageOrderValue(calculateAverageOrderValue(customerOrders));
                    return dto;
                })
                .sorted(Comparator.comparing(TopCustomerDTO::getTotalSpent).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    // ==================== LOCATION ANALYTICS ====================

    public List<SalesByLocationDTO> getSalesByProvince() {
        List<Order> allOrders = orderRepository.findAll();

        Map<String, List<Order>> ordersByProvince = allOrders.stream()
                .filter(order -> order.getProvince() != null)
                .collect(Collectors.groupingBy(Order::getProvince));

        return ordersByProvince.entrySet().stream()
                .map(entry -> {
                    List<Order> provinceOrders = entry.getValue();
                    SalesByLocationDTO dto = new SalesByLocationDTO();
                    dto.setLocation(entry.getKey());
                    dto.setOrderCount((long) provinceOrders.size());
                    dto.setTotalRevenue(calculateTotalRevenue(provinceOrders));
                    dto.setAverageOrderValue(calculateAverageOrderValue(provinceOrders));
                    return dto;
                })
                .sorted(Comparator.comparing(SalesByLocationDTO::getTotalRevenue).reversed())
                .collect(Collectors.toList());
    }

    public List<SalesByLocationDTO> getSalesByCity() {
        List<Order> allOrders = orderRepository.findAll();

        Map<String, List<Order>> ordersByCity = allOrders.stream()
                .filter(order -> order.getCity() != null)
                .collect(Collectors.groupingBy(Order::getCity));

        return ordersByCity.entrySet().stream()
                .map(entry -> {
                    List<Order> cityOrders = entry.getValue();
                    SalesByLocationDTO dto = new SalesByLocationDTO();
                    dto.setLocation(entry.getKey());
                    dto.setOrderCount((long) cityOrders.size());
                    dto.setTotalRevenue(calculateTotalRevenue(cityOrders));
                    dto.setAverageOrderValue(calculateAverageOrderValue(cityOrders));
                    return dto;
                })
                .sorted(Comparator.comparing(SalesByLocationDTO::getTotalRevenue).reversed())
                .collect(Collectors.toList());
    }

    // ==================== GROWTH METRICS ====================

    public GrowthMetricsDTO getGrowthMetrics() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfCurrentMonth = now.withDayOfMonth(1).toLocalDate().atStartOfDay();
        LocalDateTime startOfLastMonth = startOfCurrentMonth.minusMonths(1);

        List<Order> currentMonthOrders = orderRepository.findByCreatedAtBetween(startOfCurrentMonth, now);
        List<Order> lastMonthOrders = orderRepository.findByCreatedAtBetween(startOfLastMonth, startOfCurrentMonth);

        double currentMonthRevenue = calculateTotalRevenue(currentMonthOrders);
        double lastMonthRevenue = calculateTotalRevenue(lastMonthOrders);

        GrowthMetricsDTO metrics = new GrowthMetricsDTO();
        metrics.setCurrentMonthRevenue(currentMonthRevenue);
        metrics.setLastMonthRevenue(lastMonthRevenue);
        metrics.setRevenueGrowthRate(calculateGrowthRate(lastMonthRevenue, currentMonthRevenue));
        metrics.setCurrentMonthOrders((long) currentMonthOrders.size());
        metrics.setLastMonthOrders((long) lastMonthOrders.size());
        metrics.setOrderGrowthRate(calculateGrowthRate(lastMonthOrders.size(), currentMonthOrders.size()));

        return metrics;
    }

    // ==================== HOURLY SALES PATTERN ====================

    public List<HourlySalesDTO> getHourlySalesPattern(int days) {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(days);

        List<Order> orders = orderRepository.findByCreatedAtBetween(startDate, endDate);

        Map<Integer, List<Order>> ordersByHour = orders.stream()
                .collect(Collectors.groupingBy(order -> order.getCreatedAt().getHour()));

        List<HourlySalesDTO> hourlySales = new ArrayList<>();

        for (int hour = 0; hour < 24; hour++) {
            List<Order> hourOrders = ordersByHour.getOrDefault(hour, new ArrayList<>());

            HourlySalesDTO dto = new HourlySalesDTO();
            dto.setHour(hour);
            dto.setOrderCount((long) hourOrders.size());
            dto.setTotalRevenue(calculateTotalRevenue(hourOrders));
            dto.setAverageOrderValue(calculateAverageOrderValue(hourOrders));

            hourlySales.add(dto);
        }

        return hourlySales;
    }

    // ==================== RECENT ORDERS ====================

    public List<RecentOrderDTO> getRecentOrders(int limit) {
        List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .sorted(Comparator.comparing(Order::getCreatedAt).reversed())
                .limit(limit)
                .map(order -> {
                    RecentOrderDTO dto = new RecentOrderDTO();
                    dto.setOrderId(order.getId());
                    dto.setTrackingId(order.getTrackingId());
                    dto.setCustomerName(order.getFirstName() + " " + order.getLastName());
                    dto.setEmail(order.getEmail());
                    dto.setTotal(order.getTotal());
                    dto.setStatus(order.getStatus());
                    dto.setCreatedAt(order.getCreatedAt());
                    dto.setItemCount(order.getItems() != null ? order.getItems().size() : 0);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // ==================== HELPER METHODS ====================

    private List<Order> filterOrdersByDateRange(List<Order> orders, LocalDateTime start, LocalDateTime end) {
        return orders.stream()
                .filter(order -> !order.getCreatedAt().isBefore(start) && !order.getCreatedAt().isAfter(end))
                .collect(Collectors.toList());
    }

    private Double calculateTotalRevenue(List<Order> orders) {
        return orders.stream()
                .mapToDouble(order -> order.getTotal() != null ? order.getTotal() : 0.0)
                .sum();
    }

    private Double calculateAverageOrderValue(List<Order> orders) {
        if (orders.isEmpty()) return 0.0;
        return calculateTotalRevenue(orders) / orders.size();
    }

    private Long countOrdersByStatus(List<Order> orders, String status) {
        return orders.stream()
                .filter(order -> status.equalsIgnoreCase(order.getStatus()))
                .count();
    }

    private Double calculateGrowthRate(double oldValue, double newValue) {
        if (oldValue == 0) return newValue > 0 ? 100.0 : 0.0;
        return ((newValue - oldValue) / oldValue) * 100.0;
    }
}