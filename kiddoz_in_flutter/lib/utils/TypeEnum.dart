enum TypeEnum { sport, book, foodRecipe, other }

class EnumHelper {
  static String getValue(TypeEnum type) {
    switch (type) {
      case TypeEnum.other:
        return "Other";
      case TypeEnum.sport:
        return "Sport";
      case TypeEnum.foodRecipe:
        return "Food Recipe";
      case TypeEnum.book:
        return "Book";
      default:
        return "";
    }
  }
}
