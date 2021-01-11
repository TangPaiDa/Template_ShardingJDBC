package cn.tang.userMode.service.impl;

import cn.tang.common.utils.*;
import cn.tang.userMode.dao.SysPermissionsDao;
import cn.tang.userMode.dao.SysRoleDao;
import cn.tang.userMode.entity.SysUserEntity;
import cn.tang.userMode.dao.SysUserDao;
import cn.tang.userMode.service.SysUserService;
import cn.tang.userMode.vo.SysUserVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static cn.tang.common.jwt.JwtUtil.getJWTToken;

/**
 * <p>
 * 系统用户信息表 服务实现类
 * </p>
 *
 * @author Tangpd
 * @since 2020-10-22
 */
@Service
@Slf4j
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUserEntity> implements SysUserService {

    @Resource
    private SysUserDao sysUserDao;

    @Resource
    private SysRoleDao sysRoleDao;

    @Resource
    private SysPermissionsDao sysPermissionsDao;

    /**
     *
     * 功能描述: 用户登录
     *
     * @param: [sysUserEntity]
     * @return: cn.tang.common.utils.ResultJsonBean
     * @auther: tangpd
     * @date: 2020/10/23 15:25
     */
    @Override
    public ResultJsonBean login(SysUserEntity sysUserEntity) {

        //1、根据用户名获取数据库中的用户数据(用户名、手机号码、邮箱均可作为账号)
        SysUserEntity sysUserDB = sysUserDao.selectOne(new QueryWrapper<SysUserEntity>()
                .notIn("status", "1","2")
                .eq("user_name", sysUserEntity.getUserName())
        );
        if (null == sysUserDB){
            throw new ExceptionForMyCustom(ResultJsonEunm.ERROR_FOR_USER_NO_EXIST);
        }

        //2、验证密码
        String dbPassword = sysUserDB.getPassword();//数据库密码
        String userPassword = sysUserEntity.getPassword();//用户登录输入的密码
        if (StringUtil.is_empty(userPassword) ) {
            throw new ExceptionForMyCustom(ResultJsonEunm.ERROR_FOR_USER_PASSWORD_ERROR);
        }
        String salt = sysUserDB.getSalt();
        String userPasswordBySalt = getPasswordByMd5(userPassword, salt);
        if (!userPasswordBySalt.equals(dbPassword)){
            return ResultUtil.getErrorResultJsonBean(ResultJsonEunm.ERROR_FOR_USER_PASSWORD_ERROR);
        }

        //3、获取 jwt token 返回给前端，有效期为 2 小时
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.HOUR, 2);
//        instance.add(Calendar.SECOND, 30);//根据项目实际需求修改 token 过期时间
        String userRoleListStr = sysRoleDao.getUserRoleListStrByUserId(sysUserDB.getUserId());
        String userPermissionsListStr = sysPermissionsDao.getUserPermissionsListStrByUserId(sysUserDB.getUserId());
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("userId", sysUserDB.getUserId());
        userInfo.put("userRole", userRoleListStr);
        userInfo.put("userPermissions", userPermissionsListStr);
        String token = getJWTToken(userInfo, instance.getTime());

        return ResultUtil.getSuccessResultJsonBean(token);
    }

    /**
     *
     * 功能描述: 分页获取系统管理用户数据
     *
     * @param: [sysUserVo]
     * @return: cn.tang.common.utils.ResultJsonBean
     * @auther: tangpd
     * @date: 2020/12/1 15:17
     */
    @Override
    public ResultJsonBean getSysUserList(SysUserVo sysUserVo) {

        String selectParam = sysUserVo.getSelectParam();
        Page page = new Page(sysUserVo.getPageNum(), sysUserVo.getPageSize());
        IPage<Map<String, Object>> userEntityPage = sysUserDao.selectMapsPage(page, new QueryWrapper<SysUserEntity>()
                .eq(!StringUtil.is_empty(sysUserVo.getType()), "type", sysUserVo.getType())
                .eq(!StringUtil.is_empty(sysUserVo.getStatus()), "status", sysUserVo.getStatus())
                .and(!StringUtil.is_empty(selectParam), i -> i
                        .or().like("user_name", selectParam)
                        .or().like("phone", selectParam)
                        .or().like("email", selectParam)
                        .or().like("user_id", selectParam))
                .orderByDesc("create_time")
                .select("user_id userId, user_name userName, phone, email, create_time createTime, type, status"));//根据创建时间排序，最后新增的在最前面
        return ResultUtil.getSuccessResultJsonBean(userEntityPage);
    }

    /**
     *
     * 功能描述: 修改用户信息
     * 包括重置密码、删除用户等；删除用户为逻辑删除
     *
     * @param: [sysUserVo]
     * @return: cn.tang.common.utils.ResultJsonBean
     * @auther: tangpd
     * @date: 2020/10/23 13:36
     */
    @Override
    public ResultJsonBean updateSysUser(SysUserVo sysUserVo) {
        //0、先获取数据库中的数据
        SysUserEntity sysUserEntity = sysUserDao.selectOne(new QueryWrapper<SysUserEntity>().eq("user_id", sysUserVo.getUserId()));
        if (null == sysUserEntity){
            return ResultUtil.getErrorResultJsonBean(ResultJsonEunm.ERROR_FOR_DB_NOT_EXIST, "不存在用户数据：" + sysUserVo.getUserId());
        }

        //1、判断是否是修改手机号码、邮箱、用户名，若是则需要判断是否被绑定
        SysUserEntity sysUserEntityOld = null;
        if (!StringUtil.is_empty(sysUserVo.getUserName())){
            sysUserEntityOld = sysUserDao.selectOne(new QueryWrapper<SysUserEntity>().eq("user_name", sysUserVo.getUserName()));
            if (null != sysUserEntityOld){
                return ResultUtil.getErrorResultJsonBean(ResultJsonEunm.ERROR_FOR_PARAM_EXIST, "该用户名已被注册");
            }
        }
        if (!StringUtil.is_empty(sysUserVo.getPhone())){
            sysUserEntityOld = sysUserDao.selectOne(new QueryWrapper<SysUserEntity>().eq("phone", sysUserVo.getPhone()));
            if (null != sysUserEntityOld){
                return ResultUtil.getErrorResultJsonBean(ResultJsonEunm.ERROR_FOR_PARAM_EXIST, "该手机号码已被绑定");
            }
        }
        if (!StringUtil.is_empty(sysUserVo.getEmail())){
            sysUserEntityOld = sysUserDao.selectOne(new QueryWrapper<SysUserEntity>().eq("email", sysUserVo.getEmail()));
            if (null != sysUserEntityOld){
                return ResultUtil.getErrorResultJsonBean(ResultJsonEunm.ERROR_FOR_PARAM_EXIST, "该邮箱已被绑定");
            }
        }

        //2、是否为重置密码
        String oldPassword = sysUserVo.getOldPassword();
        String newPassword = sysUserVo.getNewPassword();
        if (!StringUtil.is_empty(oldPassword) && !StringUtil.is_empty(newPassword)) {
            String salt = sysUserEntity.getSalt();
            //验证旧密码有效性
            String oldPasswordBySalt = getPasswordByMd5(oldPassword, salt);
            if (!oldPasswordBySalt.equals(sysUserEntity.getPassword())){
                return ResultUtil.getErrorResultJsonBean(ResultJsonEunm.ERROR_FOR_PARAM_ERROR, "旧密码错误，重置密码失败");
            }
            //旧密码正确之后，修改密码为新密码
            String newPasswordBySalt = getPasswordByMd5(newPassword, salt);
            sysUserEntity.setPassword(newPasswordBySalt);
        }

        //3、修改用户信息
        ReflectionUtil.assignmentObjectByObject(sysUserVo, sysUserEntity);
        sysUserEntity.setUpdateTime(LocalDateTime.now());
        int updateNum = sysUserDao.update(sysUserEntity, new UpdateWrapper<SysUserEntity>().eq("user_id", sysUserVo.getUserId()));
        if (updateNum < 1){
            return ResultUtil.getErrorResultJsonBean(ResultJsonEunm.ERROR_FOR_DB_DML_ERROR, "修改用户信息失败");
        }
        return ResultUtil.getSuccessResultJsonBean("修改用户信息成功");
    }

    /**
     *
     * 功能描述: 增加后台管理系统用户
     *
     * @param: [sysUserEntity]
     * @return: cn.tang.common.utils.ResultJsonBean
     * @auther: tangpd
     * @date: 2020/10/23 9:27
     */
    @Override
    public ResultJsonBean addSysUser(SysUserEntity sysUserEntity, HttpServletRequest request) {
        //1、验证参数是否重复；若是移动端的，需要验证手机号码、邮箱的有效性
        SysUserEntity sysUserEntityOld = sysUserDao.selectOne(new QueryWrapper<SysUserEntity>().eq("user_name", sysUserEntity.getUserName()));
        if (null != sysUserEntityOld){
            return ResultUtil.getErrorResultJsonBean(ResultJsonEunm.ERROR_FOR_PARAM_EXIST, "该用户名已被注册");
        }
        sysUserEntityOld = sysUserDao.selectOne(new QueryWrapper<SysUserEntity>().eq("phone", sysUserEntity.getPhone()));
        if (null != sysUserEntityOld){
            return ResultUtil.getErrorResultJsonBean(ResultJsonEunm.ERROR_FOR_PARAM_EXIST, "该手机号码已被绑定");
        }
        sysUserEntityOld = sysUserDao.selectOne(new QueryWrapper<SysUserEntity>().eq("email", sysUserEntity.getEmail()));
        if (null != sysUserEntityOld){
            return ResultUtil.getErrorResultJsonBean(ResultJsonEunm.ERROR_FOR_PARAM_EXIST, "该邮箱已被绑定");
        }

        //2、生成加密密码
        String salt = StringUtil.getRandomStringByNum(6, StringUtil.RANDOM_STR_$);
        String passwordByMd5AndSaltAndHash = getPasswordByMd5(sysUserEntity.getPassword(), salt);

        //3、保存加密密码、加密用到的盐等信息到数据库中
        String userId = StringUtil.getUUID();
        sysUserEntity.setPassword(passwordByMd5AndSaltAndHash);
        sysUserEntity.setSalt(salt);
        sysUserEntity.setUserId(userId);
        sysUserEntity.setCreateTime(LocalDateTime.now());
        sysUserEntity.setCreateUser(SysUserInfoUtil.getSysUserId(request));

        int addNum = sysUserDao.insert(sysUserEntity);
        if (addNum < 1){
            return ResultUtil.getErrorResultJsonBean(ResultJsonEunm.ERROR_FOR_DB_DML_ERROR, "添加系统用户失败");
        }
        return ResultUtil.getSuccessResultJsonBean(userId);
    }

    //==================================================================================内部调用方法================================================================================

    /**
     *
     * 功能描述: 使用 md5 + salt 处理 + hash 散列，生成加密密码
     *
     * @param: [psw, salt]
     * @return: java.lang.String
     * @auther: tangpd
     * @date: 2020/10/23 14:33
     */
    private String getPasswordByMd5(String psw, String salt){
        if (StringUtil.is_empty(salt)){
            salt = StringUtil.getRandomStringByNum(6, StringUtil.RANDOM_STR_$);//获取随机生成的盐
        }
        String passwordByMd5 = DataMD5Util.md5Str(salt, psw);
        return passwordByMd5;
    }
}
