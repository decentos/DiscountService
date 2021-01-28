package me.decentos.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.decentos.entity.enums.Action;
import me.decentos.service.DiscountConfigureService;
import me.decentos.service.DiscountPeriodConfigureService;
import me.decentos.service.DiscountService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping(value = "/discountservice/discount")
@Slf4j
@RequiredArgsConstructor
public class DiscountController {

    private final DiscountService discountService;
    private final DiscountConfigureService discountConfigureService;
    private final DiscountPeriodConfigureService discountPeriodConfigureService;

    @GetMapping("/product")
    @ApiOperation(value = "Method of getting a discount on an item")
    public ResponseEntity<String> getDiscountForItem(
            @ApiParam(name = "itemId", value = "Unique product ID", required = true) @RequestParam long itemId,
            @ApiParam(name = "basePrice", value = "Current base price", required = true) @RequestParam double basePrice,
            @ApiParam(name = "userId", value = "Unique user ID") @RequestParam(required = false, defaultValue = "0") long userId
    ) {
        try {
            return ResponseEntity.ok(discountService.getDiscountForItem(itemId, basePrice, userId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/personal")
    @ApiOperation(value = "Method of obtaining a personal discount of the user")
    public ResponseEntity<String> getPersonalDiscount(
            @ApiParam(name = "userId", value = "Unique user ID", required = true) @RequestParam long userId
    ) {
        try {
            return ResponseEntity.ok(discountService.getPersonalDiscount(userId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/configure")
    @ApiOperation(value = "Method for configuring the discount percentage")
    public ResponseEntity<String> discountConfigure(
            @ApiParam(name = "sum", value = "Sum", required = true) @RequestParam double sum,
            @ApiParam(name = "action", value = "Action", required = true) @RequestParam Action action,
            @ApiParam(name = "percent", value = "Percent") @RequestParam(required = false, defaultValue = "0") int percent
    ) {
        try {
            return ResponseEntity.ok(discountConfigureService.discountConfigure(sum, action, percent));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/personal/change")
    @ApiOperation(value = "Method for changing the user's personal discount")
    public ResponseEntity<String> changePersonalDiscount(
            @ApiParam(name = "userId", value = "Unique user ID", required = true) @RequestParam long userId,
            @ApiParam(name = "purchaseAmount", value = "The amount of the purchase", required = true) @RequestParam int purchaseAmount
    ) {
        try {
            return ResponseEntity.ok(discountService.changePersonalDiscount(userId, purchaseAmount));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/create/date")
    @ApiOperation(value = "Method for configuring sales days")
    public ResponseEntity<String> discountPeriodConfigure(
            @ApiParam(name = "startDate", value = "Start date of the sale", required = true) @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @ApiParam(name = "endDate", value = "End date of the sale", required = true) @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @ApiParam(name = "action", value = "Action", required = true) @RequestParam Action action,
            @ApiParam(name = "percent", value = "Percentage discount", required = true) @RequestParam int percent
    ) {
        try {
            return ResponseEntity.ok(discountPeriodConfigureService.discountPeriodConfigure(startDate, endDate, action, percent));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
