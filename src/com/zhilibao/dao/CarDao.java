package com.zhilibao.dao;

import java.util.List;

import com.zhilibao.model.Cars;

public interface CarDao {
   public int save(Cars cars);
   public List<Cars> getlist();
}
