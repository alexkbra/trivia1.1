import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { Cuestionario } from './cuestionario.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class CuestionarioService {

    private resourceUrl = SERVER_API_URL + 'api/cuestionarios';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/cuestionarios';

    constructor(private http: Http) { }

    create(cuestionario: Cuestionario): Observable<Cuestionario> {
        const copy = this.convert(cuestionario);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(cuestionario: Cuestionario): Observable<Cuestionario> {
        const copy = this.convert(cuestionario);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<Cuestionario> {
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

    private convert(cuestionario: Cuestionario): Cuestionario {
        const copy: Cuestionario = Object.assign({}, cuestionario);
        return copy;
    }
}
