package org.finance;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jiangbangfa
 */
public class MybatisGeneratorMain {

    public final static String projectPath = "/home/jbf/workspace/my/海南岛/finance/finance-backend";

    public static void main(String[] args) {
        Map<OutputFile, String> map = new HashMap<>();
        map.put(OutputFile.mapperXml, projectPath + "/src/main/resources/mapper");
        FastAutoGenerator.create("jdbc:mysql://localhost:3307/hx_saas?createDatabaseIfNotExist=true&useUnicode=true&characterEncoding=utf-8", "root", "root1997")
                .globalConfig(builder -> {
                    builder.author("jiangbangfa") // 设置作者
                            //.enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir(projectPath); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("org.finance.domain") // 设置父包名
                            .moduleName("system") // 设置父包模块名
                            .pathInfo(map); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("*"); // 设置需要生成的表名
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }

}
