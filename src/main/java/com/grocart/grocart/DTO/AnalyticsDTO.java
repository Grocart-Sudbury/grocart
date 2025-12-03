package com.grocart.grocart.DTO;





import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class AnalyticsDTO {

    // ==================== COMPREHENSIVE ANALYTICS DTO ====================
    public static class ComprehensiveAnalyticsDTO {
        private DashboardOverviewDTO overview;
        private List<DailySalesDTO> dailySales;
        private List<MonthlySalesDTO> monthlySales;
        private RevenueBreakdownDTO revenueBreakdown;
        private List<OrderStatusDistributionDTO> orderStatusDistribution;
        private List<TopCustomerDTO> topCustomers;
        private List<SalesByLocationDTO> salesByProvince;
        private List<SalesByLocationDTO> salesByCity;
        private GrowthMetricsDTO growthMetrics;
        private List<HourlySalesDTO> hourlySales;
        private List<RecentOrderDTO> recentOrders;

        // Getters and Setters
        public DashboardOverviewDTO getOverview() { return overview; }
        public void setOverview(DashboardOverviewDTO overview) { this.overview = overview; }

        public List<DailySalesDTO> getDailySales() { return dailySales; }
        public void setDailySales(List<DailySalesDTO> dailySales) { this.dailySales = dailySales; }

        public List<MonthlySalesDTO> getMonthlySales() { return monthlySales; }
        public void setMonthlySales(List<MonthlySalesDTO> monthlySales) { this.monthlySales = monthlySales; }

        public RevenueBreakdownDTO getRevenueBreakdown() { return revenueBreakdown; }
        public void setRevenueBreakdown(RevenueBreakdownDTO revenueBreakdown) { this.revenueBreakdown = revenueBreakdown; }

        public List<OrderStatusDistributionDTO> getOrderStatusDistribution() { return orderStatusDistribution; }
        public void setOrderStatusDistribution(List<OrderStatusDistributionDTO> orderStatusDistribution) {
            this.orderStatusDistribution = orderStatusDistribution;
        }

        public List<TopCustomerDTO> getTopCustomers() { return topCustomers; }
        public void setTopCustomers(List<TopCustomerDTO> topCustomers) { this.topCustomers = topCustomers; }

        public List<SalesByLocationDTO> getSalesByProvince() { return salesByProvince; }
        public void setSalesByProvince(List<SalesByLocationDTO> salesByProvince) { this.salesByProvince = salesByProvince; }

        public List<SalesByLocationDTO> getSalesByCity() { return salesByCity; }
        public void setSalesByCity(List<SalesByLocationDTO> salesByCity) { this.salesByCity = salesByCity; }

        public GrowthMetricsDTO getGrowthMetrics() { return growthMetrics; }
        public void setGrowthMetrics(GrowthMetricsDTO growthMetrics) { this.growthMetrics = growthMetrics; }

        public List<HourlySalesDTO> getHourlySales() { return hourlySales; }
        public void setHourlySales(List<HourlySalesDTO> hourlySales) { this.hourlySales = hourlySales; }

        public List<RecentOrderDTO> getRecentOrders() { return recentOrders; }
        public void setRecentOrders(List<RecentOrderDTO> recentOrders) { this.recentOrders = recentOrders; }
    }

    // ==================== DASHBOARD OVERVIEW DTO ====================
    public static class DashboardOverviewDTO {
        private Double totalRevenue;
        private Long totalOrders;
        private Double todayRevenue;
        private Long todayOrders;
        private Double monthRevenue;
        private Long monthOrders;
        private Double yearRevenue;
        private Long yearOrders;
        private Double averageOrderValue;
        private Long pendingOrders;
        private Long completedOrders;

        // Getters and Setters
        public Double getTotalRevenue() { return totalRevenue; }
        public void setTotalRevenue(Double totalRevenue) { this.totalRevenue = totalRevenue; }

        public Long getTotalOrders() { return totalOrders; }
        public void setTotalOrders(Long totalOrders) { this.totalOrders = totalOrders; }

        public Double getTodayRevenue() { return todayRevenue; }
        public void setTodayRevenue(Double todayRevenue) { this.todayRevenue = todayRevenue; }

        public Long getTodayOrders() { return todayOrders; }
        public void setTodayOrders(Long todayOrders) { this.todayOrders = todayOrders; }

        public Double getMonthRevenue() { return monthRevenue; }
        public void setMonthRevenue(Double monthRevenue) { this.monthRevenue = monthRevenue; }

        public Long getMonthOrders() { return monthOrders; }
        public void setMonthOrders(Long monthOrders) { this.monthOrders = monthOrders; }

        public Double getYearRevenue() { return yearRevenue; }
        public void setYearRevenue(Double yearRevenue) { this.yearRevenue = yearRevenue; }

        public Long getYearOrders() { return yearOrders; }
        public void setYearOrders(Long yearOrders) { this.yearOrders = yearOrders; }

        public Double getAverageOrderValue() { return averageOrderValue; }
        public void setAverageOrderValue(Double averageOrderValue) { this.averageOrderValue = averageOrderValue; }

        public Long getPendingOrders() { return pendingOrders; }
        public void setPendingOrders(Long pendingOrders) { this.pendingOrders = pendingOrders; }

        public Long getCompletedOrders() { return completedOrders; }
        public void setCompletedOrders(Long completedOrders) { this.completedOrders = completedOrders; }
    }

    // ==================== DAILY SALES DTO ====================
    public static class DailySalesDTO {
        private LocalDate date;
        private Double totalSales;
        private Long orderCount;
        private Double averageOrderValue;

        // Getters and Setters
        public LocalDate getDate() { return date; }
        public void setDate(LocalDate date) { this.date = date; }

        public Double getTotalSales() { return totalSales; }
        public void setTotalSales(Double totalSales) { this.totalSales = totalSales; }

        public Long getOrderCount() { return orderCount; }
        public void setOrderCount(Long orderCount) { this.orderCount = orderCount; }

        public Double getAverageOrderValue() { return averageOrderValue; }
        public void setAverageOrderValue(Double averageOrderValue) { this.averageOrderValue = averageOrderValue; }
    }

    // ==================== MONTHLY SALES DTO ====================
    public static class MonthlySalesDTO {
        private Integer year;
        private Integer month;
        private String monthName;
        private Double totalSales;
        private Long orderCount;
        private Double averageOrderValue;

        // Getters and Setters
        public Integer getYear() { return year; }
        public void setYear(Integer year) { this.year = year; }

        public Integer getMonth() { return month; }
        public void setMonth(Integer month) { this.month = month; }

        public String getMonthName() { return monthName; }
        public void setMonthName(String monthName) { this.monthName = monthName; }

        public Double getTotalSales() { return totalSales; }
        public void setTotalSales(Double totalSales) { this.totalSales = totalSales; }

        public Long getOrderCount() { return orderCount; }
        public void setOrderCount(Long orderCount) { this.orderCount = orderCount; }

        public Double getAverageOrderValue() { return averageOrderValue; }
        public void setAverageOrderValue(Double averageOrderValue) { this.averageOrderValue = averageOrderValue; }
    }

    // ==================== REVENUE BREAKDOWN DTO ====================
    public static class RevenueBreakdownDTO {
        private Double totalSubtotal;
        private Double totalTax;
        private Double totalDiscount;
        private Double totalRevenue;

        // Getters and Setters
        public Double getTotalSubtotal() { return totalSubtotal; }
        public void setTotalSubtotal(Double totalSubtotal) { this.totalSubtotal = totalSubtotal; }

        public Double getTotalTax() { return totalTax; }
        public void setTotalTax(Double totalTax) { this.totalTax = totalTax; }

        public Double getTotalDiscount() { return totalDiscount; }
        public void setTotalDiscount(Double totalDiscount) { this.totalDiscount = totalDiscount; }

        public Double getTotalRevenue() { return totalRevenue; }
        public void setTotalRevenue(Double totalRevenue) { this.totalRevenue = totalRevenue; }
    }

    // ==================== ORDER STATUS DISTRIBUTION DTO ====================
    public static class OrderStatusDistributionDTO {
        private String status;
        private Long count;
        private Double percentage;

        // Getters and Setters
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public Long getCount() { return count; }
        public void setCount(Long count) { this.count = count; }

        public Double getPercentage() { return percentage; }
        public void setPercentage(Double percentage) { this.percentage = percentage; }
    }

    // ==================== TOP CUSTOMER DTO ====================
    public static class TopCustomerDTO {
        private String email;
        private String firstName;
        private String lastName;
        private Long totalOrders;
        private Double totalSpent;
        private Double averageOrderValue;

        // Getters and Setters
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }

        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }

        public Long getTotalOrders() { return totalOrders; }
        public void setTotalOrders(Long totalOrders) { this.totalOrders = totalOrders; }

        public Double getTotalSpent() { return totalSpent; }
        public void setTotalSpent(Double totalSpent) { this.totalSpent = totalSpent; }

        public Double getAverageOrderValue() { return averageOrderValue; }
        public void setAverageOrderValue(Double averageOrderValue) { this.averageOrderValue = averageOrderValue; }
    }

    // ==================== SALES BY LOCATION DTO ====================
    public static class SalesByLocationDTO {
        private String location;
        private Long orderCount;
        private Double totalRevenue;
        private Double averageOrderValue;

        // Getters and Setters
        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }

        public Long getOrderCount() { return orderCount; }
        public void setOrderCount(Long orderCount) { this.orderCount = orderCount; }

        public Double getTotalRevenue() { return totalRevenue; }
        public void setTotalRevenue(Double totalRevenue) { this.totalRevenue = totalRevenue; }

        public Double getAverageOrderValue() { return averageOrderValue; }
        public void setAverageOrderValue(Double averageOrderValue) { this.averageOrderValue = averageOrderValue; }
    }

    // ==================== GROWTH METRICS DTO ====================
    public static class GrowthMetricsDTO {
        private Double currentMonthRevenue;
        private Double lastMonthRevenue;
        private Double revenueGrowthRate;
        private Long currentMonthOrders;
        private Long lastMonthOrders;
        private Double orderGrowthRate;

        // Getters and Setters
        public Double getCurrentMonthRevenue() { return currentMonthRevenue; }
        public void setCurrentMonthRevenue(Double currentMonthRevenue) { this.currentMonthRevenue = currentMonthRevenue; }

        public Double getLastMonthRevenue() { return lastMonthRevenue; }
        public void setLastMonthRevenue(Double lastMonthRevenue) { this.lastMonthRevenue = lastMonthRevenue; }

        public Double getRevenueGrowthRate() { return revenueGrowthRate; }
        public void setRevenueGrowthRate(Double revenueGrowthRate) { this.revenueGrowthRate = revenueGrowthRate; }

        public Long getCurrentMonthOrders() { return currentMonthOrders; }
        public void setCurrentMonthOrders(Long currentMonthOrders) { this.currentMonthOrders = currentMonthOrders; }

        public Long getLastMonthOrders() { return lastMonthOrders; }
        public void setLastMonthOrders(Long lastMonthOrders) { this.lastMonthOrders = lastMonthOrders; }

        public Double getOrderGrowthRate() { return orderGrowthRate; }
        public void setOrderGrowthRate(Double orderGrowthRate) { this.orderGrowthRate = orderGrowthRate; }
    }

    // ==================== HOURLY SALES DTO ====================
    public static class HourlySalesDTO {
        private Integer hour;
        private Long orderCount;
        private Double totalRevenue;
        private Double averageOrderValue;

        // Getters and Setters
        public Integer getHour() { return hour; }
        public void setHour(Integer hour) { this.hour = hour; }

        public Long getOrderCount() { return orderCount; }
        public void setOrderCount(Long orderCount) { this.orderCount = orderCount; }

        public Double getTotalRevenue() { return totalRevenue; }
        public void setTotalRevenue(Double totalRevenue) { this.totalRevenue = totalRevenue; }

        public Double getAverageOrderValue() { return averageOrderValue; }
        public void setAverageOrderValue(Double averageOrderValue) { this.averageOrderValue = averageOrderValue; }
    }

    // ==================== RECENT ORDER DTO ====================
    public static class RecentOrderDTO {
        private Long orderId;
        private String trackingId;
        private String customerName;
        private String email;
        private Double total;
        private String status;
        private LocalDateTime createdAt;
        private Integer itemCount;

        // Getters and Setters
        public Long getOrderId() { return orderId; }
        public void setOrderId(Long orderId) { this.orderId = orderId; }

        public String getTrackingId() { return trackingId; }
        public void setTrackingId(String trackingId) { this.trackingId = trackingId; }

        public String getCustomerName() { return customerName; }
        public void setCustomerName(String customerName) { this.customerName = customerName; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public Double getTotal() { return total; }
        public void setTotal(Double total) { this.total = total; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

        public Integer getItemCount() { return itemCount; }
        public void setItemCount(Integer itemCount) { this.itemCount = itemCount; }
    }
}