package me.decentos.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.decentos.service.DiscountServiceAsync;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(value = "/discountservice/async/discount")
@Slf4j
@RequiredArgsConstructor
public class DiscountControllerAsync {

    private final DiscountServiceAsync discountServiceAsync;

    @GetMapping("/product")
    @ApiOperation(value = "Asynchronous method for getting a discount on an item")
    public ResponseEntity<CompletableFuture<String>> getDiscountForItem(
            @ApiParam(name = "itemId", value = "Unique product ID", required = true) @RequestParam long itemId,
            @ApiParam(name = "basePrice", value = "Current base price", required = true) @RequestParam double basePrice,
            @ApiParam(name = "userId", value = "Unique user ID") @RequestParam(required = false, defaultValue = "0") long userId
    ) {
        try {
            return ResponseEntity.ok(discountServiceAsync.getDiscountForItem(itemId, basePrice, userId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
