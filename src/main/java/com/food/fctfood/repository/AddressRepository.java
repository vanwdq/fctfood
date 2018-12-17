package com.food.fctfood.repository;

import com.food.fctfood.model.TbAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 实现Jap仓库接口类
 */
public interface AddressRepository extends JpaRepository<TbAddress, Integer> {

    List<TbAddress> findAllByTypeAndIsAllot(int type, int isAllot);

}
