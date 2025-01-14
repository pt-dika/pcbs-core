package dk.apps.pcps.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import dk.apps.pcps.model.iso8583.bni.ResponseCodeMessageEnum;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseData<T> {
    private String code;
    private String status;
    private String message;
    private T data;
    private Paginate paginate;
    private String location;

    public static ResponseData setResponse(String code, String status, String message, Object data, Paginate paginate) {
        ResponseData resp = new ResponseData();
        resp.setCode(code);
        resp.setStatus(status);
        resp.setMessage(message);
        resp.setData(data);
        resp.setPaginate(paginate);
        return resp;
    }

    public static ResponseData setResponse(ResponseCodeMessageEnum responseCodeMessageEnum, Object data) {
        String code = responseCodeMessageEnum.code;
        String status = responseCodeMessageEnum.name();
        String message = responseCodeMessageEnum.message;
        return setResponse("00"+code, status, message, data, null);
    }

    public static ResponseData setResponse(String code, String status, String message, Object data) {
        return setResponse(code, status, message, data, null);
    }
}
