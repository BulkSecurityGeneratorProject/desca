import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { MemberState } from './member-state.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<MemberState>;

@Injectable()
export class MemberStateService {

    private resourceUrl =  SERVER_API_URL + 'api/member-states';

    constructor(private http: HttpClient) { }

    create(memberState: MemberState): Observable<EntityResponseType> {
        const copy = this.convert(memberState);
        return this.http.post<MemberState>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(memberState: MemberState): Observable<EntityResponseType> {
        const copy = this.convert(memberState);
        return this.http.put<MemberState>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<MemberState>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<MemberState[]>> {
        const options = createRequestOption(req);
        return this.http.get<MemberState[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<MemberState[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: MemberState = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<MemberState[]>): HttpResponse<MemberState[]> {
        const jsonResponse: MemberState[] = res.body;
        const body: MemberState[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to MemberState.
     */
    private convertItemFromServer(memberState: MemberState): MemberState {
        const copy: MemberState = Object.assign({}, memberState);
        return copy;
    }

    /**
     * Convert a MemberState to a JSON which can be sent to the server.
     */
    private convert(memberState: MemberState): MemberState {
        const copy: MemberState = Object.assign({}, memberState);
        return copy;
    }
}
