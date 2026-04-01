package com.rental.config;

import com.rental.entity.Vehicle;
import com.rental.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(2) // Run after DataSeeder (Order 1 or default)
public class MaintenanceSyncRunner implements CommandLineRunner {

    private final VehicleRepository vehicleRepository;

    @Override
    @Transactional
    public void run(String... args) {
        log.info(">>> Khởi chạy đồng bộ trạng thái bảo trì phương tiện...");
        
        List<Vehicle> vehicles = vehicleRepository.findAll();
        int updateCount = 0;

        for (Vehicle vehicle : vehicles) {
            // Chỉ kiểm tra các xe đang Available hoặc Rented (không kiểm tra xe đã Maintenance hoặc Broken)
            if (vehicle.getStatus() == Vehicle.Status.Available || vehicle.getStatus() == Vehicle.Status.Rented) {
                int mileage = vehicle.getMileage() != null ? vehicle.getMileage() : 0;
                int lastMaintenance = vehicle.getLastMaintenanceMileage() != null ? vehicle.getLastMaintenanceMileage() : 0;
                
                if (mileage - lastMaintenance >= 5000) {
                    log.info("Vehicle ID {}: Cần bảo trì (Mileage: {}, Last: {})", 
                        vehicle.getVehicleId(), mileage, lastMaintenance);
                    
                    // Nếu xe đang Available thì chuyển thẳng sang Maintenance
                    // Nếu xe đang Rented thì cứ để khách dùng xong (Completed sẽ tự chuyển sau), 
                    // nhưng ở đây ta có thể đánh dấu hoặc xử lý tùy logic. 
                    // Theo yêu cầu "chuyển các xe... sang trạng thái đang bảo trì", ta ưu tiên xe Available.
                    
                    if (vehicle.getStatus() == Vehicle.Status.Available) {
                        vehicle.setStatus(Vehicle.Status.Maintenance);
                        updateCount++;
                    }
                }
            }
        }

        if (updateCount > 0) {
            vehicleRepository.saveAll(vehicles);
            log.info(">>> Đã cập nhật {} phương tiện sang trạng thái bảo trì.", updateCount);
        } else {
            log.info(">>> Không có phương tiện nào cần cập nhật trạng thái bảo trì ngay lúc này.");
        }
    }
}
