import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { RepairType } from './repair-type.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<RepairType>;

@Injectable()
export class RepairTypeService {

    private resourceUrl =  SERVER_API_URL + 'api/repair-types';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/repair-types';

    constructor(private http: HttpClient) { }

    create(repairType: RepairType): Observable<EntityResponseType> {
        const copy = this.convert(repairType);
        return this.http.post<RepairType>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(repairType: RepairType): Observable<EntityResponseType> {
        const copy = this.convert(repairType);
        return this.http.put<RepairType>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<RepairType>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<RepairType[]>> {
        const options = createRequestOption(req);
        return this.http.get<RepairType[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<RepairType[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<RepairType[]>> {
        const options = createRequestOption(req);
        return this.http.get<RepairType[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<RepairType[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: RepairType = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<RepairType[]>): HttpResponse<RepairType[]> {
        const jsonResponse: RepairType[] = res.body;
        const body: RepairType[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to RepairType.
     */
    private convertItemFromServer(repairType: RepairType): RepairType {
        const copy: RepairType = Object.assign({}, repairType);
        return copy;
    }

    /**
     * Convert a RepairType to a JSON which can be sent to the server.
     */
    private convert(repairType: RepairType): RepairType {
        const copy: RepairType = Object.assign({}, repairType);
        return copy;
    }
}
