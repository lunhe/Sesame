package com.helun.menu.performance;


import java.util.List;
import java.util.Map;

/**
 * 登录管理业务层接口
 *
 * @author ava 祝陈超
 * @date 2015.01.26
 */
public interface ILoginService {
    /**
     * 获取当前在线用户的登录信息
     *
     * @return 登录信息
     */
    LXXLoginInfo getLoginInfo();

    /**
     * 获取某个在线用户的登录信息
     *
     * @param userId 用户id
     * @return 登录信息
     */
    LXXLoginInfo getLoginInfo(Long userId);
    /**
     *	 设置loginInfo，用于跨线程传递loginInfo
     * @param loginInfo
     */
    void setLoginInfo(LXXLoginInfo loginInfo);
    /**
     * 获取所有在线用户的登录信息
     *
     * @return 登录信息列表
     */
    List<LXXLoginInfo> getLoginInfoList();

    /**
     * 获取登陆角色
     * @return
     */
    Integer getLoginUserType();
    
    String getLoginSchoolKey();
    
    /**
     * 获取登录学校id
     * @return
     */
    Long getLoginSchoolId();

  

    /**
     * 获取当前在线用户id
     *
     * @return 用户id
     */
    Long getUserId();


    /**
     * 用户登进
     *
     * @param loginInfo 用户登录信息
     * @return 登进状态
     */
    int Login(LXXLoginInfo loginInfo);

    /**
     * 当前在线用户登出
     *
     * @return 登出状态
     */
    int Logout(LXXLoginInfo loginInfo);

    int setPushToken(String pushToken);

    int setLoginUserType(Integer userTypeId);
    
    Map<String, Object> setLoginSchoolId(Long schoolId);
    
    Map<String, Object> setLoginClassId(Long classId);
    
    Map<String, Object> getLoginSuccessInfo();
    
    Long getSelectedSchoolId(Long userId);

    boolean checkLoginSchool(LXXLoginInfo loginInfo);

    /**
     * 角色列表
     * @return
     */
    Map<String,Object> listRoles();

    /**
     * 删除用户默认的学校设置
     * @param userId
     */
    void deleteUserSettingByUserId(Long userId);

    /**
     *  获取token
     * @param tempLoginInfo
     * @param receiverAndroidTokens
     * @param receiverIOSTokens
     */
    void listGroupDeviceType(LXXLoginInfo tempLoginInfo,List<String> receiverAndroidTokens,List<String> receiverIOSTokens);
    
    LXXLoginInfo getLoginInfoForVersion(String token);
}
