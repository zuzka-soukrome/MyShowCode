package com.zuzka.myshowcode;

import com.zuzka.myshowcode.controller.OrderController;
import com.zuzka.myshowcode.controller.ProductController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MyShowCodeApplicationTests {

    @Autowired
    ProductController productController;

    @Autowired
    OrderController orderController;

    @Test
    void contextLoads() {
        Assertions.assertThat(productController).isNotNull();
        Assertions.assertThat(orderController).isNotNull();
    }

}
