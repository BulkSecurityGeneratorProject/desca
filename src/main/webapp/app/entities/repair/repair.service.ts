import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Repair } from './repair.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Repair>;

@Injectable()
export class RepairService {

    private resourceUrl =  SERVER_API_URL + 'api/repairs';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/repairs';

    constructor(private http: HttpClient) { }

    create(repair: Repair): Observable<EntityResponseType> {
        const copy = this.convert(repair);
        return this.http.post<Repair>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(repair: Repair): Observable<EntityResponseType> {
        const copy = this.convert(repair);
        return this.http.put<Repair>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Repair>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Repair[]>> {
        const options = createRequestOption(req);
        return this.http.get<Repair[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Repair[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Repair[]>> {
        const options = createRequestOption(req);
        return this.http.get<Repair[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Repair[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Repair = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Repair[]>): HttpResponse<Repair[]> {
        const jsonResponse: Repair[] = res.body;
        const body: Repair[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Repair.
     */
    private convertItemFromServer(repair: Repair): Repair {
        const copy: Repair = Object.assign({}, repair);
        return copy;
    }

    /**
     * Convert a Repair to a JSON which can be sent to the server.
     */
    private convert(repair: Repair): Repair {
        const copy: Repair = Object.assign({}, repair);
        return copy;
    }
}
