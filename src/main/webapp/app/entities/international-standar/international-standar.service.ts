import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { InternationalStandar } from './international-standar.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<InternationalStandar>;

@Injectable()
export class InternationalStandarService {

    private resourceUrl =  SERVER_API_URL + 'api/international-standars';

    constructor(private http: HttpClient) { }

    create(internationalStandar: InternationalStandar): Observable<EntityResponseType> {
        const copy = this.convert(internationalStandar);
        return this.http.post<InternationalStandar>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(internationalStandar: InternationalStandar): Observable<EntityResponseType> {
        const copy = this.convert(internationalStandar);
        return this.http.put<InternationalStandar>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<InternationalStandar>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<InternationalStandar[]>> {
        const options = createRequestOption(req);
        return this.http.get<InternationalStandar[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<InternationalStandar[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: InternationalStandar = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<InternationalStandar[]>): HttpResponse<InternationalStandar[]> {
        const jsonResponse: InternationalStandar[] = res.body;
        const body: InternationalStandar[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to InternationalStandar.
     */
    private convertItemFromServer(internationalStandar: InternationalStandar): InternationalStandar {
        const copy: InternationalStandar = Object.assign({}, internationalStandar);
        return copy;
    }

    /**
     * Convert a InternationalStandar to a JSON which can be sent to the server.
     */
    private convert(internationalStandar: InternationalStandar): InternationalStandar {
        const copy: InternationalStandar = Object.assign({}, internationalStandar);
        return copy;
    }
}
