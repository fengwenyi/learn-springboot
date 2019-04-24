package com.fengwenyi.springbootsecurity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Erwin Feng
 * @since 2019-04-23 10:39
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)//开启security的注解模式
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    /**
     * 配置user-detail服务
     *
     * 这里需要注意的是，roles()方法是authorities()方法的简写形式。
     * roles()方法所给定的值都会加一个"ROLE_"前缀，并将其作为权限授予用户。
     * 实际上，如下用户配置与上面程序是一样的。
     *
     * @param auth
     * @throws Exception
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth)throws Exception{
        //基于内存的用户存储、认证
        auth.inMemoryAuthentication()
                .withUser("admin").password("admin").authorities("ADMIN","USER")
                .and()
                .withUser("user").password("user").authorities("USER");
    }

    /*  如果不配置PasswordEncoder 会报如下错误
        java.lang.IllegalArgumentException: There is no PasswordEncoder mapped for the id "null"
    */
    @Bean
    public PasswordEncoder passwordEncoder() {
        //虽然已经过时，但这里不对密码进行加密
        return NoOpPasswordEncoder.getInstance();
    }

    /**
     * 拦截请求
     * @param http
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http)throws Exception{
        http.authorizeRequests()
                .antMatchers("/","/css/**","/js/**").permitAll()   //任何人都可以访问
                .antMatchers("/admin/**").access("hasRole('ADMIN')")     //持有user权限的用户可以访问
                .antMatchers("/user/**").hasAuthority("USER")
                .and()
                .formLogin()
                .loginPage("/login")
                .failureUrl("/login?error")
        ;
    }

}
