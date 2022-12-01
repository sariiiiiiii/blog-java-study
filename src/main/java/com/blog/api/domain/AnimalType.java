package com.blog.api.domain;

import com.blog.api.service.animal.AnimalService;
import com.blog.api.service.animal.CatService;
import com.blog.api.service.animal.DogService;

import java.lang.reflect.InvocationTargetException;

public enum AnimalType {

//    DOG,
//    CAT,

    CAT(CatService.class),
    DOG(DogService.class);

    private final Class<? extends AnimalService> animalService;

    // @RequiredArgConstructor
    AnimalType(Class<? extends AnimalService> animalService) {
        this.animalService = animalService;
    }

    public AnimalService create() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return animalService.getDeclaredConstructor().newInstance();
    }

}
