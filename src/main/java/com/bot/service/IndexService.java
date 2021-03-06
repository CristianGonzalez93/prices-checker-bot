package com.bot.service;

import com.bot.BotApplication;
import com.bot.config.Endpoints;
import com.bot.eshop.amazon.controller.AmazonController;
import com.bot.eshop.pccomponentes.controller.PcComponentesController;
import com.bot.event.AlertEvent;
import com.bot.event.AlertEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@Service
@Slf4j
public class IndexService {

    @Autowired
    private PcComponentesController pcComponentesController;

    @Autowired
    private AmazonController amazonController;

    @Autowired
    private AlertEventPublisher alertEventPublisher;

    public Map<String, List<String>> fillEndpoints() {
        if(Endpoints.endpoints == null) {
            RequestMappingHandlerMapping requestMappingHandlerMapping = BotApplication.applicationContext
                    .getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class);
            Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping
                    .getHandlerMethods();
            Endpoints.endpoints = new HashMap<>();
            map.forEach((key, value) -> {
                List<String> endpoints = Endpoints.endpoints.get(value.getBean().toString());
                if (endpoints == null) {
                    endpoints = new ArrayList<>();
                }
                if (key.getMethodsCondition().getMethods().contains(RequestMethod.GET)) {
                    endpoints.add(WebMvcLinkBuilder.linkTo(value.getMethod()).withRel(key.getDirectPaths().iterator().next()).getHref());
                    Endpoints.endpoints.put(value.getBean().toString(), endpoints);
                    log.info("{} {}", key, value);
                }
            });
        }

        return Endpoints.endpoints;
    }

    public boolean resetAll() {
        pcComponentesController.reset();
        amazonController.reset();
        return true;
    }

    @Scheduled(fixedDelay = 600000, initialDelay = 1000)
    public String executeAll() throws InvocationTargetException, IllegalAccessException {
        final List<String> notExecuteMethods = List.of("getSales", "reset");
        Method[] methods = Arrays.asList(pcComponentesController.getClass().getDeclaredMethods())
                .stream()
                .sorted(Comparator.comparing(Method::getName))
                .toArray(Method[]::new);
        for (Method method : methods) {
            if(!notExecuteMethods.contains(method.getName())) {
                method.invoke(pcComponentesController);
            }
        }

        for (Method method : amazonController.getClass().getDeclaredMethods()) {
            if(!notExecuteMethods.contains(method.getName())) {
                method.invoke(amazonController);
            }
        }

        alertEventPublisher.publishCustomEvent("");
        return "DONE";
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "Europe/Paris")
    public void resetEmailSends() {
        AlertEvent.areNewSales = true;
    }
}
