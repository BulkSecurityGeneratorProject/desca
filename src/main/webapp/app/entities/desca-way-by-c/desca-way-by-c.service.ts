import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { DescaWayByC } from './desca-way-by-c.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<DescaWayByC>;

@Injectable()
export class DescaWayByCService {

    private resourceUrl =  SERVER_API_URL + 'api/desca-way-by-cs';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/desca-way-by-cs';

    constructor(private http: HttpClient) { }

    create(descaWayByC: DescaWayByC): Observable<EntityResponseType> {
        const copy = this.convert(descaWayByC);
        return this.http.post<DescaWayByC>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(descaWayByC: DescaWayByC): Observable<EntityResponseType> {
        const copy = this.convert(descaWayByC);
        return this.http.put<DescaWayByC>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<DescaWayByC>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<DescaWayByC[]>> {
        const options = createRequestOption(req);
        return this.http.get<DescaWayByC[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<DescaWayByC[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<DescaWayByC[]>> {
        const options = createRequestOption(req);
        return this.http.get<DescaWayByC[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<DescaWayByC[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: DescaWayByC = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<DescaWayByC[]>): HttpResponse<DescaWayByC[]> {
        const jsonResponse: DescaWayByC[] = res.body;
        const body: DescaWayByC[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to DescaWayByC.
     */
    private convertItemFromServer(descaWayByC: DescaWayByC): DescaWayByC {
        const copy: DescaWayByC = Object.assign({}, descaWayByC);
        return copy;
    }

    /**
     * Convert a DescaWayByC to a JSON which can be sent to the server.
     */
    private convert(descaWayByC: DescaWayByC): DescaWayByC {
        const copy: DescaWayByC = Object.assign({}, descaWayByC);
        return copy;
    }
}
