

package net.jjjerp.generator;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import net.jjjerp.generator.config.GeneratorStrategy;
import net.jjjerp.generator.constant.GeneratorConstant;
import net.jjjerp.generator.properties.GeneratorProperties;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * spring-boot-jjj代码生成器入口类
 **/
@Component
public class SpringBootJjjGenerator {

    /**
     * 生成代码
     * @param args
     */
    public static void main(String[] args) {
        GeneratorProperties generatorProperties = new GeneratorProperties();

        // 设置基本信息
        generatorProperties
                .setMavenModuleName("jjj-common")
                .setParentPackage("net.jjjerp.common")
                //.setModuleName("version")
                .setAuthor("jjjerp")
                .setFileOverride(true);

        // 设置表信息
        generatorProperties.addTable("jjjerp_app_wx_file","id");
        // 设置表前缀
        generatorProperties.setTablePrefix(Arrays.asList("jjjerp_"));

        // 数据源配置
        generatorProperties.getDataSourceConfig()
                .setDbType(DbType.MYSQL)
                .setUsername("root")
                .setPassword("root")
                .setDriverName("com.mysql.jdbc.Driver")
                .setUrl("jdbc:mysql://192.168.151.129:3306/jjj_erp?useUnicode=true&characterEncoding=UTF-8&useSSL=false");

        // 生成配置
        generatorProperties.getGeneratorConfig()
                .setGeneratorStrategy(GeneratorStrategy.SIMPLE)
                .setGeneratorEntity(true)
                .setGeneratorController(false)
                .setGeneratorService(true)
                .setGeneratorServiceImpl(true)
                .setGeneratorMapper(true)
                .setGeneratorMapperXml(true)
                .setGeneratorPageParam(false)
                .setGeneratorQueryVo(false)
                .setRequiresPermissions(false)
                .setPageListOrder(false)
                .setParamValidation(false)
                .setSwaggerTags(true)
                .setOperationLog(true);

        // 全局配置
        generatorProperties.getMybatisPlusGeneratorConfig().getGlobalConfig()
                .setOpen(true)
                .setSwagger2(true)
                .setIdType(IdType.AUTO)
                .setDateType(DateType.ONLY_DATE);

        // 策略配置
        generatorProperties.getMybatisPlusGeneratorConfig().getStrategyConfig()
                .setNaming(NamingStrategy.underline_to_camel)
                .setColumnNaming(NamingStrategy.underline_to_camel)
                .setEntityLombokModel(true)
                .setRestControllerStyle(true)
                .setControllerMappingHyphenStyle(true)
                .setVersionFieldName(GeneratorConstant.VERSION)
                .setLogicDeleteFieldName(GeneratorConstant.DELETED);

        // 生成代码
        CodeGenerator codeGenerator = new CodeGenerator();
        codeGenerator.generator(generatorProperties);

    }


}
