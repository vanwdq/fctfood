package com.food.fctfood.repository;

import com.food.fctfood.model.TbUserBackend;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 实现Jap仓库接口类
 */
public interface UserBackendRepository extends JpaRepository<TbUserBackend, Integer> {

    TbUserBackend findByAccountAndPassword(String account, String password);
}
