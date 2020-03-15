import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { VulnerableGroup } from './vulnerable-group.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<VulnerableGroup>;

@Injectable()
export class VulnerableGroupService {

    private resourceUrl =  SERVER_API_URL + 'api/vulnerable-groups';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/vulnerable-groups';

    constructor(private http: HttpClient) { }

    create(vulnerableGroup: VulnerableGroup): Observable<EntityResponseType> {
        const copy = this.convert(vulnerableGroup);
        return this.http.post<VulnerableGroup>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(vulnerableGroup: VulnerableGroup): Observable<EntityResponseType> {
        const copy = this.convert(vulnerableGroup);
        return this.http.put<VulnerableGroup>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<VulnerableGroup>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<VulnerableGroup[]>> {
        const options = createRequestOption(req);
        return this.http.get<VulnerableGroup[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<VulnerableGroup[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<VulnerableGroup[]>> {
        const options = createRequestOption(req);
        return this.http.get<VulnerableGroup[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<VulnerableGroup[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: VulnerableGroup = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<VulnerableGroup[]>): HttpResponse<VulnerableGroup[]> {
        const jsonResponse: VulnerableGroup[] = res.body;
        const body: VulnerableGroup[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to VulnerableGroup.
     */
    private convertItemFromServer(vulnerableGroup: VulnerableGroup): VulnerableGroup {
        const copy: VulnerableGroup = Object.assign({}, vulnerableGroup);
        return copy;
    }

    /**
     * Convert a VulnerableGroup to a JSON which can be sent to the server.
     */
    private convert(vulnerableGroup: VulnerableGroup): VulnerableGroup {
        const copy: VulnerableGroup = Object.assign({}, vulnerableGroup);
        return copy;
    }
}
