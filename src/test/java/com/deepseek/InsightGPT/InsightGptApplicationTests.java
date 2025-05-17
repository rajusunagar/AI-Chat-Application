package com.deepseek.InsightGPT;

import com.deepseek.InsightGPT.Controller.ChatController;
import com.deepseek.InsightGPT.Controller.WebController;
import com.deepseek.InsightGPT.service.DeepseekService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class InsightGptApplicationTests {

        @Autowired
        private ChatController chatController;
        
        @Autowired
        private WebController webController;
        
        @MockBean
        private RestTemplate restTemplate;

        @Test
        void contextLoads() {
                // Verify that the application context loads successfully
                assertNotNull(chatController);
                assertNotNull(webController);
        }

}
