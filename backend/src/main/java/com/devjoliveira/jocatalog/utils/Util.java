package com.devjoliveira.jocatalog.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.devjoliveira.jocatalog.projections.IdProjection;

public class Util {

  /**
   * This is a GENERIC method
   * That needs to work with any tipe that implemtns IdProjection
   * Can be Long or any other tipe that you want. like UUID, String, Integer, etc.
   * So, we use a generic type <ID> to represent the type of the ID.
   * 
   * The objective this method is to replace the unordered list and return a
   * ordered list
   * 
   */
  public static <ID> List<? extends IdProjection<ID>> replace(List<? extends IdProjection<ID>> ordered,
      List<? extends IdProjection<ID>> unordered) {

    Map<ID, IdProjection<ID>> map = new HashMap<>();
    for (IdProjection<ID> obj : unordered) {
      map.put(obj.getId(), obj);
    }

    List<IdProjection<ID>> result = new ArrayList<>();
    for (IdProjection<ID> obj : ordered) {
      result.add(map.get(obj.getId()));
    }
    return result;
  }

}
