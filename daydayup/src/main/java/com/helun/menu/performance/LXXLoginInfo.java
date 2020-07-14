package com.helun.menu.performance;

import java.io.Serializable;
import java.util.Date;

import com.helun.menu.model.BaseEntity;

/**
 * 登录信息数据模型类
 *
 * @author ava 祝陈超
 * @date 2015.01.25
 */
public class LXXLoginInfo extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id(目前使用session id作为id)
     */
    private String infoId;

    /**
     * 用户id
     */
    private Long userId;



    private String userJson;

    /**
     * 用户类型(登录类型)
     */
    private Integer userType;
    
    /**
     * 用户登录学校id(备注：目前登录的时候没有选择学校，所以系统只是默认选择第一个学校作为登录学校id)
     */
    private Long loginSchoolId;
    
    private Long loginClassId;
    
    private String loginSchoolKey;



    private String clientInfoJson;

    /**
     * 消息推送token
     */
    private String pushToken;

    /**
     * 登录时间
     */
    private Date loginTime;
    
    private String loginResult;

    /**
     * 当前学校是否是系统创建
     */
    private boolean systemCreate; //TODO 跟校园app业务有关，必须移到userSetting
    
    private int logStatus = 3;
    
    private Integer moduleId; // = 0;
    
    /**
     * 获取主键id(目前使用session id作为id)
     * @return
     */
    public String getInfoId() {
        return infoId;
    }

    /**
     * 设置主键id(目前使用session id作为id)
     * @param infoId
     */
    public void setInfoId(String infoId) {
        this.infoId = infoId;
    }

    /**
     * 获取用户id
     *
     * @return 用户id
     */
    public Long getUserId() {
        //return user.getUserId();
    	return this.userId;
    }

    /**
     * 设置用户id
     *
     * @param userId 用户id
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }



    /**
     * 获取用户类型
     *
     * @return 用户类型
     */
    public Integer getUserType() {
        return userType;
    }

    /**
     * 设置用户类型
     *
     * @param userType 用户类型
     */
    public void setUserType(Integer userType) {
        this.userType = userType;
    }



    /**
     * 获取消息推送token
     *
     * @return 消息推送token
     */
    public String getPushToken() {
        return pushToken;
    }

    /**
     * 设置消息推送token
     *
     * @param pushToken 消息推送token
     */
    public void setPushToken(String pushToken) {
        this.pushToken = pushToken;
    }

    /**
     * 获取登录时间
     *
     * @return 登录时间
     */
    public Date getLoginTime() {
        return loginTime;
    }

    /**
     * 设置登录时间
     *
     * @param loginTime 登录时间
     */
    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }
    
    /**
     * 获取登录学校id
     * @return
     */
    public Long getLoginSchoolId() {
    	return loginSchoolId;
    }
    
    /**
     * 设置登录学校id
     *
     * @param loginSchoolId 登录学校id
     */
    public void setLoginSchoolId(Long loginSchoolId) {
    	this.loginSchoolId = loginSchoolId;
    }
    
	public Long getLoginClassId() {
		return loginClassId;
	}

	public void setLoginClassId(Long loginClassId) {
		this.loginClassId = loginClassId;
	}

    public boolean getSystemCreate() {
        return systemCreate;
    }

    public void setSystemCreate(boolean systemCreate) {
        this.systemCreate = systemCreate;
    }

    public int getLogStatus() {
		return logStatus;
	}

	public void setLogStatus(int logStatus) {
		this.logStatus = logStatus;
	}

	public Integer getModuleId() {
		return moduleId;
	}

	public void setModuleId(Integer moduleId) {
		this.moduleId = moduleId;
	}

    public String getUserJson() {
        return userJson;
    }

    public void setUserJson(String userJson) {
        this.userJson = userJson;
    }

    public String getClientInfoJson() {
        return clientInfoJson;
    }

    public void setClientInfoJson(String clientInfoJson) {
        this.clientInfoJson = clientInfoJson;
    }

    public String getLoginSchoolKey() {
		return loginSchoolKey;
	}

	public void setLoginSchoolKey(String loginSchoolKey) {
		this.loginSchoolKey = loginSchoolKey;
	}

	public String getLoginResult() {
		return loginResult;
	}

	public void setLoginResult(String loginResult) {
		this.loginResult = loginResult;
	}

}
