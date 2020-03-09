import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Metodology } from './metodology.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Metodology>;

@Injectable()
export class MetodologyService {

    private resourceUrl =  SERVER_API_URL + 'api/metodologies';

    constructor(private http: HttpClient) { }

    create(metodology: Metodology): Observable<EntityResponseType> {
        const copy = this.convert(metodology);
        return this.http.post<Metodology>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(metodology: Metodology): Observable<EntityResponseType> {
        const copy = this.convert(metodology);
        return this.http.put<Metodology>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Metodology>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Metodology[]>> {
        const options = createRequestOption(req);
        return this.http.get<Metodology[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Metodology[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Metodology = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Metodology[]>): HttpResponse<Metodology[]> {
        const jsonResponse: Metodology[] = res.body;
        const body: Metodology[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Metodology.
     */
    private convertItemFromServer(metodology: Metodology): Metodology {
        const copy: Metodology = Object.assign({}, metodology);
        return copy;
    }

    /**
     * Convert a Metodology to a JSON which can be sent to the server.
     */
    private convert(metodology: Metodology): Metodology {
        const copy: Metodology = Object.assign({}, metodology);
        return copy;
    }
}
