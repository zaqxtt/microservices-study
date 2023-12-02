package com.zaq.inventoryservice.controller;

import com.zaq.inventoryservice.dto.InventoryResponse;
import com.zaq.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    public List<InventoryResponse> isInStock(@RequestParam List<String> skuCodeList){
        return inventoryService.isInStock(skuCodeList);
    }
}
