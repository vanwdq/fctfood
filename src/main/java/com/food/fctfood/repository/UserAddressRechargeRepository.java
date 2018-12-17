package com.food.fctfood.repository;

import com.food.fctfood.model.TbUserAddressRecharge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 实现Jap仓库接口类
 */
public interface UserAddressRechargeRepository extends JpaRepository<TbUserAddressRecharge, Integer> {
    List<TbUserAddressRecharge> findAllByType(int type);

    TbUserAddressRecharge findByAddress(String address);

    TbUserAddressRecharge findByTypeAndUserId(int type, int userId);

    TbUserAddressRecharge findByTypeAndAddress(int type, String address);
}
