package com.food.fctfood.repository;

import com.food.fctfood.model.TbUserAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 实现Jap仓库接口类
 */
public interface UserAddressRepository extends JpaRepository<TbUserAddress, Integer> {
    List<TbUserAddress> findAllByType(int type);

    TbUserAddress findByAddress(String address);

    TbUserAddress findByTypeAndUserId(int type,int userId);
}
