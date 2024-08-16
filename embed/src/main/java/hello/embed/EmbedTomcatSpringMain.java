package hello.embed;

import hello.servlet.HelloServlet;
import hello.spring.HelloConfig;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.File;

public class EmbedTomcatSpringMain {
    public static void main(String[] args) throws LifecycleException {
        System.out.println("EmbedTomcatSpringMain.main");

        // 톰캣 설정
        Tomcat tomcat = new Tomcat();
        Connector connector = new Connector();
        connector.setPort(8080);
        tomcat.setConnector(connector);

        // TODO - 컨텍스트 등록
        Context context = tomcat.addContext("", "/");

        // TODO - 톰켓에 webapps 생성  ----- START -----
        File docBaseFile = new File(context.getDocBase());

        if (!docBaseFile.isAbsolute()) {
            org.apache.catalina.Host parent = (org.apache.catalina.Host) context.getParent();
            docBaseFile = new File(parent.getAppBaseFile(), docBaseFile.getPath());
        }

        docBaseFile.mkdirs();
        // TODO - 생성 종료 ----- END -----

        // TODO - 스프링 컨테이너 생성
        AnnotationConfigWebApplicationContext springContext = new AnnotationConfigWebApplicationContext();
        springContext.register(HelloConfig.class);

        // TODO - 스프링 MVC 디시프처 서블릿 생성, 스프링 컨테이너 연결
        DispatcherServlet dispatcherServlet = new DispatcherServlet(springContext);

        // TODO - 디스패처 서블릿 등록
        tomcat.addServlet("", "springServlet", dispatcherServlet);
        // TODO - 서블릿 연결
        context.addServletMappingDecoded("/", "springServlet");
        tomcat.start();
    }
}
