import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { TipoPregunta } from './tipo-pregunta.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class TipoPreguntaService {

    private resourceUrl = SERVER_API_URL + 'api/tipo-preguntas';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/tipo-preguntas';

    constructor(private http: Http) { }

    create(tipoPregunta: TipoPregunta): Observable<TipoPregunta> {
        const copy = this.convert(tipoPregunta);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(tipoPregunta: TipoPregunta): Observable<TipoPregunta> {
        const copy = this.convert(tipoPregunta);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<TipoPregunta> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            return res.json();
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
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convert(tipoPregunta: TipoPregunta): TipoPregunta {
        const copy: TipoPregunta = Object.assign({}, tipoPregunta);
        return copy;
    }
}
