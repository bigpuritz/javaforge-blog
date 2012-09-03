package net.javaforge.blog.dbconf.beans;

import org.springframework.beans.factory.annotation.Value;

import javax.inject.Named;

@Named("appInfo")
public class AppInfoBean {

    @Value("${app.name}")
    private String appName;

    @Value("${app.version}")
    private String appVersion;

    public String getAppName() {
        return this.appName;
    }

    public String getAppVersion() {
        return this.appVersion;
    }

}
