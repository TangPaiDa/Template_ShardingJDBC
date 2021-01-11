package cn.tang.common.mybatisPlus;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class MybatisPlusCodeGenerator {

    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 1、全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "\\src\\main\\java");//生成文件的输出目录
        System.out.println("projectPath****************()" + projectPath);
        gc.setFileOverride(true);//是否覆盖已有文件,默认值：false
        gc.setBaseResultMap(true); //XML是否生成返回的 resultMap，与对象映射
        gc.setBaseColumnList(true); //XML是否生成通用 select 查询列字段
        gc.setOpen(false);//是否打开输出目录
        //gc.setDateType(DateType.ONLY_DATE);//使用 java.util.date 代替
        gc.setDateType(DateType.TIME_PACK);//使用 java 8 里面的 LocalDateTime 来映射数据的 datetime 类型字段
        gc.setEntityName("%sEntity");//实体命名方式
        gc.setMapperName("%sDao");//mapper 命名方式
        gc.setXmlName("%sMapper");//Mapper xml 命名方式
        gc.setServiceName("%sService");//service 命名方式
        gc.setServiceImplName("%sServiceImpl");//service impl 命名方式
        gc.setControllerName("%sController");//controller 命名方式
        gc.setAuthor("Tangpd");//开发人员
        mpg.setGlobalConfig(gc);

        // 2、数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");//驱动名称
        dsc.setUrl("jdbc:mysql://127.0.0.1:3306/sharding_jdbc?useUnicode=true&characterEncoding=utf-8&useSSL=false&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true&serverTimezone=GMT%2B8");//驱动连接的URL
        dsc.setUsername("user");//数据库连接用户名
        dsc.setPassword("123456");//数据库连接密码
        mpg.setDataSource(dsc);

        // 3、包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent("cn.tang");//父包名
        pc.setModuleName("userMode");//父包模块名
        pc.setController("controller");//Controller包名
        pc.setService("service");//Service包名
        pc.setServiceImpl("service.impl");//Service Impl包名
        pc.setMapper("dao");//dao 包名
        pc.setEntity("entity");//Entity包名
        pc.setXml("mapper");//Mapper xml 包名
        mpg.setPackageInfo(pc);

        // 自定义配置 mapper.xml 文件存放位置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() { }
        };
        List<FileOutConfig> focList = new ArrayList<>();
        focList.add(new FileOutConfig("/templates/mapper.xml.vm") {//如果模板引擎是 freemarker 则使用 "/templates/mapper.xml.ftl";
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义 mapper.xml 文件名称，是否加入模块包名称
                String mapperName = StringUtils.isEmpty(pc.getModuleName()) ? tableInfo.getEntityName() : pc.getModuleName() + "/" + tableInfo.getEntityName();
                return projectPath + "/src/main/resources/mapper/" + mapperName + "Mapper" + StringPool.DOT_XML;
            }
        });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);
        mpg.setTemplate(new TemplateConfig().setXml(null));

        // 4、策略配置
        StrategyConfig strategy = new StrategyConfig();
        /*
        记得生成代码之后，在 Entity 类中需要加入 @TableId(value = "对应id的字段名", type = IdType.INPUT) 告诉 mybatis plus 哪个是表的主键，如果有自增 id 主键就不需要
        看需求是否要在日期属性上增加注解
         */
        strategy.setInclude("course_1");// 设置要映射的表名;
//        strategy.setInclude("blog_tags","course","links","sys_settings","user_record","user_say"); // 设置要映射的表名
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(pc.getModuleName() + "_");
        mpg.setStrategy(strategy);

        mpg.execute();//执行代码生成器
    }
}
