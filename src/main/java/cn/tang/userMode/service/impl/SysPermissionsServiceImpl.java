package cn.tang.userMode.service.impl;

import cn.tang.common.utils.*;
import cn.tang.userMode.dao.SysRolePermissionsDao;
import cn.tang.userMode.entity.SysPermissionsEntity;
import cn.tang.userMode.dao.SysPermissionsDao;
import cn.tang.userMode.entity.SysRolePermissionsEntity;
import cn.tang.userMode.service.SysPermissionsService;
import cn.tang.userMode.vo.SysRolePermissionsVo;
import cn.tang.userMode.vo.SysUserVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 * 权限信息表 服务实现类
 * </p>
 *
 * @author Tangpd
 * @since 2020-12-02
 */
@Service
@Slf4j
public class SysPermissionsServiceImpl extends ServiceImpl<SysPermissionsDao, SysPermissionsEntity> implements SysPermissionsService {

    @Resource
    private SysPermissionsDao sysPermissionsDao;

    @Resource
    private SysRolePermissionsDao sysRolePermissionsDao;


    private static final String DEFAULT_PARENTID_FOR_FIRST = "1";//默认权限一级菜单父id

    /**
     *
     * 功能描述: 获取全部权限信息（树状结构）
     *
     * @param: []
     * @return: cn.tang.common.utils.ResultJsonBean
     * @auther: tangpd
     * @date: 2020/12/8 13:49
     */
    @Override
    public ResultJsonBean getAllPermissions() {
        //先获取一级权限信息
        List<Map<String, Object>> permissionsList = sysPermissionsDao.getUserPermissionsListByParentId(DEFAULT_PARENTID_FOR_FIRST);
        if (permissionsList.isEmpty()){
            return ResultUtil.getSuccessResultJsonBean(null);
        }
        //再以本身id为父id，递归获取子集权限信息
        getUnitChildPermissionsList(permissionsList, null);
        return ResultUtil.getSuccessResultJsonBean(permissionsList);
    }

    /**
     *
     * 功能描述: 删除权限信息
     *
     * @param: [sysPermissionsEntity]
     * @return: cn.tang.common.utils.ResultJsonBean
     * @auther: tangpd
     * @date: 2020/12/3 10:11
     */
    @Override
    @Transactional
    public ResultJsonBean deletePermissions(SysPermissionsEntity sysPermissionsEntity) {

        String permissionsId = sysPermissionsEntity.getPermissionsId();
        SysPermissionsEntity sysPermissionsEntityOld = sysPermissionsDao.selectOne(new QueryWrapper<SysPermissionsEntity>().eq("permissions_id", permissionsId));
        if (null == sysPermissionsEntityOld){
            return ResultUtil.getErrorResultJsonBean(ResultJsonEunm.ERROR_FOR_PARAM_ERROR, "不存在该权限信息");
        }

        //1、先删除“角色-权限”关联关系
        sysRolePermissionsDao.delete(new QueryWrapper<SysRolePermissionsEntity>().eq("permissions_id", permissionsId));

        //2、再删除权限记录
        int deletePermissionsNum = sysPermissionsDao.delete(new QueryWrapper<SysPermissionsEntity>().eq("permissions_id", permissionsId));

        if (deletePermissionsNum < 1){
            throw new ExceptionForMyCustom(ResultJsonEunm.ERROR_FOR_DB_DML_ERROR, "删除出错，中断操作");
        }
        return ResultUtil.getSuccessResultJsonBean("成功删除该条记录");
    }

    /**
     *
     * 功能描述: 修改权限信息
     *
     * @param: [sysPermissionsEntity]
     * @return: cn.tang.common.utils.ResultJsonBean
     * @auther: tangpd
     * @date: 2020/12/2 16:05
     */
    @Override
    public ResultJsonBean updatePermissions(SysPermissionsEntity sysPermissionsEntity, HttpServletRequest request) {

        String permissionsId = sysPermissionsEntity.getPermissionsId();
        SysPermissionsEntity sysPermissionsEntityOld = sysPermissionsDao.selectOne(new QueryWrapper<SysPermissionsEntity>().eq("permissions_id", permissionsId));
        if (null == sysPermissionsEntityOld){
            return ResultUtil.getErrorResultJsonBean(ResultJsonEunm.ERROR_FOR_PARAM_ERROR, "不存在该权限信息");
        }

        //属于同一父id时，不允许存在同名
        String newPermissionsName = sysPermissionsEntity.getPermissionsName();
        if (!StringUtil.is_empty(newPermissionsName)){
            sysPermissionsEntityOld = sysPermissionsDao.selectOne(new QueryWrapper<SysPermissionsEntity>()
                    .eq("permissions_name", newPermissionsName)
                    .eq("parent_id", sysPermissionsEntityOld.getParentId())
                    .ne("permissions_id", permissionsId)
            );
            if (null != sysPermissionsEntityOld){
                return ResultUtil.getErrorResultJsonBean(ResultJsonEunm.ERROR_FOR_PARAM_EXIST, "该权限名称已被同级其他权限占用，修改失败");
            }
        }

        //修改
        sysPermissionsEntity.setUpdateTime(LocalDateTime.now());
        sysPermissionsEntity.setUpdateUser(SysUserInfoUtil.getSysUserId(request));
        int updateNum = sysPermissionsDao.update(sysPermissionsEntity, new UpdateWrapper<SysPermissionsEntity>().eq("permissions_id", permissionsId));
        if (updateNum < 1){
            return ResultUtil.getErrorResultJsonBean(ResultJsonEunm.ERROR_FOR_DB_DML_ERROR, "修改权限信息失败");
        }
        return ResultUtil.getSuccessResultJsonBean("修改权限信息成功");
    }

