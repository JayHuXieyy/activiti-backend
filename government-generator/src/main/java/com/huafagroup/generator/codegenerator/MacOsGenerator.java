package com.huafagroup.generator.codegenerator;

import com.huafagroup.generator.CoreApplication;
import com.huafagroup.generator.codegenerator.core.Configure;
import com.huafagroup.generator.codegenerator.core.DataProcessor;
import com.huafagroup.generator.codegenerator.model.Table;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

@SuppressWarnings({"deprecation", "rawtypes", "unchecked"})
public class MacOsGenerator {
    private static String url = "jdbc:mysql://192.168.0.241:3306/property_rights?useUnicode=true&characterEncoding=utf-8&useSSL=false&statementInterceptors=brave.mysql.TracingStatementInterceptor";

    private static String driver = "com.mysql.jdbc.Driver";

    private static String user = "root";
    private static String password = "root";

    private static Connection getConnection() throws ClassNotFoundException, SQLException {
        Connection connection;
        Class.forName(driver);
        connection = DriverManager.getConnection(url, user, password);
        return connection;
    }

    public static String getProjectPath() throws Exception {

        java.net.URL url = CoreApplication.class.getProtectionDomain().getCodeSource().getLocation();

        String filePath = java.net.URLDecoder.decode(url.getPath(), "utf-8");
        filePath = filePath.substring(1, filePath.length() - ("/target/classes/").length());

        System.out.println("/" + filePath);
        return "/" + filePath;

    }

    public static void main(String[] args) throws Exception {
        Configure config = new Configure();

        String tableNamePattern = "message";
        String childPackage = "";
        //设置模块名
        int index = tableNamePattern.indexOf("_");
        if (index > 0) {
            childPackage = "." + tableNamePattern.substring(0, index);
        }
        config.setTargetDir(getProjectPath() + "/src/main/java/");
        config.setModelPackage("com.huafagroup.core.entity" + childPackage);
        config.setMapperPackage("com.huafagroup.core.dao" + childPackage);
        config.setXmlPackage("mapper" + childPackage); //对应resources目录下的mapper,mybatis的xml文件
        config.setRestControllerPackage("com.huafagroup.restapi.controller" + childPackage);
        config.setControllerPackage("com.huafagroup.webapp.controller" + childPackage);
        config.setServicePackage("com.huafagroup.core.service" + childPackage);

        Generator generator = new Generator(config);


        Connection connection = getConnection();
        DataProcessor t = new DataProcessor(connection);
        List<Table> tableInfos = t.getTableInfos(tableNamePattern);
        //List<DictItem> dictitemList= t.getDictItem("");
        connection.close();
        try {
            for (Table table : tableInfos) {

                generator.generateModel(config.getTargetDir(), table);

                generator.generateMapper(config.getTargetDir(), table);

                generator.generateService(config.getTargetDir(), table);
                //restcontroller生成到restapi模块的controller目录
                String restControllerDir = config.getTargetDir().replace("/core/src", "/restapi/src");
                generator.generateRestAPI(restControllerDir, table);
//				String controllerDir=config.getTargetDir().replace("/core/src","/webapp/src");
//				generator.generateWebController(controllerDir,table);

                String xmlDir = config.getTargetDir().replace("main/java", "main/resources");
                generator.generateXml(xmlDir, table);
//				String pagetemplateDir=config.getTargetDir().replace("/core/src/main/java","/webapp/src/main/resources/templates");
//				generator.generatePages(pagetemplateDir,table);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
