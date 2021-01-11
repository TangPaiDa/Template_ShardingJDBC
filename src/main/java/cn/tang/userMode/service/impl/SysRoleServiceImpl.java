package cn.tang.userMode.service.impl;

import cn.tang.common.utils.*;
import cn.tang.userMode.dao.SysRolePermissionsDao;
import cn.tang.userMode.dao.SysUserRoleDao;
import cn.tang.userMode.entity.SysRoleEntity;
import cn.tang.userMode.dao.SysRoleDao;
import cn.tang.userMode.entity.SysRolePermissionsEntity;
import cn.tang.userMode.entity.SysUserRoleEntity;
import cn.tang.userMode.service.SysRoleService;
import cn.tang.userMode.vo.SysUserRoleVo;
import cn.tang.userMode.vo.SysUserVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 * 角色信息表 服务实现类
 * </p>
 *
 * @author Tangpd
 * @since 2020-12-03
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleDao, SysRoleEntity> implements SysRoleService {

    @Resource
    private SysRoleDao sysRoleDao;

    @Resource
    private SysUserRoleDao sysUserRoleDao;

    @Resource
    private SysRolePermissionsDao sysRolePermissionsDao;

    /**
     *
     * 功能描述: 获取所有角色信息
     *
     * @param: []
     * @return: cn.tang.common.utils.ResultJsonBean
     * @auther: tangpd
     * @date: 2020/12/8 13:41
     */
    @Override
    public ResultJsonBean getAllRole() {
        List<SysRoleEntity> allRole = sysRoleDao.selectList(new QueryWrapper<SysRoleEntity>().orderByAsc("sort"));
        return ResultUtil.getSuccessResultJsonBean(allRole);
    }

    /**
     *
     * 功能描述: 删除角色信息
     *
     * @param: [sysRoleEntity]
     * @return: cn.tang.common.utils.ResultJsonBean
     * @auther: tangpd
     * @date: 2020/12/3 18:09
     */
    @Override
    @Transactional
    public ResultJsonBean deleteRole(SysRoleEntity sysRoleEntity) {

        String roleId = sysRoleEntity.getRoleId();
        SysRoleEntity sysRoleEntityOld = sysRoleDao.selectOne(new QueryWrapper<SysRoleEntity>().eq("role_id", roleId));
        if (null == sysRoleEntityOld){
            return ResultUtil.getErrorResultJsonBean(ResultJsonEunm.ERROR_FOR_PARAM_ERROR, "不存在该角色信息");
        }

        //删除“用户-角色”关联关系
        sysUserRoleDao.delete(new QueryWrapper<SysUserRoleEntity>().eq("role_id", roleId));

        //删除“角色-权限”关联关系
        sysRolePermissionsDao.delete(new QueryWrapper<SysRolePermissionsEntity>().eq("role_id", roleId));

        //删除角色信息
        int deleteNum = sysRoleDao.delete(new QueryWrapper<SysRoleEntity>().eq("role_id", roleId));
        if (deleteNum < 1){
            throw new ExceptionForMyCustom(ResultJsonEunm.ERROR_FOR_DB_DML_ERROR, "删除出错，中断操作");
        }
        return ResultUtil.getSuccessResultJsonBean("成功删除该条记录");
    }

    /**
     *
     * 功能描述: 修改角色信息
     *
     * @param: [sysRoleEntity]
     * @return: cn.tang.common.utils.ResultJsonBean
     * @auther: tangpd
     * @date: 2020/12/3 17:27
     */
    @Override
    public ResultJsonBean updateRole(SysRoleEntity sysRoleEntity) {


        String roleId = sysRoleEntity.getRoleId();
        SysRoleEntity sysRoleEntityOld = sysRoleDao.selectOne(new QueryWrapper<SysRoleEntity>().eq("role_id", roleId));
        if (null == sysRoleEntityOld){
            return ResultUtil.getErrorResultJsonBean(ResultJsonEunm.ERROR_FOR_PARAM_ERROR, "不存在该角色信息");
        }

        //校验是否重名
        String roleName = sysRoleEntity.getRoleName();
        sysRoleEntityOld = sysRoleDao.selectOne(new QueryWrapper<SysRoleEntity>()
                .eq("role_name", roleName).ne("role_id", sysRoleEntity.getRoleId()));
        if (null != sysRoleEntityOld){
            return ResultUtil.getErrorResultJsonBean(ResultJsonEunm.ERROR_FOR_PARAM_EXIST, "已存在同名角色，修改失败");
        }

        //修改
        int updateNum = sysRoleDao.update(sysRoleEntity, new UpdateWrapper<SysRoleEntity>().eq("role_id", sysRoleEntity.getRoleId()));
        if (updateNum < 1){
            return ResultUtil.getErrorResultJsonBean(ResultJsonEunm.ERROR_FOR_DB_DML_ERROR, "修改角色信息失败");
        }

        return ResultUtil.getSuccessResultJsonBean("修改角色信息成功");
    }

    /**
     *
     * 功能描述: 添加角色信息
     *
     * @param: [sysRoleEntity]
     * @return: cn.tang.common.utils.ResultJsonBean
     * @auther: tangpd
     * @date: 2020/12/3 17:13
     */
    @Override
    public ResultJsonBean addRole(SysRoleEntity sysRoleEntity) {

        SysRoleEntity sysRoleEntityOld = sysRoleDao.selectOne(new QueryWrapper<SysRoleEntity>().eq("role_name", sysRoleEntity.getRoleName()));
        if (null != sysRoleEntityOld){
            return ResultUtil.getErrorResultJsonBean(ResultJsonEunm.ERROR_FOR_PARAM_EXIST, "已存在同名角色，新增失败");
        }

        //新增
        String roleId = StringUtil.getUUID();
        sysRoleEntity.setRoleId(roleId);
        sysRoleEntity.setCreateTime(LocalDateTime.now());
        int addNum = sysRoleDao.insert(sysRoleEntity);
        if (addNum < 1){
            return ResultUtil.getErrorResultJsonBean(ResultJsonEunm.ERROR_FOR_DB_DML_ERROR, "添加角色信息失败");
        }

        return ResultUtil.getSuccessResultJsonBean(roleId);
    }

    //=============================================================用户-角色关联相关=====================================================================

    /**
     *
     * 功能描述: 获取用户拥有的角色
     *
     * @param: []
     * @return: cn.tang.common.utils.ResultJsonBean
     * @auther: tangpd
     * @date: 2020/12/7 14:11
     */
    @Override
    public ResultJsonBean getUserRoleList(SysUserVo sysUserVo) {
        return ResultUtil.getSuccessResultJsonBean(sysRoleDao.getUserRoleListByUserId(sysUserVo.getUserId()));
    }

    /**
     *
     * 功能描述: 批量增加用户-角色关联关系
     *  前端需要把之前存在的角色 id 一起传过来，然后后台这边先删，然后再全部增加一遍
     *
     * @param: [userRoleVo]
     * @return: cn.tang.common.utils.ResultJsonBean
     * @auther: tangpd
     * @date: 2020/12/3 18:07
     */
    @Override
    @Transactional
    public ResultJsonBean addUserRole(SysUserRoleVo userRoleVo) {

        //1、先清除旧数据
        sysUserRoleDao.delete(new QueryWrapper<SysUserRoleEntity>().eq("user_id", userRoleVo.getUserId()));

        //2、再增加新数据
        SysUserRoleEntity sysUserRoleEntity = new SysUserRoleEntity();
        sysUserRoleEntity.setUserId(userRoleVo.getUserId());
        List<String> roleIdList = userRoleVo.getRoleIdList();
        AtomicInteger addNum = new AtomicInteger();
        roleIdList.forEach(
                (roleId) -> {
                    sysUserRoleEntity.setUserRoleId(StringUtil.getUUID());
                    sysUserRoleEntity.setRoleId(roleId);
                    try {
                        addNum.addAndGet(sysUserRoleDao.insert(sysUserRoleEntity));
                    } catch (Exception e) {
                        throw new ExceptionForMyCustom(ResultJsonEunm.ERROR_FOR_DB_DML_ERROR, "存在错误的用户数据或者角色数据（对应id的数据不存在），请检查之后重新添加");
                    }
                }
        );
        return ResultUtil.getSuccessResultJsonBean("成功添加了" + addNum + "条数据");
    }

    //=============================================================角色-权限关联相关=====================================================================

    /**
     *
     * 功能描述: 获取角色所拥有的权限列表
     *
     * @param: [sysRoleEntity]
     * @return: cn.tang.common.utils.ResultJsonBean
     * @auther: tangpd
     * @date: 2020/12/14 18:00
     */
    @Override
    public ResultJsonBean getPermissionsByRoleId(SysRoleEntity sysRoleEntity) {
        List<String> permissionsList = sysRoleDao.getPermissionsByRoleId(sysRoleEntity.getRoleId());
        return ResultUtil.getSuccessResultJsonBean(permissionsList);
    }
}
