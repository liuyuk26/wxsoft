package com.watts.sys.controller;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.watts.sys.model.User;
import com.watts.sys.repository.BaseJDBCRepository;
import com.watts.sys.repository.UserRepository;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/user")
public class UserController {
	protected static Logger logger = LoggerFactory.getLogger(UserController.class);
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BaseJDBCRepository baseJDBCRepository;

	@ResponseBody
	@ApiOperation(value = "findUserAll", notes = "findUserAll")
	@RequestMapping(value = "/findUserAll", method = RequestMethod.GET)
	public List<User> findUserAll() {
		List<User> list = baseJDBCRepository.jdbcSqlQueryList("SELECT * FROM SYS_USER", User.class);
		return list;
	}

	@ResponseBody
	@ApiOperation(value = "findUserAllsql", notes = "findUserAllsql")
	@RequestMapping(value = "/findUserAllsql", method = RequestMethod.GET)
	public List findUserAllsql() {
		return baseJDBCRepository.jdbcsql("select * from sys_user");
	}

	@ResponseBody
	@ApiOperation(value = "findUserByWXID", notes = "findUserByWXID")
	@ApiImplicitParam(name = "wxid", value = "wxid", required = true, dataType = "String")
	@RequestMapping(value = "/findUserByWXID", method = RequestMethod.POST)
	public User findUserByWXID(Integer wxid) {
		return userRepository.findUserByID(wxid);
	}

	@ResponseBody
	@ApiOperation(value = "create", notes = "create")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public User creation(String name) {
		User user = new User();
		user.setName(name);
		user.setId(UUID.randomUUID().toString());
		user.setCreate_time(getDateString());
		logger.info("create" + JSON.toJSONString(user));
		return userRepository.save(user);
	}

	public static void main(String[] args) {
		System.out.println(getMD5("123"));
	}

	public static String getDateString() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	public static String getMD5(String str) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			return new BigInteger(1, md.digest()).toString(16);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
