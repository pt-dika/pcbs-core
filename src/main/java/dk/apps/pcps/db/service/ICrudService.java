package dk.apps.pcps.db.service;



import dk.apps.pcps.model.ResponseData;

import java.util.List;

public interface ICrudService<T, R> {
    ResponseData<List<R>> getAllDataPage(int page, int limit, String sortBy);
    ResponseData<List<R>> getAllData(String sortBy);
    ResponseData<List<R>> getAllDataBy(T req);
    ResponseData<R> getData(Integer id);
    ResponseData<R> getData(String value);
    ResponseData<R> getData(Object... object);
    ResponseData<R> createData(T req);
    ResponseData<List<R>> createBatchData(List<T> reqs);
    ResponseData<R> updateData(T req);
    ResponseData<R> removeData(Integer id);
}
