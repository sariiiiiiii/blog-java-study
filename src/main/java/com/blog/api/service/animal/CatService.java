package com.blog.api.service.animal;

import com.blog.api.domain.AnimalType;
import org.springframework.stereotype.Component;

@Component
public class CatService implements AnimalService {

    @Override
    public String getSound() {
        return "야옹";
    }

    @Override
    public AnimalType getType() {
        return AnimalType.CAT;
    }

}
