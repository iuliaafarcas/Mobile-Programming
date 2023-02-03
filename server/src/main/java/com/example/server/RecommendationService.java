package com.example.server;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RecommendationService {
     private final IRepository repository;

     @Autowired
    public RecommendationService(IRepository repository) {
        this.repository = repository;
    }

    public Recommendation saveRecommendation(Recommendation recommendation){
         log.debug("Added recommendation!{}",recommendation.toString());
         return repository.save(recommendation);
    }

    public List<Recommendation> getAllRecommendations(){
         log.debug("Reading all recommendations...");
         return repository.findAll();
    }

    @Transactional
    public Recommendation updateRecommendation(Recommendation recommendation){
         log.debug("Updating the recommendation{}", recommendation.toString());
         Recommendation oldRecommendation=repository.getReferenceById(recommendation.getId());
         oldRecommendation.setRecommendedAge(recommendation.getRecommendedAge());
         oldRecommendation.setBenefits(recommendation.getBenefits());
         oldRecommendation.setDescription(recommendation.getDescription());
         oldRecommendation.setTitle(recommendation.getTitle());
         oldRecommendation.setType(recommendation.getType());
         return repository.save(oldRecommendation);

    }

    public void deleteRecommendation(Integer id){
         log.debug("Deleting transaction{}", id);
         repository.deleteById(id);
    }
}
