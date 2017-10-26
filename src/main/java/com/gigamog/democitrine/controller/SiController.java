package com.gigamog.democitrine.controller;

import com.gigamog.democitrine.domain.model.Si;
import com.gigamog.democitrine.service.SiService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SiController {

    private final SiService siService;

    public SiController(SiService siService) {
        this.siService = siService;
    }

    @RequestMapping("/units/si")
    public Si index(String units) {
        return siService.getSi(units);
    }
}
