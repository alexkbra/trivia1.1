import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Nivel } from './nivel.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class NivelService {

    private resourceUrl = SERVER_API_URL + 'api/nivels';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/nivels';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(nivel: Nivel): Observable<Nivel> {
        const copy = this.convert(nivel);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(nivel: Nivel): Observable<Nivel> {
        const copy = this.convert(nivel);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<Nivel> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        for (let i = 0; i < jsonResponse.length; i++) {
            this.convertItemFromServer(jsonResponse[i]);
        }
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convertItemFromServer(entity: any) {
        entity.fechaInicio = this.dateUtils
            .convertLocalDateFromServer(entity.fechaInicio);
        entity.fechaFinal = this.dateUtils
            .convertLocalDateFromServer(entity.fechaFinal);
    }

    private convert(nivel: Nivel): Nivel {
        const copy: Nivel = Object.assign({}, nivel);
        copy.fechaInicio = this.dateUtils
            .convertLocalDateToServer(nivel.fechaInicio);
        copy.fechaFinal = this.dateUtils
            .convertLocalDateToServer(nivel.fechaFinal);
        return copy;
    }
}
