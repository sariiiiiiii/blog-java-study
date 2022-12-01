package com.blog.api.service.animal;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AnimalServiceFinder {

    private final List<AnimalService> animalServices;

    public AnimalService find(String type) {
        return animalServices.stream()// animalService의 각각 구현되어있는 구현체를 stream()을 돌려서
                .filter(animalService -> animalService.getType().name().equalsIgnoreCase(type)) // 구현되어있던 getType을 가지고 넘어온 parameter와 비교해서
//                .filter(animalService -> animalService.getType() == AnimalType.valueOf(type))
                .findAny() // 찾은 다음에 값을 반환
                .orElseThrow(RuntimeException::new); // 없으면 오류
    }

}
