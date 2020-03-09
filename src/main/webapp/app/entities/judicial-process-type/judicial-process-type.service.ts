import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JudicialProcessType } from './judicial-process-type.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<JudicialProcessType>;

@Injectable()
export class JudicialProcessTypeService {

    private resourceUrl =  SERVER_API_URL + 'api/judicial-process-types';

    constructor(private http: HttpClient) { }

    create(judicialProcessType: JudicialProcessType): Observable<EntityResponseType> {
        const copy = this.convert(judicialProcessType);
        return this.http.post<JudicialProcessType>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(judicialProcessType: JudicialProcessType): Observable<EntityResponseType> {
        const copy = this.convert(judicialProcessType);
        return this.http.put<JudicialProcessType>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<JudicialProcessType>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<JudicialProcessType[]>> {
        const options = createRequestOption(req);
        return this.http.get<JudicialProcessType[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<JudicialProcessType[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: JudicialProcessType = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<JudicialProcessType[]>): HttpResponse<JudicialProcessType[]> {
        const jsonResponse: JudicialProcessType[] = res.body;
        const body: JudicialProcessType[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to JudicialProcessType.
     */
    private convertItemFromServer(judicialProcessType: JudicialProcessType): JudicialProcessType {
        const copy: JudicialProcessType = Object.assign({}, judicialProcessType);
        return copy;
    }

    /**
     * Convert a JudicialProcessType to a JSON which can be sent to the server.
     */
    private convert(judicialProcessType: JudicialProcessType): JudicialProcessType {
        const copy: JudicialProcessType = Object.assign({}, judicialProcessType);
        return copy;
    }
}
