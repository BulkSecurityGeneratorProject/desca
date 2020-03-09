import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Methodology } from './methodology.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Methodology>;

@Injectable()
export class MethodologyService {

    private resourceUrl =  SERVER_API_URL + 'api/methodologies';

    constructor(private http: HttpClient) { }

    create(methodology: Methodology): Observable<EntityResponseType> {
        const copy = this.convert(methodology);
        return this.http.post<Methodology>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(methodology: Methodology): Observable<EntityResponseType> {
        const copy = this.convert(methodology);
        return this.http.put<Methodology>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Methodology>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Methodology[]>> {
        const options = createRequestOption(req);
        return this.http.get<Methodology[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Methodology[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Methodology = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Methodology[]>): HttpResponse<Methodology[]> {
        const jsonResponse: Methodology[] = res.body;
        const body: Methodology[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Methodology.
     */
    private convertItemFromServer(methodology: Methodology): Methodology {
        const copy: Methodology = Object.assign({}, methodology);
        return copy;
    }

    /**
     * Convert a Methodology to a JSON which can be sent to the server.
     */
    private convert(methodology: Methodology): Methodology {
        const copy: Methodology = Object.assign({}, methodology);
        return copy;
    }
}
