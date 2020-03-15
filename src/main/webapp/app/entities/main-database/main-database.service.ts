import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { MainDatabase } from './main-database.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<MainDatabase>;

@Injectable()
export class MainDatabaseService {

    private resourceUrl =  SERVER_API_URL + 'api/main-databases';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/main-databases';

    constructor(private http: HttpClient) { }

    create(mainDatabase: MainDatabase): Observable<EntityResponseType> {
        const copy = this.convert(mainDatabase);
        return this.http.post<MainDatabase>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(mainDatabase: MainDatabase): Observable<EntityResponseType> {
        const copy = this.convert(mainDatabase);
        return this.http.put<MainDatabase>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<MainDatabase>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<MainDatabase[]>> {
        const options = createRequestOption(req);
        return this.http.get<MainDatabase[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<MainDatabase[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<MainDatabase[]>> {
        const options = createRequestOption(req);
        return this.http.get<MainDatabase[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<MainDatabase[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: MainDatabase = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<MainDatabase[]>): HttpResponse<MainDatabase[]> {
        const jsonResponse: MainDatabase[] = res.body;
        const body: MainDatabase[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to MainDatabase.
     */
    private convertItemFromServer(mainDatabase: MainDatabase): MainDatabase {
        const copy: MainDatabase = Object.assign({}, mainDatabase);
        return copy;
    }

    /**
     * Convert a MainDatabase to a JSON which can be sent to the server.
     */
    private convert(mainDatabase: MainDatabase): MainDatabase {
        const copy: MainDatabase = Object.assign({}, mainDatabase);
        return copy;
    }
}
