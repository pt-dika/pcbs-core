package dk.apps.pcps.main.model.result.fileupload;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

@Data
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileBatch {
    private int batch;
    private String fileName;
    private String responseFileName;
    private String status;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+7")
    private Timestamp settleAt;
    @JsonIgnore
    private String strInvoiceNums;


    @JsonIgnore
    public String[] getInvoiceNums(){
        String iNums = this.getStrInvoiceNums();
        int iNumsLength = 6;
        int start = 0;
        int end = iNumsLength;
        int iNumsCount = iNums.length() / iNumsLength;
        String[] invoiceNums = new String[iNumsCount];
        for (int i = 0; i < iNumsCount; i++) {
            invoiceNums[i] = iNums.substring(start, end);
            start = end + 1;
            end = iNumsLength + start - 1;
        }
        return invoiceNums;
    }
}
