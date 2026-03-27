package com.rental.config;

import com.rental.entity.*;
import com.rental.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final LocationRepository locationRepo;
    private final VehicleTypeRepository typeRepo;
    private final VehicleRepository vehicleRepo;
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepo.count() > 0) return; // Chỉ seed lần đầu

        // Roles - kiểm tra trước khi tạo
        Role adminRole = roleRepo.findByRoleName("ADMIN")
                .orElseGet(() -> roleRepo.save(Role.builder().roleName("ADMIN").build()));
        Role staffRole = roleRepo.findByRoleName("STAFF")
                .orElseGet(() -> roleRepo.save(Role.builder().roleName("STAFF").build()));
        Role customerRole = roleRepo.findByRoleName("CUSTOMER")
                .orElseGet(() -> roleRepo.save(Role.builder().roleName("CUSTOMER").build()));

        // Locations
        if (locationRepo.count() == 0) {
            Location hcm = locationRepo.save(Location.builder()
                    .branchName("Chi nhánh TP. Hồ Chí Minh")
                    .address("123 Nguyễn Huệ").city("TP. Hồ Chí Minh").phone("0281234567").build());
            Location hn = locationRepo.save(Location.builder()
                    .branchName("Chi nhánh Hà Nội")
                    .address("45 Hoàn Kiếm").city("Hà Nội").phone("0241234567").build());
            Location dn = locationRepo.save(Location.builder()
                    .branchName("Chi nhánh Đà Nẵng")
                    .address("78 Bạch Đằng").city("Đà Nẵng").phone("0236123456").build());

            // Vehicle Types
            VehicleType sedan = typeRepo.findByTypeName("Sedan")
                    .orElseGet(() -> typeRepo.save(VehicleType.builder().typeName("Sedan").build()));
            VehicleType suv = typeRepo.findByTypeName("SUV")
                    .orElseGet(() -> typeRepo.save(VehicleType.builder().typeName("SUV").build()));
            VehicleType hatch = typeRepo.findByTypeName("Hatchback")
                    .orElseGet(() -> typeRepo.save(VehicleType.builder().typeName("Hatchback").build()));

            // Vehicles
            vehicleRepo.save(Vehicle.builder().type(sedan).currentLocation(hcm).licensePlate("51A-12345")
                    .name("Toyota Vios").brand("Toyota").manufactureYear(2022).mileage(15000)
                    .pricePerDay(new BigDecimal("600000")).status(Vehicle.Status.Available).build());
            vehicleRepo.save(Vehicle.builder().type(suv).currentLocation(hcm).licensePlate("51B-67890")
                    .name("Honda CR-V").brand("Honda").manufactureYear(2023).mileage(8000)
                    .pricePerDay(new BigDecimal("1200000")).status(Vehicle.Status.Available).build());
            vehicleRepo.save(Vehicle.builder().type(hatch).currentLocation(hn).licensePlate("29H-11111")
                    .name("Mazda 3").brand("Mazda").manufactureYear(2023).mileage(5000)
                    .pricePerDay(new BigDecimal("800000")).status(Vehicle.Status.Available).build());
            vehicleRepo.save(Vehicle.builder().type(suv).currentLocation(dn).licensePlate("43A-22222")
                    .name("Ford Everest").brand("Ford").manufactureYear(2022).mileage(20000)
                    .pricePerDay(new BigDecimal("1500000")).status(Vehicle.Status.Available).build());
        }

        // Admin user
        if (!userRepo.findByUsername("admin").isPresent()) {
            userRepo.save(User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .fullName("Quản trị viên")
                    .email("admin@xenow.vn")
                    .phone("0900000000")
                    .status(User.Status.Active)
                    .role(adminRole)
                    .build());
        }

        System.out.println("✅ Seeded: 3 roles, 3 locations, 3 vehicle types, 4 vehicles, 1 admin");
        System.out.println("👤 Admin login: username=admin / password=admin123");
    }
}
