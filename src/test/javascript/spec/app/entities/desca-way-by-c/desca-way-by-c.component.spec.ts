/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DescaTestModule } from '../../../test.module';
import { DescaWayByCComponent } from '../../../../../../main/webapp/app/entities/desca-way-by-c/desca-way-by-c.component';
import { DescaWayByCService } from '../../../../../../main/webapp/app/entities/desca-way-by-c/desca-way-by-c.service';
import { DescaWayByC } from '../../../../../../main/webapp/app/entities/desca-way-by-c/desca-way-by-c.model';

describe('Component Tests', () => {

    describe('DescaWayByC Management Component', () => {
        let comp: DescaWayByCComponent;
        let fixture: ComponentFixture<DescaWayByCComponent>;
        let service: DescaWayByCService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DescaTestModule],
                declarations: [DescaWayByCComponent],
                providers: [
                    DescaWayByCService
                ]
            })
            .overrideTemplate(DescaWayByCComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(DescaWayByCComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DescaWayByCService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new DescaWayByC(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.descaWayByCS[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
