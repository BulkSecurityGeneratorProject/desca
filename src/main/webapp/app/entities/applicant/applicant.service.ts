import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Applicant } from './applicant.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Applicant>;

@Injectable()
export class ApplicantService {

    private resourceUrl =  SERVER_API_URL + 'api/applicants';

    constructor(private http: HttpClient) { }

    create(applicant: Applicant): Observable<EntityResponseType> {
        const copy = this.convert(applicant);
        return this.http.post<Applicant>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(applicant: Applicant): Observable<EntityResponseType> {
        const copy = this.convert(applicant);
        return this.http.put<Applicant>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Applicant>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Applicant[]>> {
        const options = createRequestOption(req);
        return this.http.get<Applicant[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Applicant[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Applicant = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Applicant[]>): HttpResponse<Applicant[]> {
        const jsonResponse: Applicant[] = res.body;
        const body: Applicant[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Applicant.
     */
    private convertItemFromServer(applicant: Applicant): Applicant {
        const copy: Applicant = Object.assign({}, applicant);
        return copy;
    }

    /**
     * Convert a Applicant to a JSON which can be sent to the server.
     */
    private convert(applicant: Applicant): Applicant {
        const copy: Applicant = Object.assign({}, applicant);
        return copy;
    }
}
