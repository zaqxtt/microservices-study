package com.zaq.inventoryservice;

import com.zaq.inventoryservice.model.Inventory;
import com.zaq.inventoryservice.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaClient
public class InventoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner loadData(InventoryRepository inventoryRepository) {
        return args -> {
            Inventory inventory = new Inventory();
            inventory.setQuantity(100);
            inventory.setSkuCode("IPhone_10");

            Inventory inventory1 = new Inventory();
            inventory1.setQuantity(10);
            inventory1.setSkuCode("IPhone_10_red");

            inventoryRepository.save(inventory);
            inventoryRepository.save(inventory1);
        };
    }
}
