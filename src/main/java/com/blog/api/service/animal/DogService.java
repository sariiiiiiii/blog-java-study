package com.blog.api.service.animal;

import com.blog.api.domain.AnimalType;
import org.springframework.stereotype.Component;

@Component
public class DogService implements AnimalService {

    @Override
    public String getSound() {
        return "멍멍";
    }

    @Override
    public AnimalType getType() {
        return AnimalType.DOG;
    }

}
