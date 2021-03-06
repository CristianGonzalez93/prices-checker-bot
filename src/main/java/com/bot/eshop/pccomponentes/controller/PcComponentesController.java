package com.bot.eshop.pccomponentes.controller;

import com.bot.eshop.pccomponentes.model.PcComponentesSaleProduct;
import com.bot.eshop.pccomponentes.service.PcComponentesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@RestController
public class PcComponentesController {

    @Autowired
    private PcComponentesService pcComponentesService;

    @GetMapping("/pccomponentes/motherboards")
    public String scanMotherboardsPrices() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        pcComponentesService.scanProducts("motherboard");
        return "DONE";
    }

    @GetMapping("/pccomponentes/processors")
    public String scanProcessorsPrices() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        pcComponentesService.scanProducts("processor");
        return "DONE";
    }

    @GetMapping("/pccomponentes/tower")
    public String scanTowerPrices() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        pcComponentesService.scanProducts("tower");
        return "DONE";
    }

    @GetMapping("/pccomponentes/graphic-card")
    public String scanGraphicCardPrices() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        pcComponentesService.scanProducts("graphic-card");
        return "DONE";
    }

    @GetMapping("/pccomponentes/focused-products")
    public String scanFocusedProducts() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        pcComponentesService.scanFocusedProducts();
        return "DONE";
    }

    @GetMapping("/pccomponentes/sales")
    public List<PcComponentesSaleProduct> getSales() {
        return pcComponentesService.getSales();
    }

    @GetMapping("/pccomponentes/reset")
    public boolean reset() {
        return pcComponentesService.reset();
    }
}
