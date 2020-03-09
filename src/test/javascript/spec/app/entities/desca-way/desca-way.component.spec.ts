/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DescaTestModule } from '../../../test.module';
import { DescaWayComponent } from '../../../../../../main/webapp/app/entities/desca-way/desca-way.component';
import { DescaWayService } from '../../../../../../main/webapp/app/entities/desca-way/desca-way.service';
import { DescaWay } from '../../../../../../main/webapp/app/entities/desca-way/desca-way.model';

describe('Component Tests', () => {

    describe('DescaWay Management Component', () => {
        let comp: DescaWayComponent;
        let fixture: ComponentFixture<DescaWayComponent>;
        let service: DescaWayService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DescaTestModule],
                declarations: [DescaWayComponent],
                providers: [
                    DescaWayService
                ]
            })
            .overrideTemplate(DescaWayComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(DescaWayComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DescaWayService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new DescaWay(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.descaWays[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
