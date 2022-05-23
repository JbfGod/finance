package org.finance;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.TemplateType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jiangbangfa
 */
public class MybatisGeneratorMain {

    public static void main(String[] args) {
        String projectPath = "/home/jbf/workspace/my/海南岛/finance/src/main";
        FreemarkerTemplateEngine freemarkerTemplateEngine = new FreemarkerTemplateEngine();
        Map<OutputFile, String> map = new HashMap<>(100);
        map.put(OutputFile.xml, String.format("%s/resources/mapper", projectPath));
        FastAutoGenerator.create("jdbc:mysql://localhost:3307/finance?createDatabaseIfNotExist=true&useUnicode=true&characterEncoding=utf-8", "root", "root1997")
                .globalConfig(builder -> {
                    builder.author("jiangbangfa").outputDir(String.format("%s/java", projectPath));
                })
                .packageConfig(builder -> {
                    builder.parent("org.finance.business").serviceImpl("service").controller("web")
                            .pathInfo(map);
                })
                .strategyConfig(builder -> {
                    builder.controllerBuilder().enableRestStyle().formatFileName("%sWeb")
                        .serviceBuilder().formatServiceImplFileName("%sService");
                    builder.entityBuilder()
                            .enableLombok()
                            .enableChainModel()
                            .addTableFills(Arrays.asList(
                            new Column("create_by", FieldFill.INSERT),
                            new Column("create_time", FieldFill.INSERT),
                            new Column("creator_name", FieldFill.INSERT),
                            new Column("modify_by", FieldFill.INSERT_UPDATE),
                            new Column("modify_name", FieldFill.INSERT_UPDATE),
                            new Column("modify_time", FieldFill.INSERT_UPDATE),
                            new Column("customer_id", FieldFill.INSERT)
                    )).logicDeleteColumnName("deleted");
                    builder.addExclude(
                        "flyway_schema_history",
                            "act%"
                    );
                })
                .templateConfig(builder -> {
                    builder.disable(TemplateType.SERVICE);
                    builder.serviceImpl("templates/serviceImpl.java");
                })
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }

}
