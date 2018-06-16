package co.project.client.data.remote

import co.project.client.data.model.Client
import co.project.client.data.model.Response
import co.project.client.data.model.UpdateParam
import io.reactivex.Completable
import io.reactivex.Observable
import retrofit2.http.*

public interface ServerService {
    @POST("/api/connect")       fun connect(): Observable<Client>
    @GET("/api/list")           fun list(): Observable<List<String>>
    @PUT("/api/update")         fun update(@Body param: UpdateParam): Observable<Response>
    @DELETE("/api/disconnect")  fun disconnect(): Completable
    @DELETE("/api/reset")       fun reset(): Completable
}