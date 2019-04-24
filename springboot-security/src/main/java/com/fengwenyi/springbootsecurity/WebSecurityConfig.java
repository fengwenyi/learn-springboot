package com.fengwenyi.springbootsecurity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Erwin Feng
 * @since 2019-04-23 10:23
 */
//@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)//开启security的注解模式
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * @author lht
     * @doc   加载自定义获得用户信息（根据username）
     * @date 2018/6/1
     */
    @Bean
    UserDetailsService customUserService(){ //注册UserDetailsService 的bean
        return new CustomUserService();
    }


    /**
     * 配置认证规则
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //内存配置
        auth.inMemoryAuthentication()
                .withUser("zhangsan").password("123").roles("vip1")//xiaosu的角色是vip1
                .and()
                .withUser("lisi").password("123").roles("vip2")//小苏的角色是vip1
                .and()
                .withUser("wangwu").password("123").roles("vip3");//翛苏的角色是vip1
    }
    /*  如果不配置PasswordEncoder 会报如下错误
        java.lang.IllegalArgumentException: There is no PasswordEncoder mapped for the id "null"
    */
    @Bean
    public PasswordEncoder passwordEncoder() {
        //虽然已经过时，但这里不对密码进行加密
        return NoOpPasswordEncoder.getInstance();
    }

    /*@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserService()); //user Details Service验证

    }*/

    /**
     * 配置授权信息
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/").permitAll()//允许访问首页
                .antMatchers("/level1/*").hasRole("vip1")
                .antMatchers("/level2/*").hasRole("vip2")
                .antMatchers("/level3/*").hasRole("vip3");
//        自定义登录页面
        http.formLogin().loginPage("/userlogin")//这个/必须加上，配置登录url
                .usernameParameter("user")//指定进行认证的参数名
                .passwordParameter("pwd");

        http.logout().logoutSuccessUrl("/");//开启注销功能,并配置退出成功后重定向的的url
        http.rememberMe().rememberMeParameter("remeber");//开启记住我功能,默认会记住14天
    }

    protected void configure2(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated() //任何请求,登录后可以访问
                .and()
                .formLogin()
                .loginPage("/login")
                .failureUrl("/login?error")
                .permitAll() //登录页面用户任意访问
                .and()
                .logout().permitAll(); //注销行为任意访问

        // 范例
       /* http.authorizeRequests()
                //静态资源和一些所有人都能访问的请求
                .antMatchers("/css/**","/staic/**", "/js/**","/images/**").permitAll()
                .antMatchers("/", "/login","/session_expired").permitAll()
                //登录
                .and().formLogin()
                .loginPage("/login")
                .usernameParameter("userId")       //自己要使用的用户名字段
                .passwordParameter("password")     //密码字段
                .defaultSuccessUrl("/index")     //登陆成功后跳转的请求,要自己写一个controller转发
                .failureUrl("/loginAuthtictionFailed")  //验证失败后跳转的url
                //session管理
                .and().sessionManagement()
                .maximumSessions(1)                //系统中同一个账号的登陆数量限制
                .and().and()
                .logout()//登出
                .invalidateHttpSession(true)  //使session失效
                .clearAuthentication(true)    //清除证信息
                .and()
                .httpBasic();*/
    }

}
