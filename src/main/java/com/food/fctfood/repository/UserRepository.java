package com.food.fctfood.repository;

import com.food.fctfood.model.TbUser;
import com.food.fctfood.response.UserInfoVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 实现Jap仓库接口类
 */
public interface UserRepository extends JpaRepository<TbUser, Integer> {
    TbUser findByEmailAndPassword(String email, String password);

    TbUser findByEmail(String email);

    @Query(value = "SELECT new com.food.fctfood.response.UserInfoVo(tb1, tb2) FROM TbUser tb1, TbUserAddress tb2 WHERE tb1.id = tb2.userId and tb1.id=:id")
    List<UserInfoVo> queryUserInfo(@Param("id") int id);

    TbUser findById(int id);
}
