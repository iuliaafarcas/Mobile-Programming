package com.example.server;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class RecommendationController {
    private final RecommendationService service;

    @Autowired
    public RecommendationController(RecommendationService service) {
        this.service = service;
    }

    @GetMapping("/read")
    public List<Recommendation> readEntities(){
        return service.getAllRecommendations();
    }

    @MessageMapping("/create")
    @SendTo("/entities/create")
    public Recommendation createRecommendation(Recommendation recommendation){
        return service.saveRecommendation(recommendation);
    }

    @MessageMapping("/update")
    @SendTo("/entities/update")
    public Recommendation updateRecommendation(Recommendation recommendation){
        return service.updateRecommendation(recommendation);
    }

    @MessageMapping("/delete")
    @SendTo("/entities/delete")
    public Integer deleteRecommendation(Integer id){
        service.deleteRecommendation(id);
        return id;
    }



}
