import 'dart:math';

import 'package:collection/collection.dart';
import 'package:flutter/cupertino.dart';
import 'package:kiddoz_in_flutter/utils/TypeEnum.dart';

import 'Recommendation.dart';

class Repo extends ChangeNotifier{
  int id=1;
  List<Recommendation> recommendationList = [];
  
  Repo(){
    addToList("Tennis", "d1", "b1",6, TypeEnum.sport);
    addToList("Dog man", "d2", "b1",2, TypeEnum.book);
    addToList("Lilo and Stitch", "d3", "b1",1, TypeEnum.other);
    addToList("Zucchini fritters", "d4", "b1",13, TypeEnum.foodRecipe);
    addToList("The Magic Faraway Tree", "d5", "b1",2, TypeEnum.book);
    addToList("Gymnastics", "d6", "b1",1, TypeEnum.sport);
    addToList("The magician", "d5", "b1",2, TypeEnum.book);
    addToList("Tomato salad", "d6", "b1",1, TypeEnum.foodRecipe);
    addToList("Circus", "d5", "b1",2, TypeEnum.other);
    addToList("Voley", "d6", "b1",1, TypeEnum.sport);

  }

  List<Recommendation> getRecommendationList(){
    return recommendationList;
  }
  int getListSize(){
    return recommendationList.length;
  }

  void addToList(String title, String description, String benefits, int age, TypeEnum type) {
    int id = this.id;
    recommendationList.add(Recommendation(id, title, description, benefits, age, type));
    this.id++;
    notifyListeners();
  }

  void removeFromList(int id) {
    recommendationList.removeWhere((element) => element.id == id);
    notifyListeners();

  }

  void updateItem(Recommendation recommendation) {
    Recommendation? oldRec = recommendationList
        .firstWhereOrNull((element) => element.id == recommendation.id);
    if (oldRec != null) {
      oldRec.title=recommendation.title;
      oldRec.age = recommendation.age;
      oldRec.benefits = recommendation.benefits;
      oldRec.description = recommendation.description;
      oldRec.type = recommendation.type;
    }
    notifyListeners();

  }

}
