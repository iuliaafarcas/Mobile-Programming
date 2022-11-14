import 'package:kiddoz_in_flutter/utils/TypeEnum.dart';

class Recommendation {
  int id = 0;
  String title = "";
  String description = "";
  String benefits = "";
  int age = 0;
  TypeEnum type = TypeEnum.other;

  Recommendation(this.id, this.title, this.description, this.benefits, this.age, this.type);

  String toStr() {
    return "$id $title $description $benefits $age ${type.name}";
  }
}
