package com.repository.web.error;

import com.google.common.base.Throwables;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.MessageFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.repository.common.Constants.*;

@Controller
class CustomErrorController implements ErrorController {


    /**
     * Display an error page, as defined in web.xml <code>custom-error</code> element.
     */
    @RequestMapping(URL_ERROR_GENERAL)
    public String generalError(HttpServletRequest request, HttpServletResponse response, Model model) {
        // retrieve some useful information from the request
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
        // String servletName = (String) request.getAttribute("javax.servlet.error.servlet_name");
        String exceptionMessage = getExceptionMessage(throwable, statusCode);

        String requestUri = (String) request.getAttribute("javax.servlet.error.request_uri");
        if (requestUri == null) {
            requestUri = "Unknown";
        }


        String message = MessageFormat.format("{0} returned for {1} with message {2}",
                statusCode, requestUri, exceptionMessage
        );

        model.addAttribute("errorMessage", message);
        return HTML_ERROR_GENERAL;
    }


    private String getExceptionMessage(Throwable throwable, Integer statusCode) {
        if (throwable != null) {
            return Throwables.getRootCause(throwable).getMessage();
        }
        HttpStatus httpStatus = HttpStatus.valueOf(statusCode);
        return httpStatus.getReasonPhrase();
    }


    @RequestMapping(value = HTML_ERROR_404)
    public String handleError() {
        return "tiles/error/404";
    }

    //??????404
    @Override
    public String getErrorPath() {
        // TODO Auto-generated method stub
        return HTML_ERROR_404;
    }
}
