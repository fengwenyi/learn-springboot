package com.fengwenyi.springbooterrorpage;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Wenyi Feng
 * @since 2019-02-26
 */
@Controller
//@RequestMapping("/error")
//@EnableConfigurationProperties({ServerProperties.class})
public class ErrorPageController implements ErrorController {

    @RequestMapping(value = "/error", produces = "text/html")
    public ModelAndView handleError(HttpServletRequest request,
                                    HttpServletResponse response) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                //return "error-404";
                return new ModelAndView("404");
            }

            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return new ModelAndView("500");
            }
        }
        return new ModelAndView("error");
    }

    @RequestMapping(value = "/error")
    @ResponseBody
    public String handleError1(HttpServletRequest request,
                                    HttpServletResponse response) {
        return "出错(json)";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
