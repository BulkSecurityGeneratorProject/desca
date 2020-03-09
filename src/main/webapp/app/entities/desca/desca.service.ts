import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Desca } from './desca.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Desca>;

@Injectable()
export class DescaService {

    private resourceUrl =  SERVER_API_URL + 'api/descas';

    constructor(private http: HttpClient) { }

    create(desca: Desca): Observable<EntityResponseType> {
        const copy = this.convert(desca);
        return this.http.post<Desca>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(desca: Desca): Observable<EntityResponseType> {
        const copy = this.convert(desca);
        return this.http.put<Desca>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Desca>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Desca[]>> {
        const options = createRequestOption(req);
        return this.http.get<Desca[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Desca[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Desca = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Desca[]>): HttpResponse<Desca[]> {
        const jsonResponse: Desca[] = res.body;
        const body: Desca[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Desca.
     */
    private convertItemFromServer(desca: Desca): Desca {
        const copy: Desca = Object.assign({}, desca);
        return copy;
    }

    /**
     * Convert a Desca to a JSON which can be sent to the server.
     */
    private convert(desca: Desca): Desca {
        const copy: Desca = Object.assign({}, desca);
        return copy;
    }
}
