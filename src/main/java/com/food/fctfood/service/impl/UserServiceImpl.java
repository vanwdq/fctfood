package com.food.fctfood.service.impl;

import com.alibaba.fastjson.JSON;
import com.food.fctfood.annotation.RateLimiter;
import com.food.fctfood.common.CacheKey;
import com.food.fctfood.common.MessageCode;
import com.food.fctfood.model.*;
import com.food.fctfood.repository.*;
import com.food.fctfood.request.UserBackRq;
import com.food.fctfood.request.UserInvicationRq;
import com.food.fctfood.request.UserRq;
import com.food.fctfood.response.*;
import com.food.fctfood.service.EmailService;
import com.food.fctfood.service.UserService;
import com.food.fctfood.util.*;
import com.sargeraswang.util.ExcelUtil.ExcelLogs;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private EmailService emailService;
    @Autowired
    private JRedisUtil jRedisUtil;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserBackendRepository userBackendRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private UserAddressRepository userAddressRepository;
    @Autowired
    private UserInvicationRepository userInvicationRepository;
    @Autowired
    private FctRepository fctRepository;

    @Override
    public Response<String> sendEmail(String email) {
        Response<String> response = ResponseUtil.ok();
        response.setMessage("Successful mail delivery");
        TbUser tbUser = userRepository.findByEmail(email);
        if (tbUser != null) {
            response.setCode(MessageCode.USER_REGISTER);
            response.setMessage("User Registered");
            return response;
        }

        if (jRedisUtil.exists(email)) {
            response.setCode(MessageCode.MORE_SEND);
            response.setMessage("Send too fast, please send 60 seconds later");
            return response;
        }
        String code = RandomCode.generate();
        emailService.sendSimpleMail(email, "Registration code", code);
        jRedisUtil.set(email, code, 180);
        return response;
    }

    @Override
    public Response<String> forgetPwd(String email) {
        Response<String> response = ResponseUtil.ok();
        response.setMessage("Successful mail delivery");
        TbUser tbUser = userRepository.findByEmail(email);
        if (tbUser == null) {
            response.setCode(MessageCode.WRONG_USER);
            response.setMessage("ERROR Incorrect username or password");
            return response;
        }

        if (jRedisUtil.exists(CacheKey.USER_EMAIL + email)) {
            response.setCode(MessageCode.MORE_SEND);
            response.setMessage("Send too fast, please send 60 seconds later");
            return response;
        }
        String code = RandomCode.generate();
        emailService.sendSimpleMail(CacheKey.USER_EMAIL + email, "forget password code", code);
        jRedisUtil.set(CacheKey.USER_EMAIL + email, code, 180);
        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response<String> register(UserRq userRq) {
        Response<String> response = ResponseUtil.ok();
        String codeCache = jRedisUtil.get(userRq.getEmail());
        if (StringUtils.isEmpty(codeCache)) {
            response.setCode(MessageCode.INVALID_CODE);
            response.setMessage("Verification code expiration");
            return response;
        }
        if (!codeCache.equals(userRq.getCode())) {
            response.setCode(MessageCode.WRONG_CODE);
            response.setMessage("Error in Verification Code Input");
            return response;
        }
        TbUser tbUser = userRepository.findByEmail(userRq.getEmail());
        if (tbUser != null) {
            response.setCode(MessageCode.USER_REGISTER);
            response.setMessage("User Registered");
            return response;
        }
        //用户入库
        tbUser = new TbUser();
        tbUser.setCreateTime(new Date());
        tbUser.setEmail(userRq.getEmail());
        tbUser.setPassword(MD5Tools.string2MD5(userRq.getPassword()));
        tbUser = userRepository.save(tbUser);
        int userId = tbUser.getId();
        //从地址表里取出btc和eth各一个分配给用户
        List<TbAddress> btcAddress = addressRepository.findAllByTypeAndIsAllot(0, 0);
        List<TbAddress> ethAddress = addressRepository.findAllByTypeAndIsAllot(1, 0);
        if (CollectionUtils.isEmpty(btcAddress)) {
            response.setCode(MessageCode.NO_ADDRESS);
            response.setMessage("The btc address has reached the upper limit");
            return response;
        }

        if (CollectionUtils.isEmpty(ethAddress)) {
            response.setCode(MessageCode.NO_ADDRESS);
            response.setMessage("The eth address has reached the upper limit");
            return response;
        }
        TbAddress btcAddr = btcAddress.get(0);
        TbAddress ethAddr = ethAddress.get(0);
        btcAddr.setIsAllot(1);
        ethAddr.setIsAllot(1);
        addressRepository.saveAndFlush(btcAddr);
        addressRepository.saveAndFlush(ethAddr);

        String btcAdr = btcAddr.getAddress();
        String ethAdr = ethAddr.getAddress();
        //分配地址
        TbUserAddress tbUserAddress1 = new TbUserAddress();
        tbUserAddress1.setAddress(btcAdr);
        tbUserAddress1.setType(0);
        tbUserAddress1.setUserId(userId);
        tbUserAddress1.setCreateTime(new Date());
        tbUserAddress1.setAmount(BigDecimal.ZERO);
        TbUserAddress tbUserAddress2 = new TbUserAddress();
        tbUserAddress2.setAddress(ethAdr);
        tbUserAddress2.setType(1);
        tbUserAddress2.setUserId(userId);
        tbUserAddress2.setCreateTime(new Date());
        tbUserAddress2.setAmount(BigDecimal.ZERO);
        userAddressRepository.saveAndFlush(tbUserAddress1);
        userAddressRepository.saveAndFlush(tbUserAddress2);
        response.setMessage("register was successful");
        return response;
    }

    @Override
    public Response<UserResponseVo> login(UserRq userRq) {
        Response<UserResponseVo> response = ResponseUtil.ok();
        String email = userRq.getEmail();
        String pwd = userRq.getPassword();
        pwd = MD5Tools.string2MD5(pwd);
        TbUser tbUser = userRepository.findByEmailAndPassword(email, pwd);
        if (tbUser == null) {
            response.setCode(MessageCode.WRONG_USER);
            response.setMessage("ERROR Incorrect username or password");
            return response;
        }

        //查询个人信息
        List<UserInfoVo> userInfoVos = userRepository.queryUserInfo(tbUser.getId());
        List<UserDetailVo> userDetailVos = new ArrayList<>();
        for (UserInfoVo userInfoVo : userInfoVos) {
            UserDetailVo userDetailVo = new UserDetailVo();
            userDetailVo.setAddress(userInfoVo.getTbUserAddress().getAddress());
            userDetailVo.setAmount(userInfoVo.getTbUserAddress().getAmount());
            userDetailVo.setType(userInfoVo.getTbUserAddress().getType());
            userDetailVos.add(userDetailVo);
        }
        //记录用户信息
        String token = UUID.randomUUID().toString();//token
        jRedisUtil.set(CacheKey.USER_SESSION_KEY + token, JSON.toJSONString(tbUser), CacheKey.USER_LOGIN_EXPIRE);
        UserResponseVo userResponseVo = new UserResponseVo();
        userResponseVo.setToken(token);
        userResponseVo.setUserDetailVos(userDetailVos);
        response.setData(userResponseVo);
        return response;
    }

    @Override
    public Response<String> loginBack(UserBackRq userBackRq) {
        Response<String> response = ResponseUtil.ok();
        TbUserBackend tbUserBackend = userBackendRepository.findByAccountAndPassword(userBackRq.getAccount(), userBackRq.getPassword());
        if (tbUserBackend == null) {
            response.setCode(MessageCode.WRONG_USER);
            response.setMessage("用户名或密码错误");
        }
        return response;
    }

    @Override
    public Response<String> logout(String token) {
        Response<String> response = ResponseUtil.ok();
        String json = jRedisUtil.get(CacheKey.USER_SESSION_KEY + token);
        if (StringUtils.isEmpty(json)) {
            response.setCode(MessageCode.NO_TOKEN);
            response.setMessage("no token");
            return response;
        }
        TbUser tbUser = JSON.parseObject(json, TbUser.class);
        if (tbUser == null) {
            response.setCode(MessageCode.TOKEN_EXPIRE);
            response.setMessage("token  exprie");
            return response;
        }
        jRedisUtil.del(CacheKey.USER_SESSION_KEY + token);
        return response;
    }

    @Override
    public Response<UserResponseVo> getUserInfo(String token) {
        Response<UserResponseVo> response = ResponseUtil.ok();
        String json = jRedisUtil.get(CacheKey.USER_SESSION_KEY + token);
        if (StringUtils.isEmpty(json)) {
            response.setCode(MessageCode.NO_TOKEN);
            response.setMessage("no token");
            return response;
        }
        TbUser tbUser = JSON.parseObject(json, TbUser.class);
        if (tbUser == null) {
            response.setCode(MessageCode.WRONG_USER);
            response.setMessage("token exprie");
            return response;
        }
        //查询个人信息
        List<UserInfoVo> userInfoVos = userRepository.queryUserInfo(tbUser.getId());
        List<UserDetailVo> userDetailVos = new ArrayList<>();
        for (UserInfoVo userInfoVo : userInfoVos) {
            UserDetailVo userDetailVo = new UserDetailVo();
            userDetailVo.setAddress(userInfoVo.getTbUserAddress().getAddress());
            userDetailVo.setAmount(userInfoVo.getTbUserAddress().getAmount());
            userDetailVo.setType(userInfoVo.getTbUserAddress().getType());
            userDetailVos.add(userDetailVo);
        }
        UserResponseVo userResponseVo = new UserResponseVo();
        userResponseVo.setToken(token);
        userResponseVo.setUserDetailVos(userDetailVos);
        response.setData(userResponseVo);
        return response;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized Response<String> vote(String token, UserInvicationRq userInvicationRq) {
        Response<String> response = ResponseUtil.ok();
        String json = jRedisUtil.get(CacheKey.USER_SESSION_KEY + token);
        if (StringUtils.isEmpty(json)) {
            response.setCode(MessageCode.NO_TOKEN);
            response.setMessage("Please Login");
            return response;
        }
        TbUser tbUser = JSON.parseObject(json, TbUser.class);
        if (tbUser == null) {
            response.setCode(MessageCode.WRONG_USER);
            response.setMessage("token exprie");
            return response;
        }
        //查询用户地址
        TbUserAddress tbUserAddress = userAddressRepository.findByTypeAndUserId(userInvicationRq.getType(), tbUser.getId());
        if (tbUserAddress == null) {
            response.setCode(MessageCode.NO_ADDRESS);
            response.setMessage("Address not unallocated");
            return response;
        }
        if (tbUserAddress.getAmount().compareTo(userInvicationRq.getAmount()) <= 0) {
            response.setCode(MessageCode.NO_MONEY);
            response.setMessage("Not enough amount");
            return response;
        }

        BigDecimal remainMoney = tbUserAddress.getAmount().subtract(userInvicationRq.getAmount()).setScale(BigDecimal.ROUND_HALF_UP);
        tbUserAddress.setAmount(remainMoney);
        userAddressRepository.saveAndFlush(tbUserAddress);
        TbUserInvication tbUserInvication = new TbUserInvication();
        tbUserInvication.setAddress(tbUserAddress.getAddress());
        tbUserInvication.setType(userInvicationRq.getType());
        tbUserInvication.setAmount(userInvicationRq.getAmount());
        tbUserInvication.setUserId(tbUser.getId());
        tbUserInvication.setCreateTime(new Date());
        tbUserInvication.setFctBd(ConfigUtil.countBd(userInvicationRq.getType(), userInvicationRq.getAmount()));
        tbUserInvication = userInvicationRepository.save(tbUserInvication);
        BigDecimal fct = tbUserInvication.getFctBd();
        //fct总的投资额减去参与投资的
        TbFct tbFct = fctRepository.findAll().get(0);
        if (tbFct.getRemainTotal().compareTo(BigDecimal.ZERO) <= 0) {
            response.setCode(MessageCode.NO_VOTE);
            response.setMessage("With no investment quota");
            return response;
        }
        BigDecimal voteTotal = tbFct.getVoteTotal().add(fct).setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal remainTotal = tbFct.getFctTotal().subtract(voteTotal).setScale(2, BigDecimal.ROUND_HALF_UP);
        //计算参投比例
        BigDecimal rate = voteTotal.divide(tbFct.getFctTotal(), 8, BigDecimal.ROUND_HALF_UP);
        tbFct.setUpdateTime(new Date());
        tbFct.setRemainTotal(remainTotal);
        tbFct.setRate(rate);
        tbFct.setVoteTotal(voteTotal);
        fctRepository.save(tbFct);
        response.setMessage("Investment success");
        return response;
    }

    @Override
    public Response<List<TbUserInvication>> queryVoteDetail(String token) {
        Response<List<TbUserInvication>> response = ResponseUtil.ok();
        String json = jRedisUtil.get(CacheKey.USER_SESSION_KEY + token);
        if (StringUtils.isEmpty(json)) {
            response.setCode(MessageCode.NO_TOKEN);
            response.setMessage("no token");
            return response;
        }
        TbUser tbUser = JSON.parseObject(json, TbUser.class);
        if (tbUser == null) {
            response.setCode(MessageCode.WRONG_USER);
            response.setMessage("token exprie");
            return response;
        }

        List<TbUserInvication> tbUserInvications = userInvicationRepository.findAllByUserId(tbUser.getId());
        response.setData(tbUserInvications);
        response.setMessage("Investment details");
        return response;
    }

    @Override
    public Response<TbFct> voteload() {
        Response<TbFct> response = ResponseUtil.ok();
        TbFct tbFct = fctRepository.findAll().get(0);
        response.setData(tbFct);
        response.setMessage("Investment progress");
        return response;
    }

    @Override
    public Response<String> importAddress(MultipartFile file) {
        Response<String> response = ResponseUtil.ok();
        if (!file.isEmpty()) {
            try {
                FileUtils.writeByteArrayToFile(new File("/usr/local/file/" + file.getOriginalFilename()), file.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        File f = new File("/usr/local/file/" + file.getOriginalFilename());
        InputStream inputStream = null;
        List<TbAddress> tbAddresses = new ArrayList<>();
        try {
            inputStream = new FileInputStream(f);
            ExcelLogs logs = new ExcelLogs();
            Collection<Map> importExcel = ExcelUtil.importExcel(Map.class, inputStream, "yyyy/MM/dd HH:mm:ss", logs, 0);
            for (Map map : importExcel) {
                TbAddress tbAddress = new TbAddress();
                String address = String.valueOf(map.get(file.getName()));
                tbAddress.setCreateTime(new Date());
                tbAddress.setIsAllot(0);
                if (file.getOriginalFilename().contains("eth")) {
                    tbAddress.setType(1);
                } else {
                    tbAddress.setType(0);
                }
                tbAddress.setAddress(address);
                tbAddresses.add(tbAddress);
            }
            addressRepository.saveAll(tbAddresses);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            response.setCode(MessageCode.EXCEPTION_ERROR);
        }
        return response;
    }

    @Override
    public Response<String> updatePassword(UserRq userRq) {
        Response<String> response = ResponseUtil.ok();
        String email = userRq.getEmail();
        TbUser tbUser = userRepository.findByEmail(email);
        if (tbUser == null) {
            response.setCode(MessageCode.WRONG_USER);
            response.setMessage("no user");
            return response;
        }

        String code = jRedisUtil.get(CacheKey.USER_EMAIL + userRq.getEmail());
        if (!code.equals(userRq.getCode())) {
            response.setCode(MessageCode.WAONR_CODE);
            response.setMessage("Not right code");
            return response;
        }

        String pwd = MD5Tools.string2MD5(userRq.getPassword());
        tbUser.setPassword(pwd);
        userRepository.save(tbUser);
        return response;
    }

    @Override
    public Response<UserInvication> queryAllVoteDetail(Integer pageIndex, Integer pageRow) {
        Response<UserInvication> response = ResponseUtil.ok();
        UserInvication userInvication = new UserInvication();
        pageIndex = pageIndex * pageRow;
        Pageable pageable = new QPageRequest(pageIndex, pageRow);
        Page<TbUserInvication> tbUserInvicationPage = userInvicationRepository.findAll(pageable);
        int totalPage = tbUserInvicationPage.getTotalPages();
        List<TbUserInvication> tbUserInvications = tbUserInvicationPage.getContent();
        userInvication.setTotalPage(totalPage);
        userInvication.setTbUserInvications(tbUserInvications);
        response.setData(userInvication);
        return response;
    }



}
