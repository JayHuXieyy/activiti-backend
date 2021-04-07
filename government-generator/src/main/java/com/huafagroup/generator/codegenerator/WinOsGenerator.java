package com.huafagroup.generator.codegenerator;

import com.huafagroup.generator.CoreApplication;
import com.huafagroup.generator.codegenerator.core.Configure;
import com.huafagroup.generator.codegenerator.core.DataProcessor;
import com.huafagroup.generator.codegenerator.model.Table;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

@SuppressWarnings({"deprecation", "rawtypes", "unchecked"})
public class WinOsGenerator {
    private static String url = "jdbc:mysql://172.16.241.128:3306/call_system?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8";

    private static String driver = "com.mysql.jdbc.Driver";

    private static String user = "root";
    private static String password = "123456";

    private static Connection getConnection() throws ClassNotFoundException, SQLException {
        Connection connection;
        Properties props = new Properties();
        props.setProperty("user", user);
        props.setProperty("password", password);
        props.setProperty("remarks", "true"); //设置可以获取remarks信息
        props.setProperty("useInformationSchema", "true");//设置可以获取tables remarks信息

        Class.forName(driver);
        connection = DriverManager.getConnection(url, props);
		/*Connection connection;
		Class.forName(driver);
		connection = DriverManager.getConnection(url, user, password);*/
        return connection;
    }

    public static String getProjectPath() throws Exception {

        java.net.URL url = CoreApplication.class.getProtectionDomain().getCodeSource().getLocation();

        String filePath = java.net.URLDecoder.decode(url.getPath(), "utf-8");
        filePath = filePath.substring(1, filePath.length() - ("/target/classes/").length());

        System.out.println(filePath);
        return filePath;

    }

    public static void main(String[] args) throws Exception {
        Configure config = new Configure();
        //注意：一定要指定下划线
        String tableNamePattern = "news_information";
        String childPackage = "";
        //设置模块名
        int index = tableNamePattern.indexOf("_");
        if (index > 0) {
            childPackage = "." + tableNamePattern.substring(0, index);
        }
        config.setTargetDir(getProjectPath() + "/src/main/java/");
        config.setModelPackage("com.huafagroup.core.entity");
        config.setMapperPackage("com.huafagroup.core.dao");
        config.setXmlPackage("mapper/call"); //对应resources目录下的mapper,mybatis的xml文件
        config.setRestControllerPackage("com.huafagroup.restapi.controller");
        config.setControllerPackage("com.huafagroup.webapp.controller");
        config.setServicePackage("com.huafagroup.core.service");

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
                generator.generateServiceImpl(config.getTargetDir(), table);
                //restcontroller生成到restapi模块的controller目录
                String restControllerDir = config.getTargetDir().replace("/core/src", "/restapi/src");
                generator.generateRestAPI(restControllerDir, table);
				/*String controllerDir=config.getTargetDir().replace("/huafa-framework-core/src","/huafa-framework-console/src");
				generator.generateWebController(controllerDir,table);*/

                String xmlDir = config.getTargetDir().replace("main/java", "main/resources");
                generator.generateXml(xmlDir, table);
                //String pagetemplateDir=config.getTargetDir().replace("/huafa-framework-core/src/main/java","/huafa-framework-console/src/main/resources/templates");
                //generator.generatePages(pagetemplateDir,table);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