    /**
     *
     * 功能描述: 增加权限信息
     *
     * @param: [sysPermissionsEntity]
     * @return: cn.tang.common.utils.ResultJsonBean
     * @auther: tangpd
     * @date: 2020/12/2 14:09
     */
    @Override
    public ResultJsonBean addPermissions(SysPermissionsEntity sysPermissionsEntity, HttpServletRequest request) {

        //属于同一父id时，不允许存在同名
        SysPermissionsEntity sysPermissionsEntityOld = sysPermissionsDao.selectOne(new QueryWrapper<SysPermissionsEntity>()
                .eq("permissions_name", sysPermissionsEntity.getPermissionsName())
                .eq("parent_id", sysPermissionsEntity.getParentId()));
        if (null != sysPermissionsEntityOld){
            return ResultUtil.getErrorResultJsonBean(ResultJsonEunm.ERROR_FOR_PARAM_EXIST, "该权限名称已被同级其他权限占用，新增失败");
        }

        //新增
        String permissionsId = StringUtil.getUUID();
        sysPermissionsEntity.setPermissionsId(permissionsId);
        sysPermissionsEntity.setCreateTime(LocalDateTime.now());
        sysPermissionsEntity.setUpdateUser(SysUserInfoUtil.getSysUserId(request));
        int addNum = sysPermissionsDao.insert(sysPermissionsEntity);
        if (addNum < 1){
            return ResultUtil.getErrorResultJsonBean(ResultJsonEunm.ERROR_FOR_DB_DML_ERROR, "添加权限信息失败");
        }

        return ResultUtil.getSuccessResultJsonBean(permissionsId);
    }


    //=============================================================角色-权限关联相关=====================================================================


    @Override
    public ResultJsonBean getUserPermissionsList(SysUserVo sysUserVo) {
        //先获取一级权限信息
        String userId = sysUserVo.getUserId();
        List<Map<String, Object>> userPermissionsList = sysPermissionsDao.getUserPermissionsListByUserIdAndParentId(userId, DEFAULT_PARENTID_FOR_FIRST);
        if (userPermissionsList.isEmpty()){
            return ResultUtil.getSuccessResultJsonBean(null);
        }
        //再以本身id为父id，递归获取子集权限信息
        getUnitChildPermissionsList(userPermissionsList, userId);
        return ResultUtil.getSuccessResultJsonBean(userPermissionsList);
    }

    /**
     *
     * 功能描述: 批量添加角色的权限
     *
     * @param: [sysRolePermissionsVo]
     * @return: cn.tang.common.utils.ResultJsonBean
     * @auther: tangpd
     * @date: 2020/12/2 18:23
     */
    @Override
    @Transactional
    public ResultJsonBean addRolePermissions(SysRolePermissionsVo sysRolePermissionsVo) {

        //1、先清除旧数据
        sysRolePermissionsDao.delete(new QueryWrapper<SysRolePermissionsEntity>().eq("role_id", sysRolePermissionsVo.getRoleId()));

        //2、再增加新数据
        SysRolePermissionsEntity sysRolePermissionsEntity = new SysRolePermissionsEntity();
        sysRolePermissionsEntity.setRoleId(sysRolePermissionsVo.getRoleId());
        List<String> permissionsIdList = sysRolePermissionsVo.getPermissionsIdList();
        AtomicInteger addNum = new AtomicInteger();
        permissionsIdList.forEach(
                (permissionsId) -> {
                    sysRolePermissionsEntity.setRolePermissionsId(StringUtil.getUUID());
                    sysRolePermissionsEntity.setPermissionsId(permissionsId);
                    try {
                        addNum.addAndGet(sysRolePermissionsDao.insert(sysRolePermissionsEntity));
                    } catch (Exception e) {
                        throw new ExceptionForMyCustom(ResultJsonEunm.ERROR_FOR_DB_DML_ERROR, "存在错误的角色数据或者权限数据（对应id的数据不存在），请检查之后重新添加");
                    }
                }
        );
        return ResultUtil.getSuccessResultJsonBean("成功添加了" + addNum + "条数据");
    }

    //============================================================================内部公用方法=====================================================================================

    //递归获取子集权限信息
    private void getUnitChildPermissionsList(List<Map<String, Object>> permissionsList, String userId){
        if (permissionsList.isEmpty()){
            return;
        }
        if (StringUtil.is_empty(userId)){
            permissionsList.forEach((permissionsUnit) -> {
                List<Map<String, Object>> childPermissionsList = sysPermissionsDao.getUserPermissionsListByParentId((String) permissionsUnit.get("permissionsId"));
                getUnitChildPermissionsList(childPermissionsList, userId);
                if (!childPermissionsList.isEmpty()){
                    permissionsUnit.put("childPermissionsList", childPermissionsList);
                }
            });
        }else {
            permissionsList.forEach((permissionsUnit) -> {
                List<Map<String, Object>> childPermissionsList = sysPermissionsDao.getUserPermissionsListByUserIdAndParentId(userId, (String) permissionsUnit.get("permissionsId"));
                getUnitChildPermissionsList(childPermissionsList, userId);
                if (!childPermissionsList.isEmpty()){
                    permissionsUnit.put("childPermissionsList", childPermissionsList);
                }
            });
        }
    }

}
