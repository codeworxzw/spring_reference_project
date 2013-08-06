package spring.reference.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import spring.reference.exception.RecordNotFoundException;

@ControllerAdvice
public class WebExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebExceptionHandler.class);

    @ExceptionHandler(RecordNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public void handleRecordNotFoundException(Exception e, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        String errorMessage = enhanceErrorMessage("Record not found!", e);

        setResponse(httpServletResponse, errorMessage);
    }

    @ExceptionHandler(ClassNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public void handleClassNotFoundException(Exception e, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        setResponse(httpServletResponse, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public void handleOtherException(Exception e, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        setResponse(httpServletResponse, e.getMessage());
    }

    private String enhanceErrorMessage(String errorPrefix, Exception e) {
        return errorPrefix + "\n" + e.getMessage();
    }

    void setResponse(HttpServletResponse httpServletResponse, String errorMessage) {
        httpServletResponse.setContentType("text/html");

        try {
            PrintWriter writer = httpServletResponse.getWriter();
            writer.println(errorMessage);
        } catch (IOException e) {
            LOGGER.error("Error during writing error message through response.", e);
        } catch (IllegalStateException e) {
            LOGGER.error("Error during writing error message through response.", e);
        }
    }
}
