package com.grocart.grocart.Controller;





import com.grocart.grocart.DTO.AnalyticsDTO.*;
import com.grocart.grocart.Services.OrderAnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analytics")
@CrossOrigin(origins = "*")
public class OrderAnalyticsController {

    @Autowired
    private OrderAnalyticsService analyticsService;

    /**
     * Get ALL analytics data in a single API call
     * GET /api/analytics/all
     */
    @GetMapping("/all")
    public ResponseEntity<ComprehensiveAnalyticsDTO> getAllAnalytics(
            @RequestParam(defaultValue = "30") int dailySalesDays,
            @RequestParam(defaultValue = "12") int monthlySalesMonths,
            @RequestParam(defaultValue = "10") int topCustomersLimit,
            @RequestParam(defaultValue = "7") int hourlySalesDays,
            @RequestParam(defaultValue = "10") int recentOrdersLimit) {

        ComprehensiveAnalyticsDTO analytics = analyticsService.getAllAnalytics(
                dailySalesDays,
                monthlySalesMonths,
                topCustomersLimit,
                hourlySalesDays,
                recentOrdersLimit
        );

        return ResponseEntity.ok(analytics);
    }

    /**
     * Get comprehensive dashboard overview with key metrics
     * GET /api/analytics/overview
     */
    @GetMapping("/overview")
    public ResponseEntity<DashboardOverviewDTO> getDashboardOverview() {
        DashboardOverviewDTO overview = analyticsService.getDashboardOverview();
        return ResponseEntity.ok(overview);
    }

    /**
     * Get daily sales data for the last N days
     * GET /api/analytics/daily-sales?days=30
     */
    @GetMapping("/daily-sales")
    public ResponseEntity<List<DailySalesDTO>> getDailySales(
            @RequestParam(defaultValue = "30") int days) {
        List<DailySalesDTO> dailySales = analyticsService.getDailySales(days);
        return ResponseEntity.ok(dailySales);
    }

    /**
     * Get monthly sales data for the last N months
     * GET /api/analytics/monthly-sales?months=12
     */
    @GetMapping("/monthly-sales")
    public ResponseEntity<List<MonthlySalesDTO>> getMonthlySales(
            @RequestParam(defaultValue = "12") int months) {
        List<MonthlySalesDTO> monthlySales = analyticsService.getMonthlySales(months);
        return ResponseEntity.ok(monthlySales);
    }

    /**
     * Get revenue breakdown (subtotal, tax, discount, total)
     * GET /api/analytics/revenue-breakdown
     */
    @GetMapping("/revenue-breakdown")
    public ResponseEntity<RevenueBreakdownDTO> getRevenueBreakdown() {
        RevenueBreakdownDTO breakdown = analyticsService.getRevenueBreakdown();
        return ResponseEntity.ok(breakdown);
    }

    /**
     * Get order status distribution with percentages
     * GET /api/analytics/order-status-distribution
     */
    @GetMapping("/order-status-distribution")
    public ResponseEntity<List<OrderStatusDistributionDTO>> getOrderStatusDistribution() {
        List<OrderStatusDistributionDTO> distribution = analyticsService.getOrderStatusDistribution();
        return ResponseEntity.ok(distribution);
    }

    /**
     * Get top customers by total spending
     * GET /api/analytics/top-customers?limit=10
     */
    @GetMapping("/top-customers")
    public ResponseEntity<List<TopCustomerDTO>> getTopCustomers(
            @RequestParam(defaultValue = "10") int limit) {
        List<TopCustomerDTO> topCustomers = analyticsService.getTopCustomers(limit);
        return ResponseEntity.ok(topCustomers);
    }

    /**
     * Get sales data by province
     * GET /api/analytics/sales-by-province
     */
    @GetMapping("/sales-by-province")
    public ResponseEntity<List<SalesByLocationDTO>> getSalesByProvince() {
        List<SalesByLocationDTO> salesByProvince = analyticsService.getSalesByProvince();
        return ResponseEntity.ok(salesByProvince);
    }

    /**
     * Get sales data by city
     * GET /api/analytics/sales-by-city
     */
    @GetMapping("/sales-by-city")
    public ResponseEntity<List<SalesByLocationDTO>> getSalesByCity() {
        List<SalesByLocationDTO> salesByCity = analyticsService.getSalesByCity();
        return ResponseEntity.ok(salesByCity);
    }

    /**
     * Get growth metrics (month-over-month comparison)
     * GET /api/analytics/growth-metrics
     */
    @GetMapping("/growth-metrics")
    public ResponseEntity<GrowthMetricsDTO> getGrowthMetrics() {
        GrowthMetricsDTO metrics = analyticsService.getGrowthMetrics();
        return ResponseEntity.ok(metrics);
    }

    /**
     * Get hourly sales pattern for the last N days
     * GET /api/analytics/hourly-sales?days=7
     */
    @GetMapping("/hourly-sales")
    public ResponseEntity<List<HourlySalesDTO>> getHourlySalesPattern(
            @RequestParam(defaultValue = "7") int days) {
        List<HourlySalesDTO> hourlySales = analyticsService.getHourlySalesPattern(days);
        return ResponseEntity.ok(hourlySales);
    }

    /**
     * Get recent orders
     * GET /api/analytics/recent-orders?limit=10
     */
    @GetMapping("/recent-orders")
    public ResponseEntity<List<RecentOrderDTO>> getRecentOrders(
            @RequestParam(defaultValue = "10") int limit) {
        List<RecentOrderDTO> recentOrders = analyticsService.getRecentOrders(limit);
        return ResponseEntity.ok(recentOrders);
    }
}