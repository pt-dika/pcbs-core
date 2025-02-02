package dck.pcbs.settlementfiledownload.client;

import dck.pcbs.commons.model.core.FileUploadHost;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


@FeignClient(name = "pcbs-data-shared-client", configuration = PCBSDataSharedConfig.class, url = "${client.datasharedCore.baseUrl}")
public interface PCBSDataSharedClient {
    @GetMapping(value = "/file-upload-host/findByBatchGroupId/{batchGroupId}")
    FileUploadHost fuh_findByBatchGroupId(@PathVariable(name = "batchGroupId") int batchGroupId);

    @GetMapping(value = "/file-upload-host/findAllByActiveTrue")
    List<FileUploadHost> findAllByActiveTrue();
}
