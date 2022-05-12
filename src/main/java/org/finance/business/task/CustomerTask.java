package org.finance.business.task;

import lombok.extern.slf4j.Slf4j;
import org.finance.business.entity.Customer;
import org.finance.business.service.CustomerService;
import org.finance.infrastructure.exception.HxException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author jiangbangfa
 */
@Slf4j
@Component
public class CustomerTask {

    private final static ArrayBlockingQueue<Customer> initialCustomerQueue = new ArrayBlockingQueue<>(100);
    private final AtomicInteger counter = new AtomicInteger(1);
    private final ExecutorService threadPoolExecutor = Executors.newSingleThreadExecutor(r -> {
        Thread thread = new Thread(r);
        thread.setName("GenerateCustomerRelatedTableTask-" + counter.getAndIncrement());
        return thread;
    });
    @Resource
    private CustomerService customerService;

    @PostConstruct
    public void init() {
       /* customerService.loadInitializationCustomer();
        threadPoolExecutor.execute(() -> {
            while (true) {
                Customer customer = null;
                try {
                    customer = initialCustomerQueue.take();
                    customerService.initializationRelatedTable(customer.getTableIdentified());
                    customerService.initializationData(customer.getId());
                } catch (Exception e) {
                    log.error("初始化客户数据失败", e);
                    if (customer != null) {
                        log.error("初始化客户数据失败，客户ID：{}", customer.getId());
                    }
                }
            }
        });*/
    }

    public static void addInitialCustomer(Customer customer) {
        try {
            initialCustomerQueue.add(customer);
        } catch (Exception e) {
            throw new HxException("当前初始化客户数据的服务器资源已满，请稍后重试。剩余等待：" + initialCustomerQueue.size());
        }
    }

}
