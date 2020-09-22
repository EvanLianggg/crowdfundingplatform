package com.atguigu.crowd.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.entity.AdminExample;
import com.atguigu.crowd.entity.AdminExample.Criteria;
import com.atguigu.crowd.exception.LoginAcctAlreadyInUseException;
import com.atguigu.crowd.exception.LoginAcctAlreadyInUseForUpdateException;
import com.atguigu.crowd.exception.LoginFailedException;
import com.atguigu.crowd.mapper.AdminMapper;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.util.CrowdUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class AdminServiceImpl implements AdminService {
	
	@Autowired
	private AdminMapper adminMapper;
	
	private Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Override
	public void saveAdmin(Admin admin) {
		
		// 1. Password encryption
		String userPswd = admin.getUserPswd();
		// userPswd = CrowdUtil.md5(userPswd);
		userPswd = passwordEncoder.encode(userPswd);
		admin.setUserPswd(userPswd);
		
		// 2. Generation creation time
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String createTime = format.format(date);
		admin.setCreateTime(createTime);
		
		// 3. Perform save
		try {
			adminMapper.insert(admin);
		} catch (Exception e) {
			e.printStackTrace();
			
			logger.info("异常全类名="+e.getClass().getName());
			
			if(e instanceof DuplicateKeyException) {
				throw new LoginAcctAlreadyInUseException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
			}
		}
		
	}

	@Override
	public List<Admin> getAll() {
		return adminMapper.selectByExample(new AdminExample());
	}

	@Override
	public Admin getAdminByLoginAcct(String loginAcct, String userPswd) {
		
		// 1. Query the Admin object according to the login account
		// ①Create AdminExample object

		AdminExample adminExample = new AdminExample();
		
		// ②Create a Criteria object

		Criteria criteria = adminExample.createCriteria();
		
		// ③Encapsulate query conditions in the Criteria object
		criteria.andLoginAcctEqualTo(loginAcct);
		
		// ④Call the method of AdminMapper to execute the query
		List<Admin> list = adminMapper.selectByExample(adminExample);
		
		// 2. Determine whether the Admin object is null
		if(list == null || list.size() == 0) {
			throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
		}
		
		if(list.size() > 1) {
			throw new RuntimeException(CrowdConstant.MESSAGE_SYSTEM_ERROR_LOGIN_NOT_UNIQUE);
		}
		
		Admin admin = list.get(0);
		
		// 3. Throw an exception if the Admin object is null
		if(admin == null) {
			throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
		}
		
		// 4. If the Admin object is not null, remove the database password from the Admin object
		String userPswdDB = admin.getUserPswd();
		
		// 5. Encrypt the plain text password submitted by the form
		String userPswdForm = CrowdUtil.md5(userPswd);
		
		// 6. Compare the passwords
		if(!Objects.equals(userPswdDB, userPswdForm)) {
			// 7. If the comparison result is inconsistent, throw an exception
			throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
		}
		
		// 8. If consistent, return to the Admin object
		return admin;
	}

	@Override
	public PageInfo<Admin> getPageInfo(String keyword, Integer pageNum, Integer pageSize) {
		
		// 1. Call the static method of PageHelper to turn on the paging function
		// This fully reflects the "non-intrusive" design of PageHelper: the original query does not need to be modified
		PageHelper.startPage(pageNum, pageSize);
		
		// 2. Execute query
		List<Admin> list = adminMapper.selectAdminByKeyword(keyword);
		
		// 3. Encapsulated in the PageInfo object
		return new PageInfo<>(list);
	}

	@Override
	public void remove(Integer adminId) {
		adminMapper.deleteByPrimaryKey(adminId);
	}

	@Override
	public Admin getAdminById(Integer adminId) {
		return adminMapper.selectByPrimaryKey(adminId);
	}

	@Override
	public void update(Admin admin) {
		
		// "Selective" indicates a selective update, and the field with a null value is not updated
		try {
			adminMapper.updateByPrimaryKeySelective(admin);
		} catch (Exception e) {
			e.printStackTrace();
			
			logger.info("Exception full class neme ="+e.getClass().getName());
			
			if(e instanceof DuplicateKeyException) {
				throw new LoginAcctAlreadyInUseForUpdateException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
			}
		}
	}

	@Override
	public void saveAdminRoleRelationship(Integer adminId, List<Integer> roleIdList) {
		
		// 1. Delete old association data according to adminId
		adminMapper.deleteOLdRelationship(adminId);
		
		// 2. Save the new association relationship according to roleIdList and adminId
		if(roleIdList != null && roleIdList.size() > 0) {
			adminMapper.insertNewRelationship(adminId, roleIdList);
		}
	}

	@Override
	public Admin getAdminByLoginAcct(String username) {
		
		AdminExample example = new AdminExample();
		
		Criteria criteria = example.createCriteria();
		
		criteria.andLoginAcctEqualTo(username);
		
		List<Admin> list = adminMapper.selectByExample(example);
		
		Admin admin = list.get(0);
		
		return admin;
	}

}
