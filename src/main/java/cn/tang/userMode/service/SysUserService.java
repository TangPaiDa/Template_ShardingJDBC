package cn.tang.userMode.service;

import cn.tang.common.utils.ResultJsonBean;
import cn.tang.userMode.entity.SysUserEntity;
import cn.tang.userMode.vo.SysUserVo;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 系统用户信息表 服务类
 * </p>
 *
 * @author Tangpd
 * @since 2020-10-22
 */
public interface SysUserService extends IService<SysUserEntity> {

    ResultJsonBean getSysUserList(SysUserVo sysUserVo);

    ResultJsonBean login(SysUserEntity sysUserEntity);

    ResultJsonBean updateSysUser(SysUserVo sysUserVo);

    ResultJsonBean addSysUser(SysUserEntity sysUserEntity, HttpServletRequest request);

}
