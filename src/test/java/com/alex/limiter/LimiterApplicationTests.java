package com.alex.limiter;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.alex.limiter.config.LimiterProperties;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(properties = "spring.profiles.active=test", webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class LimiterApplicationTests {

    private int differentIp = 10;

    private int requestCountPerIp = 500;

    private int allRequestCount = differentIp * requestCountPerIp;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LimiterProperties limiterProperties;

    private ExecutorService executor = Executors.newFixedThreadPool(10);

    /**
     * написать простой JUnit-тест, который будет эмулировать работу параллельных запросов с разных IP.
     */
    @Test
    void multithreadingTest() throws Exception {

        List<Callable<SimpleEntry<String, Integer>>> tasks = new ArrayList<>();
        for (int i = 0; i < differentIp; i++) {
            for (int j = 0; j < requestCountPerIp; j++) {
                int finalI = i;
                tasks.add((Callable) () -> requestPerform("192.168.0." + finalI));
            }
        }
        Map<String, List<Integer>> res = getResult(tasks);
        checkResult(res);
    }

    private void checkResult(Map<String, List<Integer>> res) {

        assertEquals(differentIp, res.size());

        for (String key : res.keySet()) {

            List<Integer> oneIpList = res.get(key);

            assertEquals(requestCountPerIp, oneIpList.size());

            Map<Integer, Long> result = oneIpList.stream()
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            assertEquals((long) limiterProperties.getMaxCallQuantity(), result.get(200));
            assertEquals(requestCountPerIp - limiterProperties.getMaxCallQuantity(), result.get(502));
        }
    }

    private Map<String, List<Integer>> getResult(List<Callable<SimpleEntry<String, Integer>>> tasks) throws Exception {
        Map<String, List<Integer>> result = new HashMap<>();
        List<Future<SimpleEntry<String, Integer>>> futures = executor.invokeAll(tasks);
        for (Future<SimpleEntry<String, Integer>> future : futures) {
            var key = future.get().getKey();
            var value = future.get().getValue();

            if (result.containsKey(key)) {
                result.get(key).add(value);
            } else {
                result.put(key, new ArrayList<>(Collections.singletonList(value)));
            }
        }
        return result;
    }

    private SimpleEntry<String, Integer> requestPerform(String ip) throws Exception {
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/")
                        .with(request -> {
                            request.
                                    setRemoteAddr(ip);
                            return request;
                        })
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        return new SimpleEntry<>(ip, result.getResponse().getStatus());
    }

}
