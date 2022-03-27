package com.bot.controller;

import com.bot.config.Urls;
import com.bot.service.IndexService;
import lombok.Data;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

@RestController
@Data
public class IndexController {

    @NonNull
    private final Urls urls;

    @Autowired
    private IndexService indexService;

    @GetMapping("/urls")
    public Urls getAllUrls() {
        return urls;
    }

    @GetMapping("/endpoints")
    public Map<String, List<String>> getAllEndpoints() {
        return indexService.fillEndpoints();
    }

    @GetMapping("/scan-all")
    public String scanAll() throws InvocationTargetException, IllegalAccessException {
        return indexService.executeAll();
    }
}
