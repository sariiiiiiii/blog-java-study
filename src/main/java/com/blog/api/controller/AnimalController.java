package com.blog.api.controller;

import com.blog.api.domain.AnimalType;
import com.blog.api.service.animal.AnimalService;
import com.blog.api.service.animal.AnimalServiceFinder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AnimalController {

    private final List<AnimalService> animalServices;
    private final AnimalServiceFinder animalServiceFinder;
    private final Map<String, AnimalService> animalServiceList;

    @GetMapping("/animal")
    public String sound(@RequestParam String type) {
        return animalServiceFinder.find(type).getSound();
    }

    @GetMapping("/sound")
    public String sound_v2(@RequestParam String type) {
        AnimalService service = animalServiceList.get(type.toLowerCase() + "Service");
        return service.getSound();
    }

    @GetMapping("/enum")
    public String sound_v3(@RequestParam String type) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        AnimalType animalType = AnimalType.valueOf(type);
        AnimalService service = animalType.create();

        return service.getSound();
    }

}
