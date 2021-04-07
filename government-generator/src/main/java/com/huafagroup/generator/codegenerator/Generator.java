package com.huafagroup.generator.codegenerator;

import com.huafagroup.generator.codegenerator.core.Configure;
import com.huafagroup.generator.codegenerator.model.Table;
import com.huafagroup.generator.codegenerator.utils.BeanUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@SuppressWarnings({"deprecation", "rawtypes", "unchecked"})
public class Generator {


    protected Configure config;

    public Generator() {
        config = new Configure();
    }

    public Generator(Configure config) {
        this.config = config;
    }

    public Writer createWriter(String targetDir, String path) {
        path = targetDir + path;
        File file = new File(path);
        String dir = file.getParent();
        File pd = new File(dir);
        if (!pd.exists()) {
            pd.mkdirs();
        }
        FileWriter writer = null;
        try {
            writer = new FileWriter(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // StringWriter writer=new StringWriter();
        return writer;
    }

    private VelocityEngine createVelocityEngine() {

        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(Velocity.ENCODING_DEFAULT, "UTF-8");
        ve.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
        ve.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, config.getTargetDir() + "template/");
        ve.init();
        return ve;
    }

    public void generateModel(String targetDir, Table table) {
        VelocityEngine velocityEngine = createVelocityEngine();
        VelocityContext context = createContext(table);
        Writer writer = createWriter(targetDir, config.getModelPackage().replace(".", "/")
                + "/" + table.getBeanName() + ".java");

        Template t = velocityEngine.getTemplate("beanTemplate.vm");

        t.merge(context, writer);

        flushWriter(writer);


    }


    public void generatePages(String targetDir, Table table) {
        generateIndexPage(targetDir, table);
        generateAddPage(targetDir, table);
        generateEditPage(targetDir, table);
    }

    public void generateIndexPage(String targetDir, Table table) {
        VelocityEngine velocityEngine = createVelocityEngine();
        VelocityContext context = createContext(table);
        String path = "";
        if (table.isModulePrefix()) {
            path = "/" + table.getModule() + "/" + table.getLowerBeanName() + "/index.ftl";
        } else {
            path = "/" + table.getLowerBeanName() + "/index.ftl";
        }
        Writer writer = createWriter(targetDir, path);
        Template t = velocityEngine.getTemplate("indexpageTemplate.vm");
        t.merge(context, writer);
        flushWriter(writer);
    }


    public void generateAddPage(String targetDir, Table table) {
        VelocityEngine velocityEngine = createVelocityEngine();
        VelocityContext context = createContext(table);
        String path = "";
        if (table.isModulePrefix()) {
            path = "/" + table.getModule() + "/" + table.getLowerBeanName() + "/add.ftl";
        } else {
            path = "/" + table.getLowerBeanName() + "/add.ftl";
        }
        Writer writer = createWriter(targetDir, path);
        Template t = velocityEngine.getTemplate("addpageTemplate.vm");
        t.merge(context, writer);
        flushWriter(writer);
    }

    public void generateEditPage(String targetDir, Table table) {
        VelocityEngine velocityEngine = createVelocityEngine();
        VelocityContext context = createContext(table);
        String path = "";
        if (table.isModulePrefix()) {
            path = "/" + table.getModule() + "/" + table.getLowerBeanName() + "/edit.ftl";
        } else {
            path = "/" + table.getLowerBeanName() + "/edit.ftl";
        }
        Writer writer = createWriter(targetDir, path);
        Template t = velocityEngine.getTemplate("editpageTemplate.vm");
        t.merge(context, writer);
        flushWriter(writer);
    }

    private void flushWriter(Writer writer) {
        try {
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void generateMapper(String targetDir, Table table) {
        VelocityEngine velocityEngine = createVelocityEngine();
        VelocityContext context = createContext(table);
        Writer writer = createWriter(targetDir, config.getMapperPackage().replace(".", "/")
                + "/" + table.getBeanName() + "Mapper.java");
        Template t = velocityEngine.getTemplate("mapperTemplate.vm");
        t.merge(context, writer);
        flushWriter(writer);
    }

    public void generateService(String targetDir, Table table) {
        VelocityEngine velocityEngine = createVelocityEngine();
        VelocityContext context = createContext(table);
        Writer writer = createWriter(targetDir, config.getServicePackage().replace(".", "/")
                + "/" + table.getBeanName() + "Service.java");
        Template t = velocityEngine.getTemplate("serviceTemplate.vm");
        t.merge(context, writer);
        flushWriter(writer);
    }

    public void generateServiceImpl(String targetDir, Table table) {
        VelocityEngine velocityEngine = createVelocityEngine();
        VelocityContext context = createContext(table);
        Writer writer = createWriter(targetDir, config.getServicePackage().replace(".", "/")
                + "/impl/" + table.getBeanName() + "ServiceImpl.java");
        Template t = velocityEngine.getTemplate("serviceImplTemplate.vm");
        t.merge(context, writer);
        flushWriter(writer);
    }

    public void generateRestAPI(String targetDir, Table table) {
        VelocityEngine velocityEngine = createVelocityEngine();
        VelocityContext context = createContext(table);
        Writer writer = createWriter(targetDir, config.getRestControllerPackage().replace(".", "/")
                + "/" + table.getBeanName() + "Controller.java");
        Template t = velocityEngine.getTemplate("restControllerTemplate.vm");
        t.merge(context, writer);
        flushWriter(writer);
    }

    public void generateWebController(String targetDir, Table table) {

        VelocityEngine velocityEngine = createVelocityEngine();
        VelocityContext context = createContext(table);
        Writer writer = createWriter(targetDir, config.getControllerPackage().replace(".", "/")
                + "/" + table.getBeanName() + "Controller.java");
        Template t = velocityEngine.getTemplate("controllerTemplate.vm");
        t.merge(context, writer);
        flushWriter(writer);
    }

    public void generateXml(String targetDir, Table table) {

        VelocityEngine velocityEngine = createVelocityEngine();
        VelocityContext context = createContext(table);
        Writer writer = createWriter(targetDir, config.getXmlPackage()
                .replace(".", "/") + "/" + table.getBeanName() + "Mapper.xml");
        Template t = velocityEngine.getTemplate("xmlTemplate.vm");

        t.merge(context, writer);

        flushWriter(writer);
    }


    private VelocityContext createContext(Table table) {
        Map map = BeanUtils.getValueMap(table);
        Map configMap = BeanUtils.getValueMap(config);
        configMap.put("author", System.getProperty("user.name"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        configMap.put("dateTime", sdf.format(new Date()));
        map.putAll(configMap);
        VelocityContext context = new VelocityContext(map);

        return context;
    }


}
