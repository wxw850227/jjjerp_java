

package net.jjjerp.generator.test;

import net.jjjerp.generator.CodeGenerator;
import net.jjjerp.generator.properties.GeneratorProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 生成代码配置文件读取测试
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GeneratorTestApplication.class)
public class GeneratorPropertiesTest {

    @Autowired
    private GeneratorProperties generatorProperties;

    @Autowired
    private CodeGenerator codeGenerator;

    @Test
    public void test(){
//        codeGenerator.generator(generatorProperties);
    }

}
