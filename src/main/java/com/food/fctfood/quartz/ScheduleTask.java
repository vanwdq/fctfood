package com.food.fctfood.quartz;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.food.fctfood.model.TbUser;
import com.food.fctfood.model.TbUserAddress;
import com.food.fctfood.model.TbUserAddressRecharge;
import com.food.fctfood.repository.UserAddressRechargeRepository;
import com.food.fctfood.repository.UserAddressRepository;
import com.food.fctfood.repository.UserRepository;
import com.food.fctfood.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Component
public class ScheduleTask {


    private static Logger logger = LoggerFactory.getLogger(ScheduleTask.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserAddressRepository userAddressRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserAddressRechargeRepository userAddressRechargeRepository;


    public final static long ethMinute = 5 * 60 * 1000;

    public final static long btcMinute = 1 * 60 * 1000;

    @Autowired
    private EmailService emailService;


    /**
     * 检测是否充值到账
     *
     * @return
     */
  //  @Scheduled(fixedDelay = btcMinute)
   // @Transactional
    public void rechargeBtc() {
        List<TbUserAddress> tbUserAddresses = userAddressRepository.findAllByType(0);
        List<String> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(tbUserAddresses)) {
            for (TbUserAddress tbUserAddress : tbUserAddresses) {
                list.add(tbUserAddress.getAddress());
            }
            String address = listToString(list, ',');
            String url = "https://chain.api.btc.com/v3/address/%s?_ga=2.174704678.506932064.1544438864-200091720.1544438864";
            url = String.format(url, address);
            try {
                ResponseEntity responseEntity = restTemplate.getForEntity(url, String.class);
                if (responseEntity.getStatusCode().value() == 200) {
                    //则处理业务逻辑
                    String body = responseEntity.getBody().toString();
                    JSONObject jsonObject = JSON.parseObject(body);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray != null && jsonArray.size() > 0) {
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            if (jsonObject1 == null) {
                                continue;
                            }
                            String address1 = jsonObject1.getString("address");
                            BigDecimal balance = jsonObject1.getBigDecimal("balance");
                            balance = balance.divide(new BigDecimal("100000000")).setScale(6, BigDecimal.ROUND_HALF_UP);
                            TbUserAddressRecharge tbUserAddressRecharge = userAddressRechargeRepository.findByTypeAndAddress(0, address1);
                            if (tbUserAddressRecharge != null && tbUserAddressRecharge.getAmount().compareTo(balance) != 0) {
                                TbUserAddress tbUserAddress = userAddressRepository.findByAddress(address1);
                                TbUserAddressRecharge tbUserAddressRecharge1 = new TbUserAddressRecharge();
                                tbUserAddressRecharge1.setType(0);
                                tbUserAddressRecharge1.setUserId(tbUserAddressRecharge.getUserId());
                                tbUserAddressRecharge1.setCreateTime(new Date());
                                tbUserAddressRecharge1.setAmount(balance);
                                tbUserAddressRecharge1.setAddress(address1);
                                userAddressRechargeRepository.save(tbUserAddressRecharge1);
                                //给账户加上余额
                                BigDecimal amountNew = tbUserAddress.getAmount().add(balance).setScale(6, BigDecimal.ROUND_HALF_UP);
                                tbUserAddress.setAmount(amountNew);
                                userAddressRepository.save(tbUserAddress);
                                //发送邮件到账
                                TbUser tbUser = userRepository.findById(tbUserAddressRecharge.getUserId());
                                emailService.sendSimpleMail(tbUser.getEmail(), "您的btc:" + balance + "已到账", "充值到账");
                            }
                            if (tbUserAddressRecharge == null && balance.compareTo(BigDecimal.ZERO) > 0) {
                                TbUserAddress tbUserAddress = userAddressRepository.findByAddress(address1);
                                TbUserAddressRecharge tbUserAddressRecharge1 = new TbUserAddressRecharge();
                                tbUserAddressRecharge1.setType(0);
                                tbUserAddressRecharge1.setUserId(tbUserAddress.getUserId());
                                tbUserAddressRecharge1.setCreateTime(new Date());
                                tbUserAddressRecharge1.setAmount(balance);
                                tbUserAddressRecharge1.setAddress(address1);
                                userAddressRechargeRepository.save(tbUserAddressRecharge1);
                                TbUserAddress tbUserAddress1 = userAddressRepository.findByAddress(address1);
                                //给账户加上余额
                                BigDecimal amountNew = tbUserAddress.getAmount().add(balance).setScale(6, BigDecimal.ROUND_HALF_UP);
                                tbUserAddress.setAmount(amountNew);
                                userAddressRepository.save(tbUserAddress);
                                //发送邮件到账
                                TbUser tbUser = userRepository.findById(tbUserAddress1.getUserId());
                                emailService.sendSimpleMail(tbUser.getEmail(), "您的btc:" + balance + "已到账", "充值到账");
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("btc定时器错误:" + e.getMessage());
            }
        }

    }

  //  @Scheduled(fixedDelay = ethMinute)
   // @Transactional
    public void rechargeEth() {
        List<TbUserAddress> tbUserAddresses = userAddressRepository.findAllByType(1);
        List<String> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(tbUserAddresses)) {
            for (TbUserAddress tbUserAddress : tbUserAddresses) {
                list.add(tbUserAddress.getAddress());
            }
            String address = listToString(list, ',');
            String url = "https://api.etherscan.io/api?module=account&action=balancemulti&address=%s&tag=latest&apikey=YourApiKeyToken";
            url = String.format(url, address);
            try {
                ResponseEntity responseEntity = restTemplate.getForEntity(url, String.class);
                if (responseEntity.getStatusCode().value() == 200) {
                    //则处理业务逻辑
                    String body = responseEntity.getBody().toString();
                    JSONObject jsonObject = JSON.parseObject(body);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    if (jsonArray != null && jsonArray.size() > 0) {
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String address1 = jsonObject1.getString("account");
                            BigDecimal balance = jsonObject1.getBigDecimal("balance");
                            balance = balance.divide(new BigDecimal("1000000000000000000"));
                            TbUserAddressRecharge tbUserAddressRecharge = userAddressRechargeRepository.findByTypeAndAddress(1, address1);
                            if (tbUserAddressRecharge != null && tbUserAddressRecharge.getAmount().compareTo(balance) != 0) {
                                TbUserAddress tbUserAddress = userAddressRepository.findByAddress(address1);
                                TbUserAddressRecharge tbUserAddressRecharge1 = new TbUserAddressRecharge();
                                tbUserAddressRecharge1.setType(1);
                                tbUserAddressRecharge1.setUserId(tbUserAddressRecharge.getUserId());
                                tbUserAddressRecharge1.setCreateTime(new Date());
                                tbUserAddressRecharge1.setAmount(balance);
                                tbUserAddressRecharge1.setAddress(address1);
                                userAddressRechargeRepository.save(tbUserAddressRecharge1);
                                //给账户加上余额
                                BigDecimal amountNew = tbUserAddress.getAmount().add(balance).setScale(6, BigDecimal.ROUND_HALF_UP);
                                tbUserAddress.setAmount(amountNew);
                                userAddressRepository.save(tbUserAddress);
                                //发送邮件到账
                                TbUser tbUser = userRepository.findById(tbUserAddressRecharge.getUserId());
                                emailService.sendSimpleMail(tbUser.getEmail(), "您的eth:" + balance + "已到账", "充值到账");
                            }
                            if (tbUserAddressRecharge == null && balance.compareTo(BigDecimal.ZERO) > 0) {
                                TbUserAddress tbUserAddress = userAddressRepository.findByAddress(address1);
                                TbUserAddressRecharge tbUserAddressRecharge1 = new TbUserAddressRecharge();
                                tbUserAddressRecharge1.setType(1);
                                tbUserAddressRecharge1.setUserId(tbUserAddress.getUserId());
                                tbUserAddressRecharge1.setCreateTime(new Date());
                                tbUserAddressRecharge1.setAmount(balance);
                                tbUserAddressRecharge1.setAddress(address1);
                                userAddressRechargeRepository.save(tbUserAddressRecharge1);
                                TbUserAddress tbUserAddress1 = userAddressRepository.findByAddress(address1);
                                //给账户加上余额
                                BigDecimal amountNew = tbUserAddress.getAmount().add(balance).setScale(6, BigDecimal.ROUND_HALF_UP);
                                tbUserAddress.setAmount(amountNew);
                                userAddressRepository.save(tbUserAddress);
                                //发送邮件到账
                                TbUser tbUser = userRepository.findById(tbUserAddress1.getUserId());
                                emailService.sendSimpleMail(tbUser.getEmail(), "您的eth:" + balance + "已到账", "充值到账");
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("eth定时器错误:" + e.getMessage());
            }
        }
    }


    public String listToString(List list, char separator) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i)).append(separator);
        }
        return sb.toString().substring(0, sb.toString().length() - 1);
    }


}
