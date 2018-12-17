package com.food.fctfood.repository;
 
import com.food.fctfood.model.TbUserInvication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 实现Jap仓库接口类 
 */
public interface UserInvicationRepository extends JpaRepository<TbUserInvication, Integer> {
    List<TbUserInvication> findAllByUserId(int userId);
    Page<TbUserInvication> findAll(Pageable pageable);
}
