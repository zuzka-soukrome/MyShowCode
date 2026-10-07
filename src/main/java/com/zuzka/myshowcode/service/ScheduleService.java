package com.zuzka.myshowcode.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ScheduleService {

    private OrderService orderService;

    public ScheduleService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Scheduled(fixedRateString = "${scheduler.rate.ms}")
    public void scheduleFixedRateTask() {
        log.debug("Scheduled task triggered.");
        orderService.checkForExpiredOrders();
    }
}
