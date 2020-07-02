import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { AuthService } from './auth.service';

@Injectable({
    providedIn: 'root',
})
export class ProductoService {
    constructor(private httpClient: HttpClient) {}

    async listarDetalleProducto(id:number):Promise<any> {
        return await this.httpClient
            .get(`${environment.url_api}/productos/${id}`,AuthService.getHeaderWithAuthorization())
            .toPromise();
    }


}