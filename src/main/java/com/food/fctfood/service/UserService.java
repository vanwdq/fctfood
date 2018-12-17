package com.food.fctfood.service;

import com.food.fctfood.model.TbFct;
import com.food.fctfood.model.TbUserInvication;
import com.food.fctfood.request.UserBackRq;
import com.food.fctfood.request.UserInvicationRq;
import com.food.fctfood.request.UserRq;
import com.food.fctfood.response.Response;
import com.food.fctfood.response.UserInvication;
import com.food.fctfood.response.UserResponseVo;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface UserService {

    Response<String> sendEmail(String email);

    Response<String> forgetPwd(String email);

    Response<String> register(UserRq userRq);

    Response<UserResponseVo> login(UserRq userRq);

    Response<String> loginBack(UserBackRq userBackRq);

    Response<String> logout(String token);

    Response<UserResponseVo> getUserInfo(String token);

    Response<String> vote(String token, UserInvicationRq userInvicationRq);

    Response<List<TbUserInvication>> queryVoteDetail(String token);

    Response<TbFct> voteload();

    Response<String> importAddress(MultipartFile file);

    Response<String> updatePassword(UserRq userRq);

    Response<UserInvication> queryAllVoteDetail(Integer pageIndex, Integer pageRow);
}
