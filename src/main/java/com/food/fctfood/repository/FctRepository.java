package com.food.fctfood.repository;
 
import com.food.fctfood.model.TbFct;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 实现Jap仓库接口类 
 */
public interface FctRepository extends JpaRepository<TbFct, Integer> {

}
