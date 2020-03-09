import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { InternationalStandard } from './international-standard.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<InternationalStandard>;

@Injectable()
export class InternationalStandardService {

    private resourceUrl =  SERVER_API_URL + 'api/international-standards';

    constructor(private http: HttpClient) { }

    create(internationalStandard: InternationalStandard): Observable<EntityResponseType> {
        const copy = this.convert(internationalStandard);
        return this.http.post<InternationalStandard>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(internationalStandard: InternationalStandard): Observable<EntityResponseType> {
        const copy = this.convert(internationalStandard);
        return this.http.put<InternationalStandard>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<InternationalStandard>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<InternationalStandard[]>> {
        const options = createRequestOption(req);
        return this.http.get<InternationalStandard[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<InternationalStandard[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: InternationalStandard = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<InternationalStandard[]>): HttpResponse<InternationalStandard[]> {
        const jsonResponse: InternationalStandard[] = res.body;
        const body: InternationalStandard[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to InternationalStandard.
     */
    private convertItemFromServer(internationalStandard: InternationalStandard): InternationalStandard {
        const copy: InternationalStandard = Object.assign({}, internationalStandard);
        return copy;
    }

    /**
     * Convert a InternationalStandard to a JSON which can be sent to the server.
     */
    private convert(internationalStandard: InternationalStandard): InternationalStandard {
        const copy: InternationalStandard = Object.assign({}, internationalStandard);
        return copy;
    }
}
