package cn.tang.common.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * @Auther: tangpd
 * @Date: 2019/10/30 15:10
 * @Description:
 */
@Slf4j
public class ReflectionUtil {


    /**
     * 传入一个参数对象，然后根据需要验证的参数名称数组进行检查，若有空参数，则返回参数名
     * @param paramVo
     * @param checkName
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static String checkParameterHaveNull(Object paramVo, String[] checkName) throws NoSuchFieldException, IllegalAccessException {
        StringBuffer haveNullParamBuffer = new StringBuffer();
        String haveNullParam = null;
        for (String param : checkName) {
            Field field = paramVo.getClass().getDeclaredField(param);
            field.setAccessible(true);
            if (String.class.equals(field.getType()) && StringUtil.is_empty((String)field.get(paramVo))){
                haveNullParamBuffer.append(param).append(";");
            }
            if (List.class.equals(field.getType()) && ((List)field.get(paramVo)).isEmpty()){
                haveNullParamBuffer.append(param).append(";");
            }
            if (Map.class.equals(field.getType()) && ((Map)field.get(paramVo)).isEmpty()){
                haveNullParamBuffer.append(param).append(";");
            }
            if (JSONObject.class.equals(field.getType()) && ((JSONObject)field.get(paramVo)).isEmpty()){
                haveNullParamBuffer.append(param).append(";");
            }
            if (null == field.get(paramVo)){
                haveNullParamBuffer.append(param).append(";");
            }
        }
        haveNullParam = haveNullParamBuffer.toString();
        if (!StringUtil.is_empty(haveNullParam)){
            throw new ExceptionForMyCustom(ResultJsonEunm.ERROR_FOR_PARAM_ISNULL, haveNullParam + "参数为空，请检查参数");
        }
        return haveNullParam;
    }
    
    public static String checkParameterHaveNullNotThrow(Object paramVo, String[] checkName) throws NoSuchFieldException, IllegalAccessException {
        StringBuffer haveNullParamBuffer = new StringBuffer();
        for (String param : checkName) {
            Field field = paramVo.getClass().getDeclaredField(param);
            field.setAccessible(true);

            if (String.class.equals(field.getType()) && StringUtil.is_empty((String)field.get(paramVo))){
                haveNullParamBuffer.append(param).append(";");
            }
            if (List.class.equals(field.getType()) && ((List)field.get(paramVo)).isEmpty()){
                haveNullParamBuffer.append(param).append(";");
            }
            if (Map.class.equals(field.getType()) && ((Map)field.get(paramVo)).isEmpty()){
                haveNullParamBuffer.append(param).append(";");
            }
            if (JSONObject.class.equals(field.getType()) && ((JSONObject)field.get(paramVo)).isEmpty()){
                haveNullParamBuffer.append(param).append(";");
            }
            if (null == field.get(paramVo)){
                haveNullParamBuffer.append(param).append(";");
            }
        }
        return haveNullParamBuffer.toString();
    }

    /**
     * 传入两个拥有相同属性名并相同数据类型的对象，将一个对象中的属性值，赋值给另一个对象中相同属性名的属性；
     * @param objectForHaveValue    有值的对象
     * @param objectForResult       被赋值的对象（返回的对象）
     * @return
     */
    public static Object assignmentObjectByObject (Object objectForHaveValue, Object objectForResult) {
        try {
            Field[]  resultFields = objectForResult.getClass().getDeclaredFields();
            Field[]  valueFields = objectForHaveValue.getClass().getDeclaredFields();
            for (Field valuefield : valueFields) {
                valuefield.setAccessible(true);
                for (Field resultfield : resultFields) {
                    resultfield.setAccessible(true);
                    if (valuefield.getName().equals(resultfield.getName()) && null != valuefield.get(objectForHaveValue)){
                        resultfield.set(objectForResult, valuefield.get(objectForHaveValue));
                    }
                }
            }
        } catch (IllegalAccessException e) {
            //上面已经解开了属性访问权限，所以一般不会抛出这个异常；这里就把异常捕获就可以了
            e.printStackTrace();
            log.error("对象赋值出错-->(){}", e.getMessage());
        }
        return objectForResult;
    }
}
