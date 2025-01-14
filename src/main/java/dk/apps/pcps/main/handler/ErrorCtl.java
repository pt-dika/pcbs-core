package dk.apps.pcps.main.handler;

import com.fasterxml.jackson.core.JsonParseException;
import dk.apps.pcps.commonutils.ISOConverters;
import dk.apps.pcps.main.module.tapcash.model.enums.ResponseCodeMessageEnum;
import dk.apps.pcps.model.ResponseData;
import dk.apps.pcps.main.model.enums.ProcessMessageEnum;
import lombok.Data;
import org.jpos.iso.ISOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.Map;

@RestController
@RestControllerAdvice
public class ErrorCtl implements ErrorController {

    private static final String PATH = "/error";

    ErrorAttributes errorAttributes;

    @Autowired
    public ErrorCtl(ErrorAttributes errorAttributes){
        this.errorAttributes = errorAttributes;
    }

    @ExceptionHandler(ApplicationException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseData applicationException(ApplicationException ex, WebRequest request) {
        ProcessMessageEnum error = ProcessMessageEnum.valueOf(ex.getMessage());
        String messages = error.message;
        if (!ex.getMessages().equalsIgnoreCase(""))
            messages += " "+ex.getMessages();
        return new ResponseData()
                .setCode(error.code)
                .setStatus(error.name())
                .setMessage(messages);
    }

    @ExceptionHandler({ValidationException.class, ConstraintViolationException.class,
            JsonParseException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseData validateHandler(RuntimeException runtimeException){
        return new ResponseData()
                .setCode("0400")
                .setStatus(HttpStatus.BAD_REQUEST.name())
                .setMessage(runtimeException.getMessage());
    }

    @ExceptionHandler({ResponseStatusException.class, ISOException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ResponseData globalHandler(ResponseStatusException responseStatusException){
        return new ResponseData()
                .setCode("0"+ HttpStatus.INTERNAL_SERVER_ERROR.value())
                .setStatus(responseStatusException.getStatus().name())
                .setMessage(responseStatusException.getReason());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus()
    ResponseData globalHandler(HttpRequestMethodNotSupportedException ex){
        return new ResponseData()
                .setCode("0"+ HttpStatus.METHOD_NOT_ALLOWED.value())
                .setStatus(HttpStatus.METHOD_NOT_ALLOWED.name())
                .setMessage(ex.getMessage());
    }

    @ExceptionHandler(MessageException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseData messageException(MessageException ex, WebRequest request) {
        ResponseCodeMessageEnum error = ResponseCodeMessageEnum.getByCode(ex.getMessage());
        String messages = error.message;
        if (!ex.getMessages().equalsIgnoreCase(""))
            messages += " "+ex.getMessages();
        return new ResponseData()
                .setCode(ISOConverters.padLeftZeros(error.code, 4))
                .setStatus(error.name())
                .setMessage(messages);
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    ResponseData unauthorized(UnauthorizedException unauthorizedException){
        return new ResponseData()
                .setCode("0"+ HttpStatus.UNAUTHORIZED.value())
                .setStatus(HttpStatus.UNAUTHORIZED.name())
                .setMessage(unauthorizedException.getMessage());
    }

    @RequestMapping(value = PATH)
    ResponseData errorHandler(HttpServletRequest request, HttpServletResponse response) {
        ErrorJson err = new ErrorJson(response.getStatus(), getErrorAttributes(request, true));
        return new ResponseData()
                .setCode("0"+ err.getCode())
                .setStatus(err.getError())
                .setMessage(err.getTrace());
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }

    private Map<String, Object> getErrorAttributes(HttpServletRequest request, boolean includeStackTrace) {
        ServletWebRequest requestAttributes = new ServletWebRequest(request);
        return errorAttributes.getErrorAttributes(requestAttributes, ErrorAttributeOptions.of(ErrorAttributeOptions.Include.STACK_TRACE));
    }

    @Data
    public class ErrorJson {

        private Integer code;
        private String error;
        private String message;
        private String timeStamp;
        private String trace;

        public ErrorJson(int code, Map<String, Object> errorAttributes) {
            this.code = code;
            this.error = (String) errorAttributes.get("error");
            this.message = (String) errorAttributes.get("message");
            this.timeStamp = errorAttributes.get("timestamp").toString();
            this.trace = (String) errorAttributes.get("trace");
        }
    }
}
